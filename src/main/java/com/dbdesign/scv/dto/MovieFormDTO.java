package com.dbdesign.scv.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MovieFormDTO {

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
}
