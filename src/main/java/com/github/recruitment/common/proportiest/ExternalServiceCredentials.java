package com.github.recruitment.common.proportiest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalServiceCredentials {
    private String username;
    private String password;
}
