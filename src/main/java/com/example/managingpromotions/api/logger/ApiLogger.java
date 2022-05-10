package com.example.managingpromotions.api.logger;

import com.example.managingpromotions.api.filter.JsonRequestWrapper;
import com.example.managingpromotions.api.filter.JsonResponseWrapper;
import com.github.jknack.handlebars.internal.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ApiLogger {

    public void logRequest(JsonRequestWrapper requestWrapper, String correlationId) {
        Map<String, String> headers = new HashMap<>();
        var headerNames = requestWrapper.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            var headerName = headerNames.nextElement();
            var headerValue = requestWrapper.getHeader(headerName);
            headers.put(headerName, headerValue);
        }

        log.info("REQUEST | URI = {} | Method = {} | Remote address = {} | Headers = {} | Content = {}",
                getRequestURI(requestWrapper), requestWrapper.getMethod(), requestWrapper.getRemoteAddr(),
                headers, requestWrapper.getContent());
    }

    public void logResponse(JsonRequestWrapper requestWrapper, JsonResponseWrapper responseWrapper, long executionTimeMillis) {
        var headers = getResponseHeaders(responseWrapper);
        var content = getContent(responseWrapper);

        log.info("RESPONSE | URI = {} | Method = {} | Remote address = {} | Status code = {} | Execution time = {}ms | Content = {}",
                getRequestURI(requestWrapper), requestWrapper.getMethod(), requestWrapper.getRemoteAddr(),
                responseWrapper.getStatus(), executionTimeMillis, content);
    }

    public void logError(Exception exception, JsonRequestWrapper requestWrapper, JsonResponseWrapper responseWrapper, long executionTimeMillis) {
        var headers = getResponseHeaders(responseWrapper);
        var content = getContent(responseWrapper);

        log.error("ERROR | URI = {} | Method = {} | Remote address = {} | Status code = {} | Execution time = {}ms | Content = {} | Exception stack trace = {}",
                getRequestURI(requestWrapper), requestWrapper.getMethod(), requestWrapper.getRemoteAddr(),
                responseWrapper.getStatus(), executionTimeMillis, content, Arrays.toString(exception.getStackTrace()));
    }

    private String getContent(JsonResponseWrapper responseWrapper) {
        try {
            var content = responseWrapper.getCaptureAsString();
            if (content.length() > 5000) {
                content = content.substring(0, 5000);
            }
            return content;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private String getRequestURI(JsonRequestWrapper requestWrapper) {
        var queryParams = StringUtils.isNotBlank(requestWrapper.getQueryString()) ? "?" + requestWrapper.getQueryString() : "";
        return requestWrapper.getRequestURI() + queryParams;
    }

    private Map<String, String> getResponseHeaders(JsonResponseWrapper responseWrapper) {
        var headers = new HashMap<String, String>();
        responseWrapper.getHeaderNames().forEach(headerName ->
                headers.put(headerName, responseWrapper.getHeader(headerName))
        );
        return headers;
    }
}
