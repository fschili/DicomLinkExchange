package de.fschili.dlx.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import de.fschili.dlx.openapi.api.DownloadApiDelegate;

@Service
public class DownloadService implements DownloadApiDelegate {

    private final static Logger log = LoggerFactory.getLogger(DownloadService.class);

    @Override
    public ResponseEntity<org.springframework.core.io.Resource> downloadIdGet(String id) {

        String token = ServiceUtils.getTokenFromContext();
        if (token == null || token.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (TokenService.isValidToken(token)) {
            if (id == null || id.isEmpty()) {
                log.error("No id for download given (id: '" + id + "')");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            log.info("Returning data for token '" + token + "' with id '" + id + "'.");

            if (id.equals(ListService.PDF_UUID)) {
                return ServiceUtils.getResponseEntity(ListService.PDF_FILENAME, ListService.PDF_MIME_TYPE);
            }
            else if (id.equals(ListService.JPEG_UUID)) {
                return ServiceUtils.getResponseEntity(ListService.JPEG_FILENAME, ListService.JPEG_MIME_TYPE);
            }
            else if (id.equals(ListService.ZIP_UUID)) {
                return ServiceUtils.getResponseEntity(ListService.ZIP_FILENAME, ListService.ZIP_MIME_TYPE);
            }
            else {
                log.error("Unknown id '" + id + "'");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        else {
            log.error("Unknown token '" + token + "'");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
