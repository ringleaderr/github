package com.github.recruitment.service;

import com.github.recruitment.gateway.GithubGateway;
import com.github.recruitment.model.SearchGithubDto;
import com.github.recruitment.model.SearchGithubRequest;
import com.github.recruitment.util.GithubTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class GithubServiceImpl implements GithubService {

    private final GithubGateway githubGateway;
    private final GithubTransfer githubTransfer;

    @Autowired
    public GithubServiceImpl( GithubGateway githubGateway, GithubTransfer githubTransfer ) {
        this.githubGateway = githubGateway;
        this.githubTransfer = githubTransfer;
    }

    public SearchGithubDto searchRepositories( String owner, String repositoryName ) {
        SearchGithubRequest searchDto = githubTransfer.create( owner, repositoryName );
        SearchGithubDto searchGithubResponse = githubGateway.searchRepositories( searchDto );
        return searchGithubResponse;
    }
}
