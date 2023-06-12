package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateGenreDTO {

    @ApiModelProperty(value = "장르 고유 id(Primary Key)", example = "1", required = true)
    private Long id;

    @ApiModelProperty(value = "새로운 장르 이름", example = "thriller", required = true)
    private String newName;
}
