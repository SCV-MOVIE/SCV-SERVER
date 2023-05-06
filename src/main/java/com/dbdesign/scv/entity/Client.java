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
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    private String password;
    private int point;

    @Column(name = "security_nm")
    private String securityNm;

    private String name;

    @Column(name = "phone_nm")
    private String phoneNm;

    @Column(name = "is_member")
    private char isMember;

    private String membership;
}
