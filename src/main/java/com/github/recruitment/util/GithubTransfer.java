package com.github.recruitment.util;

import com.github.recruitment.model.SearchGithubRequest;

public interface GithubTransfer {

    String createEndpoint( String owner, String repositoryName );

    SearchGithubRequest create( String owner, String repository );
}
