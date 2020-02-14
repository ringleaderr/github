package com.github.recruitment.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
public class SearchGithubDto {

    private String fullName;
    private String description;
    private String cloneUrl;
    private int stars;
    private LocalDate createdAt;

    public static SearchGithubDto of( SearchGithubResponse response ) {
        return SearchGithubDto.builder()
                .fullName( response.getFullName() )
                .description( response.getDescription() )
                .cloneUrl( response.getCloneUrl() )
                .stars( response.getStargazersCount() )
                .createdAt( transferTime( response.getCreatedAt() ) )
                .build();
    }

    private static LocalDate transferTime( String createDate ) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern( pattern );
        LocalDateTime result = LocalDateTime.parse( createDate, inputFormatter );

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String output = dateTimeFormatter.format( result );
        return LocalDate.parse( output, dateTimeFormatter );
    }
}
