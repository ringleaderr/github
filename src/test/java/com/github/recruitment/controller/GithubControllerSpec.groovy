package com.github.recruitment.controller

import com.github.recruitment.ErrorHandlerControllerAdvice
import com.github.recruitment.gateway.GithubGateway
import com.github.recruitment.model.SearchGithubDto
import com.github.recruitment.model.SearchGithubRequest
import com.github.recruitment.service.GithubService
import com.github.recruitment.service.GithubServiceImpl
import com.github.recruitment.util.GithubTransfer
import com.github.recruitment.util.GithubTransferImpl
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.client.HttpClientErrorException
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class GithubControllerSpec extends Specification {

    private GithubGateway githubGateway = Mock()
    private GithubTransfer githubTransfer = new GithubTransferImpl()
    private GithubService githubService
    private GithubController githubController
    private MockMvc mockMvc

    void setup() {
        githubService = new GithubServiceImpl( githubGateway, githubTransfer )
        githubController = new GithubController( githubService )
        ErrorHandlerControllerAdvice errorHandlerControllerAdvice = new ErrorHandlerControllerAdvice()

        mockMvc = MockMvcBuilders.standaloneSetup( githubController )
                .setControllerAdvice( errorHandlerControllerAdvice )
                .build()
    }

    def 'Should searchRepositories return success response'() {

        given:
            githubGateway.searchRepositories( _ ) >> expectResponse()

        when:
            def response = mockMvc.perform( get( '/repositories/repo/repogit' ) )
        then:
            response.andExpect( status().isOk() )
                    .andExpect( jsonPath( '$.fullName' ).value( 'fullName' ) )
                    .andExpect( jsonPath( '$.description' ).value( 'description' ) )
                    .andExpect( jsonPath( '$.cloneUrl' ).value( 'https://github.com/repo/repogit.git' ) )
                    .andExpect( jsonPath( '$.stars' ).value( '2' ) )
                    .andExpect( jsonPath( '$.createdAt' ).value( [2020, 2, 14] ) )
    }

    @Unroll
    def 'Should return status #statusCode on #endpoint'() {

        given:
            githubGateway.searchRepositories( { SearchGithubRequest s -> s.owner == "testOwner"; s.repositoryName == 'NotFoundRepo' } ) >> {
                throw new HttpClientErrorException( HttpStatus.NOT_FOUND, description )
            }
            githubGateway.searchRepositories( { SearchGithubRequest s -> s.owner == "testOwner"; s.repositoryName == 'connectionIssue' } ) >> {
                throw new Exception()
            }

        when:
            def response = mockMvc.perform( get( endpoint ) )

        then:
            response
                    .andExpect( status().is( statusCode ) )
                    .andExpect( jsonPath( '$.description' ).value( description ) )

        where:
            endpoint                                  || description                                                           || statusCode
            "/repositories/testOwner/NotFoundRepo"    || "Repository not found owner : testOwner repository name NotFoundRepo" || 404
            "/repositories/testOwner/connectionIssue" || "Service is currently unavailable, please try again later."           || 500
    }

    static SearchGithubDto expectResponse() {
        return SearchGithubDto.builder()
                .description( "description" )
                .fullName( "fullName" )
                .cloneUrl( 'https://github.com/repo/repogit.git' )
                .stars( 2 )
                .createdAt( LocalDate.of( 2020, 02, 14 ) )
                .build()
    }
}
