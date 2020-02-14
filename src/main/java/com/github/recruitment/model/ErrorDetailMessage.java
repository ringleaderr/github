package com.github.recruitment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ErrorDetailMessage {

    private static final String SERVICE_NOT_AVAILABLE = "Service is unavailable, please try again later.";

    private String code;
    private String description;

}
