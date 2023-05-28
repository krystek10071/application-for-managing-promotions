package com.example.managingpromotions.api.filter;

import com.example.managingpromotions.api.logger.ApiLogger;
import com.example.managingpromotions.api.logger.ApiStore;
import com.github.jknack.handlebars.internal.lang3.StringUtils;
import com.google.common.collect.ImmutableList;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Order(0)
@Component
public class ApiLoggingFilter implements Filter {

    private static final String APPLICATION_JSON_HEADER_VALUE = "application/json";
    private static final String REQUEST_UUID_KEY = "Request-UUID";

    @Autowired
    private ApiStore apiStore;

    @Autowired
    private ApiLogger apiLogger;

    private final ImmutableList<String> endpointsWithoutLogs = ImmutableList.of("/health", "/prometheus");

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpServletRequest = (HttpServletRequest) request;
        if ((isJson(httpServletRequest) || HttpMethod.GET.toString().equals(httpServletRequest.getMethod()))
                && !endpointsWithoutLogs.contains(httpServletRequest.getRequestURI())) {
            doLogging(httpServletRequest, (HttpServletResponse) response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void doLogging(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var requestWrapper = new JsonRequestWrapper(request);
        var responseWrapper = new JsonResponseWrapper(response);
        var correlationId = getCorrelationId(request);
        apiStore.setCorrelationId(correlationId);
        MDC.put(REQUEST_UUID_KEY, correlationId);
        response.addHeader(REQUEST_UUID_KEY, correlationId);
        apiLogger.logRequest(requestWrapper);

        var stopWatch = new StopWatch();

        stopWatch.start();
        chain.doFilter(requestWrapper, responseWrapper);
        stopWatch.stop();

        if (apiStore.getException() != null) {
            apiLogger.logError(apiStore.getException(), requestWrapper, responseWrapper, stopWatch.getTotalTimeMillis());
        } else {
            apiLogger.logResponse(requestWrapper, responseWrapper, stopWatch.getTotalTimeMillis());
        }

        MDC.clear();
    }

    @Override
    public void destroy() {
    }

    private String getCorrelationId(HttpServletRequest request) {
        String correlationId = request.getHeader(REQUEST_UUID_KEY);
        return StringUtils.isEmpty(correlationId) ? UUID.randomUUID().toString() : correlationId;
    }

    private boolean isJson(HttpServletRequest servletRequest) {
        return isJsonAccept(servletRequest) || isJsonContentType(servletRequest);
    }

    private Boolean isJsonAccept(HttpServletRequest servletRequest) {
        var acceptHeader = servletRequest.getHeader(HttpHeaders.ACCEPT);
        return !StringUtils.isNotBlank(acceptHeader) && APPLICATION_JSON_HEADER_VALUE.equals(acceptHeader);
    }

    private boolean isJsonContentType(HttpServletRequest servletRequest) {
        return servletRequest.getContentType() != null && servletRequest.getContentType().contains(APPLICATION_JSON_HEADER_VALUE);
    }
}
