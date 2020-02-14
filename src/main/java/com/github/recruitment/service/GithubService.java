package com.github.recruitment.service;

import com.github.recruitment.model.SearchGithubDto;

public interface GithubService {
    SearchGithubDto searchRepositories( String owner, String repositoryName );
}
