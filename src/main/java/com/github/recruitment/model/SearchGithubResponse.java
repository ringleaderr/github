package com.github.recruitment.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming ( PropertyNamingStrategy.SnakeCaseStrategy.class )
public class SearchGithubResponse {
    private String cloneUrl;
    private String description;
    private String fullName;
    private int stargazersCount;
    private String createdAt;
}
