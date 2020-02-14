package com.github.recruitment.util;

import com.github.recruitment.model.SearchGithubRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
class GithubTransferImpl implements GithubTransfer {

    public String createEndpoint( String owner, String repositoryName ) {
        return UriComponentsBuilder.newInstance()
                .scheme( "https" )
                .host( "api.github.com" )
                .path( "/repos/{owner}/{repoName}" )
                .buildAndExpand( owner, repositoryName )
                .toString();

    }

    @Override
    public SearchGithubRequest create( String owner, String repository ) {
        return SearchGithubRequest.builder()
                .owner( owner )
                .repositoryName( repository )
                .build();
    }
}
