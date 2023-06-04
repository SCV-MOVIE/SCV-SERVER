package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MovieFormDTO {

    @ApiModelProperty(value = "영화 이름", example = "saw", required = true)
    private String name;

    @ApiModelProperty(value = "영화 길이", example = "221", required = true)
    private int length;

    @ApiModelProperty(value = "영화 등급", example = "18+", required = true)
    private String rating;

    @ApiModelProperty(value = "감독", example = "director example", required = true)
    private String director;

    @ApiModelProperty(value = "영화 소개", example = "introduction example", required = true)
    private String introduction;

    @ApiModelProperty(value = "배급사", example = "CJ", required = true)
    private String distributor;

    @ApiModelProperty(value = "영화 포스터 URL", example = "imgUrl example", required = true)
    private String imgUrl;

    @ApiModelProperty(value = "배우 리스트", example = "Andrew Garfield, Emma Stone", required = true)
    private String actor;

    @ApiModelProperty(value = "스태프 리스트", example = "staff1, staff2, staff3", required = true)
    private String staff;

    @ApiModelProperty(value = "장르 리스트", required = true)
    private List<GenreDTO> genreDTOList;
}
