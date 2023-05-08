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
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int length;
    private String rating;
    private String director;
    private String introduction;
    private String distributor;

    @Column(name = "img_url")
    private String imgUrl;

    private String actor;
    private String staff;
    private char deleted = 'N';
}
