package com.dbdesign.scv.dto;

import com.dbdesign.scv.entity.Theater;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TheaterDTO {

    @ApiModelProperty(value = "상영관 id(Primary Key)", example = "1", required = true)
    private Long id;

    @ApiModelProperty(value = "상영관 규격", example = "10x10", required = true)
    private String layout;

    @ApiModelProperty(value = "상영관 이름", example = "1관", required = true)
    private String name;

    @ApiModelProperty(value = "삭제 여부", example = "N", required = true)
    private char deleted;

    @ApiModelProperty(value = "상영관 테마 리스트", required = true)
    private String theaterType;

    public static TheaterDTO from(Theater entity) {

        return TheaterDTO.builder()
                .id(entity.getId())
                .layout(entity.getLayout())
                .name(entity.getName())
                .deleted(entity.getDeleted())
                .theaterType(entity.getTheaterType().getName())
                .build();
    }
}
