package com.example.managingpromotions.api.filter;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class JsonRequestWrapper extends HttpServletRequestWrapper {

    @Getter
    @Setter
    private String content;

    JsonRequestWrapper(HttpServletRequest request) {
        super(request);

        var stringBuilder = new StringBuilder();
        try (InputStream inputStream = request.getInputStream()) {
            if (inputStream != null) {
                var reader = new BufferedReader(new InputStreamReader(inputStream));
                var charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = reader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                // make an empty string since there is no payload
                stringBuilder.append("");
            }
        } catch (IOException ignored) {
        }
        content = stringBuilder.toString();
    }

    @Override
    public ServletInputStream getInputStream() {
        final var byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }
}