package com.github.recruitment.integration

import com.github.recruitment.RecruitmentApplication
import com.github.recruitment.common.RestTemplateConfiguration
import com.github.recruitment.gateway.GithubGateway
import com.github.recruitment.gateway.GithubGatewayImpl
import com.github.recruitment.model.SearchGithubRequest
import com.github.recruitment.util.GithubTransfer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Title

import java.time.LocalDate

@SpringBootTest(classes = [
        RecruitmentApplication,
        RestTemplateConfiguration,
        OkHttpClientFactory,
        GithubGateway,
        GithubGatewayImpl,
        RestTemplate,
        OkHttpClientFactory
])
@Title('Integration tests searching repository on Github')
class GithubIntegrationTest extends Specification {

    @Autowired
    private GithubTransfer githubTransfer

    @Autowired
    @Qualifier("OKRestTemplate")
    private RestTemplate restTemplate

    private GithubGateway gateway

    def setup() {
        gateway = new GithubGatewayImpl( restTemplate, githubTransfer )
    }

    def 'should return repository from Github'() {
        given:
            SearchGithubRequest searchGithubRequest = SearchGithubRequest.builder()
                    .owner( "octocat" )
                    .repositoryName( "Hello-World" )
                    .build()
        when:
            def response = gateway.searchRepositories( searchGithubRequest )

        then:
            response.fullName == "octocat/Hello-World"
            response.cloneUrl == "https://github.com/octocat/Hello-World.git"
            response.description == "My first repository on GitHub!"
            response.createdAt == LocalDate.of( 2011, 1, 26 )
    }

    def 'should return not found repository'() {
        given:
            SearchGithubRequest searchGithubRequest = SearchGithubRequest.builder()
                    .owner( "fake" )
                    .repositoryName( "fakeRepo" )
                    .build()
        when:
            gateway.searchRepositories( searchGithubRequest )
        then:
            def e = thrown( HttpClientErrorException.class )
            e.statusText == 'Repository not found owner : fake repository name fakeRepo'
    }
}