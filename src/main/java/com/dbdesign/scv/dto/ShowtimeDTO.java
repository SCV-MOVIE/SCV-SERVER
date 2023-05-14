package com.dbdesign.scv.dto;

import com.dbdesign.scv.entity.Showtime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ShowtimeDTO {

    private Long id;
    private String startDate;
    private int round;
    private int isPublic;
    private int remainSeatNm; // 남은 좌석 수
    private int theaterSize; // 총 좌석 수
    private String movieName;
    private String theaterName;

    public static ShowtimeDTO from(Showtime entity) {

        return ShowtimeDTO.builder()
                .id(entity.getId())
                .startDate(entity.getStartDate())
                .round(entity.getRound())
                .isPublic(entity.getIsPublic()) // 어드민에서 공개 전환을 위해 보여줄 필요 있음
                .remainSeatNm(0) // service에서 채울 예정
                .theaterSize(0) // service에서 채울 예정
                .movieName("") // service에서 채울 예정
                .theaterName("") // service에서 채울 예정
                .build();
    }
}
