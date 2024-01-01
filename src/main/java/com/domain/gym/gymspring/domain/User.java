package com.domain.gym.gymspring.domain;

import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_tb")
public class User {
    @Id
    @Column(name = "u_id")
    private int id;

    @Column(name = "u_gym")
    private int gym;

    @Column(name = "u_loginid")
    private String loginid;

    @Column(name = "u_passwd")
    private String passwd;

    @Column(name = "u_name")
    private String name;

    @Column(name = "u_role")
    private int role;

    @Column(name = "u_image")
    private String image;

    @Column(name = "u_sex")
    private int sex;

    @Column(name = "u_birth")
    private Timestamp birth;

    @Column(name = "u_phonenum")
    private String phonenum;

    @Column(name = "u_address")
    private String address;

    @Column(name = "u_startday")
    private Timestamp startday;

    @Column(name = "u_endday")
    private Timestamp endday;

    @Column(name = "u_date")
    private Timestamp date;
}
