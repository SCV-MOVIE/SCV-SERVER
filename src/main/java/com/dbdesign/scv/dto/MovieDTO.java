package com.dbdesign.scv.dto;

import com.dbdesign.scv.entity.Movie;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class MovieDTO {

    private Long id;
    private String name;
    private int length;
    private String rating;
    private String director;
    private String introduction;
    private String distributor;
    private String imgUrl;
    private String actor;
    private String staff;
    private List<GenreDTO> genreDTOList;

    public static MovieDTO from(Movie entity) {

        return  MovieDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .length(entity.getLength())
                .rating(entity.getRating())
                .director(entity.getDirector())
                .introduction(entity.getIntroduction())
                .distributor(entity.getDistributor())
                .imgUrl(entity.getImgUrl())
                .actor(entity.getActor())
                .staff(entity.getStaff())
                .genreDTOList(null) // service 단에서 추가
                .build();
    }
}
