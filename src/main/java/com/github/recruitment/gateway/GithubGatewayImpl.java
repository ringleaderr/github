package com.github.recruitment.gateway;

import com.github.recruitment.model.SearchGithubDto;
import com.github.recruitment.model.SearchGithubRequest;
import com.github.recruitment.model.SearchGithubResponse;
import com.github.recruitment.util.GithubTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
class GithubGatewayImpl implements GithubGateway {

    private final RestTemplate okRestTemplate;
    private final GithubTransfer githubTransfer;

    @Autowired
    public GithubGatewayImpl( RestTemplate okRestTemplate,
            GithubTransfer githubTransfer ) {
        this.okRestTemplate = okRestTemplate;
        this.githubTransfer = githubTransfer;
    }

    public SearchGithubDto searchRepositories( SearchGithubRequest searchDto ) {
        log.info( "Fetch repository owner : {}, repository name : {}", searchDto.getOwner(), searchDto.getRepositoryName() );
        String endpoint = githubTransfer.createEndpoint( searchDto.getOwner(), searchDto.getRepositoryName() );
        try {
            SearchGithubResponse response = okRestTemplate.getForObject( endpoint, SearchGithubResponse.class );
            return SearchGithubDto.of( response );

        } catch ( HttpClientErrorException e ) {
            if ( e.getStatusCode() == HttpStatus.NOT_FOUND ) {
                throw new HttpClientErrorException( e.getStatusCode(), String.format ( "Repository not found owner : %s repository name %s" , searchDto.getOwner()  , searchDto.getRepositoryName() ) );
            }
            throw e;
        }
    }
}
