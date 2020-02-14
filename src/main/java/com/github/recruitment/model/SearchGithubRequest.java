package com.github.recruitment.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchGithubRequest {
    private final String owner;
    private final String repositoryName;
}
