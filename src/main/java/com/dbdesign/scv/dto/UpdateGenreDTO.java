package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateGenreDTO {

    @ApiModelProperty(value = "수정할 장르 이름", example = "thriller", required = true)
    private String oldName;

    @ApiModelProperty(value = "새로운 장르 이름", example = "comedy", required = true)
    private String newName;
}
