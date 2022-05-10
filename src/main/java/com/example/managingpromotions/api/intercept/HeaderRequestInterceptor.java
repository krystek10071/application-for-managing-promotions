package com.example.managingpromotions.api.intercept;

import lombok.NoArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@NoArgsConstructor
public class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final String REQUEST_UUID_KEY = "Request-UUID";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().set(REQUEST_UUID_KEY, MDC.get(REQUEST_UUID_KEY));
        return execution.execute(request, body);
    }
}