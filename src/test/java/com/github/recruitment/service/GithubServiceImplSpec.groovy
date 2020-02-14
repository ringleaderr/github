package com.github.recruitment.service


import com.github.recruitment.gateway.GithubGateway
import com.github.recruitment.model.SearchGithubDto
import com.github.recruitment.util.GithubTransfer
import com.github.recruitment.util.GithubTransferImpl
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import spock.lang.Specification

import java.time.LocalDate

class GithubServiceImplSpec extends Specification {

    private GithubGateway githubGateway = Mock()
    private GithubTransfer githubTransfer = new GithubTransferImpl()
    private GithubService githubService = new GithubServiceImpl( githubGateway, githubTransfer )

    def 'Should searchRepositories return success response'() {

        given:
            githubGateway.searchRepositories( _ ) >> SearchGithubDto.builder()
                    .description( "description" )
                    .fullName( "fullName" )
                    .cloneUrl( 'https://github.com/owner/repositoryName.git' )
                    .stars( 2 )
                    .createdAt( LocalDate.of( 2020, 02, 14 ) )
                    .build()

        when:
            def response = githubService.searchRepositories( "owner", "repositoryName" )
        then:
            response.createdAt.isEqual( LocalDate.of( 2020, 02, 14 ) )
            response.description == "description"
            response.fullName == "fullName"
            response.stars == 2
    }


    def 'Should searchRepositories return fail Repository not found '() {

        given:
            githubGateway.searchRepositories( _ ) >> {
                throw new HttpClientErrorException( HttpStatus.NOT_FOUND, "Repository not found owner : owner repository name repositoryNotExist" )
            }

        when:
            githubService.searchRepositories( "owner", "repositoryNotExist" )
        then:
            def e = thrown( HttpClientErrorException.class )
            e.statusText == "Repository not found owner : owner repository name repositoryNotExist"
            e.statusCode == HttpStatus.NOT_FOUND
    }
}
