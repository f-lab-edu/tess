package com.flab.tess.domain;

import lombok.*;
import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", columnDefinition = "BIGINT UNSIGNED")
    private BigInteger userId;

    @Column(name="name")
    private String name;

    @Column(name="password")
    private String password;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    //유저 입장에서 유저 한명이 많은 계좌를 가질 수 있음 1:N
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accountList = new ArrayList<Account>();

    // createdAt 필드를 현재 시간으로 설정
    @PrePersist
    public void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

    //  pdatedAt 필드를 현재 시간으로 설정
    @PreUpdate
    public void updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

}