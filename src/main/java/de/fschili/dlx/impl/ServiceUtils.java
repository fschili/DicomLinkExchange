package de.fschili.dlx.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import de.fschili.dlx.jwt.BasicAuthentication;

public class ServiceUtils {

    private final static Logger log = LoggerFactory.getLogger(ServiceUtils.class);

    public static String getTokenFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication instanceof BasicAuthentication) {
            return authentication.getName();
        }

        log.error("Could not get token from context.");
        return null;
    }

    public static ResponseEntity<Resource> getResponseEntity(String fileName, String mimeType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        try {
            log.debug("Loading file: " + fileName + " from classpath (war).");

            Resource resource = new ClassPathResource("data/" + fileName, ServiceUtils.class.getClassLoader());
            InputStreamResource streamResource = new InputStreamResource(resource.getInputStream());

            String[] type = mimeType.split("/");
            MediaType mediaType = new MediaType(type[0], type[1]);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(mediaType)
                    .body(streamResource);
        }
        catch (IOException e) {
            log.error("Could not respond to request (" + e + ")", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
