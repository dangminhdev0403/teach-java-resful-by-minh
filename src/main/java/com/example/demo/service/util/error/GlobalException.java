package com.example.demo.service.util.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.demo.domain.response.ResponseData;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j(topic = "GlobalException")
public class GlobalException {

    private ResponseData<Object> createResponseData(int statusCode, String error, Exception ex) {

        ResponseData<Object> data = ResponseData.<Object>builder().status(statusCode).data(null).error(error)
                .message(ex.getMessage()).build();

        return data;
    }

    // !Ngoại lệ chung
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseData<Object>> commonException(Exception ex) {

        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String error = "Một lỗi với vẩn nào đó chưa fix";

        ResponseData<Object> data = createResponseData(statusCode, error, ex);

        return ResponseEntity.status(statusCode).body(data);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ResponseData<Object>> noResourceFoundException(Exception ex) {

        int statusCode = HttpStatus.NOT_FOUND.value();
        String error = "Enpoint khong ton tai";

        ResponseData<Object> data = createResponseData(statusCode, error, ex);

        return ResponseEntity.status(statusCode).body(data);
    }

}
