package de.fschili.dlx.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import de.fschili.dlx.openapi.api.DownloadallApiDelegate;

@Service
public class DownloadAllService implements DownloadallApiDelegate {

    private final static Logger log = LoggerFactory.getLogger(DownloadAllService.class);

    @Override
    public ResponseEntity<Resource> downloadallGet() {

        String token = ServiceUtils.getTokenFromContext();
        if (token == null || token.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (TokenService.isValidToken(token)) {
            log.info("Returning all data for token '" + token + "'");
            return ServiceUtils.getResponseEntity(ListService.ZIP_FILENAME, ListService.ZIP_MIME_TYPE);
        }
        else {
            log.error("Unknown token '" + token + "'");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
