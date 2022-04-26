/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.flem.util.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author tscortes
 */
@Service
public class FileStorageService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Carregar arquivo por nome
     *
     * @param url
     * @return
     */
    public Resource loadFile(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<?> entity = new HttpEntity(headers);
        ResponseEntity<Resource> response = restTemplate.exchange(url, HttpMethod.GET, entity, Resource.class);
        return response.getBody();
    }

    /**
     * Salvar arquivos
     *
     * @param pathFileName
     * @param file
     * @return
     * @throws java.io.IOException
     */
    public String store(String pathFileName, MultipartFile file) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", getFileResource(file));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

        return post(pathFileName, requestEntity);
    }

    /**
     * Enviar o arquivo para o servidor de arquivos
     *
     * @param filePath
     * @param requestEntity
     * @return
     */
    private String post(String fileCompleteUrl, HttpEntity<MultiValueMap<String, Object>> requestEntity) {
        ResponseEntity<String> response = restTemplate.exchange(fileCompleteUrl, HttpMethod.POST, requestEntity, String.class);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return response.getBody();
        }
        return null;
    }
    
	/**
	 * Convert Multipart in Resource
	 *
	 * @param filePart
	 * @return
	 * @throws IOException
	 */
    public Resource getFileResource(MultipartFile filePart) throws IOException {
        return new ByteArrayResource(filePart.getBytes()) {

            @Override
            public String getFilename() {
                return filePart.getOriginalFilename();
            }

            @Override
            public long contentLength() {
                return filePart.getSize();
            }
        };
    }
}
