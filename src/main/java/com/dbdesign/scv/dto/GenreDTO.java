package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreDTO {

    @ApiModelProperty(value = "장르 이름", example = "horror", required = true)
    private String name;
}
