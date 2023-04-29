package com.dbdesign.scv.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "theater")
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_occupied")
    private Number isOccupied;

    private String layout;
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_type_id")
    private TheaterType theaterType;
}
