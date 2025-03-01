package com.example.demo.service.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.demo.domain.response.ResponseData;

import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class FormatResponse implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {

        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        // ! Lấy status code

        HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int statusCode = httpResponse.getStatus();

        if (body instanceof String || statusCode >= 400) {
            return body;
        }

        ResponseData<Object> data = ResponseData.<Object>builder().status(statusCode).data(body)
                .message("CALL API THÀNH CÔNG").build();

        return data;
    }

}
