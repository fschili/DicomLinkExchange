package de.fschili.dlx.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import de.fschili.dlx.jwt.BasicAuthentication;
import de.fschili.dlx.openapi.api.ListApiDelegate;
import de.fschili.dlx.openapi.model.DataItem;
import de.fschili.dlx.openapi.model.DataItems;

@Service
public class ListService implements ListApiDelegate {

    private final static Logger log = LoggerFactory.getLogger(ListService.class);

    @Override
    public ResponseEntity<DataItems> listGet() {

        String token = getTokenFromContext();
        if (token == null || token.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (token.equals(TokenService.TOKEN_BIRTHDATE)) {
            log.info("List data for birthdate token '" + token + "'");

            DataItems result = new DataItems();
            result.addDataItemItem(new DataItem("abc", "application/pdf", "report.pdf"));
            result.addDataItemItem(new DataItem("111", "application/dicom", "image001.dcm"));
            result.addDataItemItem(new DataItem("112", "application/dicom", "image002.dcm"));
            result.addDataItemItem(new DataItem("113", "application/dicom", "image003.dcm"));
            result.addDataItemItem(new DataItem("114", "application/dicom", "image004.dcm"));

            return new ResponseEntity<DataItems>(result, HttpStatus.OK);
        }
        else if (token.equals(TokenService.TOKEN_CUSTOM)) {
            log.info("List data for custom token '" + token + "'");

            DataItems result = new DataItems();
            result.addDataItemItem(new DataItem("xyz", "application/pdf", "report.pdf"));
            result.addDataItemItem(new DataItem("222", "application/dicom", "image222.dcm"));

            return new ResponseEntity<DataItems>(result, HttpStatus.OK);
        }
        else {
            log.error("Unknown token '" + token + "'");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private String getTokenFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication instanceof BasicAuthentication) {
            return authentication.getName();
        }

        log.error("Could not get token from context.");
        return null;
    }
}
