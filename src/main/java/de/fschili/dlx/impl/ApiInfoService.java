package de.fschili.dlx.impl;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import de.fschili.dlx.openapi.api.ApiInfoApiDelegate;
import de.fschili.dlx.openapi.model.ApiInfo;

@Service
public class ApiInfoService implements ApiInfoApiDelegate {

    private final static Logger log = LoggerFactory.getLogger(ApiInfoService.class);

    @Autowired
    private NativeWebRequest nativeWebRequest;

    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(nativeWebRequest);
    }

    @Override
    public ResponseEntity<ApiInfo> apiInfoGet() {
        log.debug("Got api request");

        return new ResponseEntity<ApiInfo>(getApiInfo(getRequest()), HttpStatus.OK);
    }

    private static String getPath(Optional<NativeWebRequest> request) {

        NativeWebRequest nwr = request.get();
        if (nwr != null) {
            Object nativeRequest = nwr.getNativeRequest();
            if (nativeRequest instanceof HttpServletRequest) {
                HttpServletRequest r = (HttpServletRequest) nativeRequest;

                StringBuilder sb = new StringBuilder();
                sb.append(r.getScheme());
                sb.append("://");
                sb.append(r.getServerName());
                sb.append(":");
                sb.append(r.getServerPort());

                String contextPath = r.getContextPath();
                if (contextPath != null && !contextPath.isEmpty()) {
                    sb.append("/");
                    sb.append(contextPath);
                }

                sb.append("/dlx/v1");

                return sb.toString();
            }

        }
        return "";

    }

    protected static ApiInfo getApiInfo(Optional<NativeWebRequest> request) {
        ApiInfo info = new ApiInfo();
        info.dlxVersion("1");
        info.vendorInformation("CHILI GmbH");

        info.apiBasePath("https://<server>/dlx/v1/");

        String basePath = getPath(request);
        if (basePath != null && !basePath.isEmpty()) {
            info.apiBasePath(basePath);
        }

        return info;
    }
}
