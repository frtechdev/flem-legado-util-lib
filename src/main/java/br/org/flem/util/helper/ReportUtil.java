/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.flem.util.helper;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.fill.JRAbstractLRUVirtualizer;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author tscortes
 */
public class ReportUtil {
	
	  private ReportUtil() {
		  throw new IllegalStateException("Utility class");
	  }

    /**
     *
     * @param parameters
     * @param pathReport
     * @param collections
     * @return
     * @throws net.sf.jasperreports.engine.JRException
     */
    public static byte[] generatePDF(Map<String, Object> parameters, String pathReport, Collection<?> collections) throws JRException {
        JasperPrint print = getJasperPrint(parameters, pathReport, collections);
        return exportReportToPdf(print);
    }
    
    public static byte[] generatePDF(Map<String, Object> parameters, String pathReport, JRBeanCollectionDataSource dataSource) throws JRException {
        JasperPrint print = getJasperPrint(parameters, pathReport, dataSource);
        return exportReportToPdf(print);
    }
    /**	
     * 
     * @param parameters
     * @param pathReport
     * @param collections
     * @return 
     */
    public static JasperPrint getJasperPrint(Map<String, Object> parameters, String pathReport, Collection<?> collections) {
        return getJasperPrint(parameters, pathReport, new JRBeanCollectionDataSource(collections));
    }
    
    public static JasperPrint getJasperPrint(Map<String, Object> parameters, String pathReport, JRBeanCollectionDataSource dataSource) {
        try {
            URL a = ReportUtil.class.getClassLoader().getResource(pathReport);
            Map<String, Object> parametros = parameters;
            JRAbstractLRUVirtualizer virtualizer = new JRGzipVirtualizer(100);
            parametros.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
            JasperReport report = (JasperReport) JRLoader.loadObject(a);
            return JasperFillManager.fillReport(report, parametros, dataSource);
        } catch (JRException ex) {
            Logger.getLogger(ReportUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static byte[] exportReportToPdf(JasperPrint print) throws JRException{
        return JasperExportManager.exportReportToPdf(print);
    }

}
