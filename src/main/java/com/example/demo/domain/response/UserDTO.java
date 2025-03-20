package com.example.demo.domain.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
// ! Thêm mỗi Builder thay vì Contructor
@Builder
public class UserDTO {
    private long id;
    private String name;
    private String email;
}
