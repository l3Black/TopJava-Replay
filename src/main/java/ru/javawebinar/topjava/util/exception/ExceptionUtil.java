package ru.javawebinar.topjava.util.exception;

import org.springframework.validation.BindingResult;

import java.util.StringJoiner;

public class ExceptionUtil {
    public static String exceptionHandler(BindingResult result){
        StringJoiner joiner = new StringJoiner("<br>");
        result.getFieldErrors().forEach(
                fe -> joiner.add(String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
        );
        return joiner.toString();
    }
}
