package com.example.managingpromotions.api.filter;

import org.bouncycastle.util.io.TeeOutputStream;
import org.springframework.mock.web.DelegatingServletOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream capture;

    JsonResponseWrapper(HttpServletResponse response) {
        super(response);
        capture = new ByteArrayOutputStream(response.getBufferSize());
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new DelegatingServletOutputStream(new TeeOutputStream(super.getOutputStream(), capture));
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new DelegatingServletOutputStream(new TeeOutputStream(super.getOutputStream(), capture)));
    }

    private byte[] getCaptureAsBytes() {
        return capture.toByteArray();
    }

    public String getCaptureAsString() throws IOException {
        return new String(getCaptureAsBytes(), getCharacterEncoding());
    }

}