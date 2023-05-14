package com.dbdesign.scv.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateShowtimeDTO {

    private int showtimeId;
    private String startDate;
    private int round;
    private int movieId;
    private int theaterId;
}
