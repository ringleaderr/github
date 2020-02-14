package com.github.recruitment.gateway;

import com.github.recruitment.model.SearchGithubDto;
import com.github.recruitment.model.SearchGithubRequest;

public interface GithubGateway {
    SearchGithubDto searchRepositories( SearchGithubRequest searchDto );
}
