package com.example.demo.service.util.error;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public ResponseEntity<ResponseData<Object>> noResourceFoundException(
            NoResourceFoundException ex, HttpRequest request) {
        String url = request.uri().toString();

        int statusCode = HttpStatus.NOT_FOUND.value();
        String error = "Enpoint " + url + " khong ton tai";

        ResponseData<Object> data = createResponseData(statusCode, error, ex);

        return ResponseEntity.status(statusCode).body(data);
    }

    @ExceptionHandler(value = IncorrectResultSizeDataAccessException.class)
    public ResponseEntity<ResponseData<Object>> exceptionHandlerExceptionResolver(Exception ex) {

        int statusCode = HttpStatus.NOT_FOUND.value();
        String error = "Yêu câu 1 model nhưng kết quả trả về là 3";

        ResponseData<Object> data = createResponseData(statusCode, error, ex);

        return ResponseEntity.status(statusCode).body(data);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseData<Object>> methodArgumentNotValidException(MethodArgumentNotValidException ex) {

        int statusCode = HttpStatus.BAD_REQUEST.value();
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        List<Map<String, String>> errors = fieldErrors.stream()
                .map(fieldError -> Map.of(fieldError.getField(), fieldError.getDefaultMessage())).toList();
        // ! field : message
        ResponseData<Object> data = ResponseData.<Object>builder().status(statusCode).data(null).error("Lỗi Validate")
                .message(errors).build();

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

}
