package com.dbdesign.scv.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateMovieDTO {

    private Long movieId;
    private int newLength;
    private String newRating;
    private String newDirector;
    private String newIntroduction;
    private String newDistributor;
    private String newImageUrl;
    private String newActor;
    private String newStaff;
}
