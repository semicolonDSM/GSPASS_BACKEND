package com.semicolon.gspass.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    USER_ALREADY_EXIST(400, "User Already Exist."),
    USER_NOT_FOUND(404, "User Not Found."),

    INVALID_TOKEN(401, "Invalid Token."),

    SCHOOL_NOT_FOUND(404, "School Not Found."),
    SCHOOL_ALREADY_EXIST(400, "School Already Exist."),
    PARSE_ERROR(400, "Parse Error.");

    private final int status;
    private final String message;

}
