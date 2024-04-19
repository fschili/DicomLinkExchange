package de.fschili.dlx.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import de.fschili.dlx.openapi.api.ApiInfoApiDelegate;
import de.fschili.dlx.openapi.model.ApiInfo;

@Service
public class ApiInfoService implements ApiInfoApiDelegate {

    private final static Logger log = LoggerFactory.getLogger(ApiInfoService.class);

    @Override
    public ResponseEntity<ApiInfo> apiInfoGet() {
        log.debug("Got api request");

        return new ResponseEntity<ApiInfo>(getApiInfo(), HttpStatus.OK);
    }

    protected static ApiInfo getApiInfo() {
        ApiInfo info = new ApiInfo();
        info.dlxVersion("1.0");
        info.vendorInformation("CHILI GmbH");

        info.apiBasePath("https://<server>/dlx/v1/");

        return info;
    }
}
