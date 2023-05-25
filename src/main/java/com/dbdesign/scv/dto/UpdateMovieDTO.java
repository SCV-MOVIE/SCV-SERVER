package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UpdateMovieDTO {

    @ApiModelProperty(value = "수정할 영화 id(Primary Key)", example = "1", required = true)
    private Long movieId;

    @ApiModelProperty(value = "새로운 영화 길이", example = "221", required = true)
    private int newLength;

    @ApiModelProperty(value = "새로운 영화 등급", example = "18+", required = true)
    private String newRating;

    @ApiModelProperty(value = "새로운 감독", example = "director example", required = true)
    private String newDirector;

    @ApiModelProperty(value = "새로운 영화 소개", example = "introduction example", required = true)
    private String newIntroduction;

    @ApiModelProperty(value = "새로운 배급사", example = "CJ", required = true)
    private String newDistributor;

    @ApiModelProperty(value = "새로운 영화 포스터 URL", example = "imgUrl example", required = true)
    private String newImgUrl;

    @ApiModelProperty(value = "새로운 배우 리스트", example = "Andrew Garfield, Emma Stone", required = true)
    private String newActor;

    @ApiModelProperty(value = "새로운 스태프 리스트", example = "staff1, staff2, staff3", required = true)
    private String newStaff;

    @ApiModelProperty(value = "새로운 장르 리스트", example = "horror", required = true)
    private List<GenreDTO> genreDTOList;
}
