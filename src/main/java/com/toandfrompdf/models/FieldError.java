package com.toandfrompdf.models;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldError {

    private String field;
    private String errorCode;
    private String errorMessage;

}
