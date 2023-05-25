package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteMovieDTO {

    @ApiModelProperty(value = "삭제할 영화 id(Primary Key)", example = "1", required = true)
    private Long movieId;
}
