package com.github.recruitment.controller;

import com.github.recruitment.model.SearchGithubDto;
import com.github.recruitment.model.SearchGithubResponse;
import com.github.recruitment.service.GithubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GithubController {

    private final GithubService githubService;

    @Autowired
    public GithubController( GithubService githubService ) {
        this.githubService = githubService;
    }

    @RequestMapping ( value = "repositories/{owner}/{repository-name}",
            produces = { "application/json" },
            method = RequestMethod.GET )
//    @GetMapping ("repositories/{owner}/{repositoryName}")
    public ResponseEntity<SearchGithubDto> getRepositories( @PathVariable ( "owner" ) String owner, @PathVariable ( "repository-name" ) String repositoryName ) {
        SearchGithubDto response = githubService.searchRepositories( owner, repositoryName );
        return ResponseEntity.ok( response );
    }
}
