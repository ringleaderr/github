package com.github.recruitment.gateway

import com.github.recruitment.model.SearchGithubRequest
import com.github.recruitment.util.GithubTransfer
import com.github.recruitment.util.GithubTransferImpl
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class GithubGatewayImplSpec extends Specification {

    private RestTemplate okRestTemplate = Mock()
    private GithubGateway githubGateway
    private GithubTransfer githubTransfer = new GithubTransferImpl()

    def setup() {
        githubGateway = new GithubGatewayImpl( okRestTemplate, githubTransfer )
    }

    def 'Should searchRepositories return exception status 404'() {

        given:
            SearchGithubRequest searchDto = SearchGithubRequest.builder()
                    .owner( "testOwner" )
                    .repositoryName( "NotFoundRepo" )
                    .build()

            okRestTemplate.getForObject( _, _ ) >> { throw new HttpClientErrorException( HttpStatus.NOT_FOUND ) }

        when:
            githubGateway.searchRepositories( searchDto )
        then:
            def e = thrown( HttpClientErrorException.class )
            e.getStatusCode().value() == statusCode
            e.statusText.contains( responseMsg )

        where:
            responseMsg                                                           | statusCode
            "Repository not found owner : testOwner repository name NotFoundRepo" | 404
    }

    def 'Should searchRepositories return exception status 500'() {

        given:
            SearchGithubRequest searchDto = SearchGithubRequest.builder()
                    .owner( "testOwner" )
                    .repositoryName( "NotFoundRepo" )
                    .build()

            okRestTemplate.getForObject( _, _ ) >> { throw new HttpServerErrorException( HttpStatus.INTERNAL_SERVER_ERROR ) }

        when:
            githubGateway.searchRepositories( searchDto )
        then:
            def e = thrown( HttpServerErrorException.class )
            e.getStatusCode().value() == statusCode
            e.statusText.contains( responseMsg )

        where:
            responseMsg             | statusCode
            "INTERNAL_SERVER_ERROR" | 500
    }
}
