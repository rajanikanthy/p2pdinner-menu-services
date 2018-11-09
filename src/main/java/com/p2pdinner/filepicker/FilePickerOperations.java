package com.p2pdinner.filepicker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Locale;

/**
 * Created by rajaniy on 7/13/15.
 */
@Component
public class FilePickerOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilePickerOperations.class);

    @Autowired
    private Environment environment;

    @Autowired
    private RestTemplate restTemplate;


    @Value("${filepicker.api.key}")
    private String apiKey;

    @Value("${filepicker.api.secret}")
    private String apiSecretKey;

    @Value("${filepicker.api.uri}")
    private String apiUri;


    public FilePickerUploadResponse upload(String fileName) {
        LOGGER.info("Uploading file - {}", fileName);
        final MultiValueMap<String,Object> data = new LinkedMultiValueMap<String, Object>();
        try {
            Resource resource = new FileSystemResource(fileName);
            data.add("fileUpload", resource);
            data.add("filename", resource.getFile().getName());
            data.add("container", "uploads");
            URI uri = UriComponentsBuilder.fromUriString(apiUri).queryParam("key", this.apiKey).build().toUri();
            FilePickerUploadResponse response = restTemplate.postForObject(uri, data, FilePickerUploadResponse.class);
            return response;
        } catch (Exception excep) {
            LOGGER.error(excep.getMessage(), excep);
        }
        return null;

    }

}
