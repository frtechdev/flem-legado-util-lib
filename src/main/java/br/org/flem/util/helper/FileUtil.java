/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.flem.util.helper;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

import br.org.flem.util.conts.FileType;

/**
 *
 * @author tscortes
 */
public class FileUtil {

	public static final String FILE_SEPARATOR = "/";
	private static final Pattern ENV_VAR = Pattern.compile("((?<=\\$\\{)[a-zA-Z_0-9]*(?=\\}))");

	private FileUtil() {

	}

	/**
	 *
	 * @param input
	 * @param folderOut
	 * @param contentType
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static List<String> fromPdfToImage(Resource input, String folderOut, String contentType, String filename)
			throws IOException {
		return fromPdfToImage(input.getInputStream(), folderOut, contentType, filename);
	}

	/**
	 *
	 * @param input
	 * @param folderOut
	 * @param contentType
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static List<String> fromPdfToImage(InputStream input, String folderOut, String contentType, String filename)
			throws IOException {
		String type = StringUtils.isEmpty(contentType) ? FileType.JPG.getValue() : contentType;
		List<String> paths = new ArrayList<>();
		PDDocument document = PDDocument.load(input);
		PDFRenderer pdfRenderer = new PDFRenderer(document);
		for (int page = 0; page < document.getNumberOfPages(); ++page) {
			String pathOut = folderOut + getFileNameWithoutExtension(filename) + "-" + page + "." + type;
			fromPdfToImage(pdfRenderer, pathOut, page, type);
			paths.add(pathOut);
		}
		document.close();
		return paths;
	}

	/**
	 *
	 * @param pdfRenderer
	 * @param pathOut
	 * @param page
	 * @param contentType
	 * @throws IOException
	 */
	public static void fromPdfToImage(PDFRenderer pdfRenderer, String pathOut, int page, String contentType)
			throws IOException {
		BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
		File outputfile = new File(pathOut);
		if (!outputfile.exists()) {
			outputfile.mkdirs();
		}
		Logger.getLogger(FileUtil.class.getName()).info(outputfile.getAbsolutePath());
		ImageIO.write(bim, contentType, outputfile);
	}

	/**
	 *
	 * @param filePath
	 * @return
	 */
	public static String getFileExtension(String filename) {
		return FilenameUtils.getExtension(filename);
	}
	
	public static String getBaseFilename(String filename) {
		return FilenameUtils.getBaseName(filename);
	}
	
	public static String getBaseFilenameNormalize(String filename) {
		return getBaseFilename(normalize(filename));
	}
	
    public static String normalize(String fileName) {
        return Normalizer.normalize(fileName, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

	/**
	 *
	 * @param path
	 * @return
	 */
	public static String getFileNameWithoutExtension(String path) {
		String leaf = getLeaf(path);
		int index = leaf.lastIndexOf('.');
		if (index != -1) {
			leaf = leaf.substring(0, index);
		}
		return leaf;
	}

	/**
	 *
	 * @param path
	 * @return
	 */
	public static String getLeaf(String path) {
		if (path != null) {
			String normalisedPath = expand(path);
			if (normalisedPath.endsWith(FILE_SEPARATOR)) {
				normalisedPath = normalisedPath.substring(0, normalisedPath.length() - FILE_SEPARATOR.length());
			}
			int index = normalisedPath.lastIndexOf(FILE_SEPARATOR);
			if (index != -1) {
				return normalisedPath.substring(index + 1);
			}
		}
		return path;
	}

	/**
	 *
	 * @param path
	 * @return
	 */
	public static String expand(String path) {
		String expandedPath = path;
		if (expandedPath != null) {
			Matcher matcher = ENV_VAR.matcher(expandedPath);
			while (matcher.find()) {
				String envVar = matcher.group();

				String expVar = System.getenv(envVar);
				if (expVar != null) {
					expandedPath = expandedPath.replace(String.format("${%s}", envVar), expVar);
				}

				expVar = System.getProperty(envVar);
				if (expVar != null) {
					expandedPath = expandedPath.replace(String.format("${%s}", envVar), expVar);
				}
			}
		}
		return normalise(expandedPath);
	}

	/**
	 *
	 * @param path
	 * @return
	 */
	public static String normalise(String path) {
		try {
			return new File(path).getCanonicalPath();
		} catch (IOException e) {
			return path;
		}
	}
	
	
	public static String convertImageToPdf(InputStream input, String pathTo, String originalFinaName) throws DocumentException, IOException {
        Document convertJpgToPdf = new Document();
        String [] arrayPath = pathTo.split("\\.");
        String finalFileName = "";
        FileOutputStream out = null;
        if (pathTo.contains(originalFinaName)) {
        	pathTo = FILE_SEPARATOR + arrayPath[1] + FILE_SEPARATOR + arrayPath[2] + FILE_SEPARATOR + arrayPath[3] + FILE_SEPARATOR + arrayPath[4];
            finalFileName = pathTo + FILE_SEPARATOR + getFileNameWithoutExtension(arrayPath[5]) + "."+FileType.PDF.getValue();
            out = new FileOutputStream(finalFileName);
        } else {
            finalFileName = pathTo + getFileNameWithoutExtension(originalFinaName) + "."+FileType.PDF.getValue();
            out = new FileOutputStream(finalFileName);
        }
        File newImage = null;
        String originalName = getFileExtension(originalFinaName);
        if (originalName != null && originalName.contains(FileType.PNG.getValue())) {
            newImage = new File(pathTo + FILE_SEPARATOR + originalFinaName);
        } else {
            newImage = convertJPEGToJPG(input, pathTo, originalFinaName);
        }
        if (newImage != null) {
            PdfWriter.getInstance(convertJpgToPdf, out);
            convertJpgToPdf.open();

            Image convertJpg = Image.getInstance(newImage.getPath());
            float scaleRatio = calculateScaleRatio(convertJpgToPdf, convertJpg);
            if (scaleRatio < 1F) {
                convertJpg.scalePercent(scaleRatio * 100F);
            }
            //Add image to Document
            convertJpgToPdf.add(convertJpg);
            //Close Document
            convertJpgToPdf.close();
            newImage.delete();
        }
        return finalFileName;
    }

    public static File convertJPEGToJPG(InputStream entrada, String pathTo, String originalFinaName) throws IOException {
        
        String saida = pathTo + FILE_SEPARATOR + getFileNameWithoutExtension(originalFinaName) + "_nova."+FileType.JPG.getValue();
        BufferedImage bufferedImage = ImageIO.read(entrada);
        File novoArquivo = null;
        if (bufferedImage != null) {
            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            novoArquivo = new File(saida);
            ImageIO.write(newBufferedImage, FileType.JPG.getValue(), novoArquivo);
        }
        return novoArquivo;
    }

    /**
     * *
     * Calculate scale ratio required to fit the supplied image in the supplied
     * PDF document.
     *
     * @param doc PDF to fit image in.
     * @param image Image to be converted into a PDF.
     * @return Scale ratio (0.0 - 1.0), or 1.0 if no scaling is required to fit
     * the image.
     */
    private static float calculateScaleRatio(Document doc, Image image) {
        float scaleRatio;
        float imageWidth = image.getWidth();
        float imageHeight = image.getHeight();
        if (imageWidth > 0 && imageHeight > 0) {
            // Firstly get the scale ratio required to fit the image width
            Rectangle pageSize = doc.getPageSize();
            float pageWidth = pageSize.getWidth() - doc.leftMargin() - doc.rightMargin();
            scaleRatio = pageWidth / imageWidth;
            // Now get scale ratio required to fit image height - if smaller, use this instead
            float pageHeight = pageSize.getHeight() - doc.topMargin() - doc.bottomMargin();
            float heightScaleRatio = pageHeight / imageHeight;
            if (heightScaleRatio < scaleRatio) {
                scaleRatio = heightScaleRatio;
            }
            // Do not upscale - if the entire image can fit in the page, leave it unscaled.
            if (scaleRatio > 1F) {
                scaleRatio = 1F;
            }
        } else {
            // No scaling if the width or height is zero.
            scaleRatio = 1F;
        }
        return scaleRatio;
    }

}
