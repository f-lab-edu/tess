package com.flab.tess.domain;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Column(name="login_id")
    private String loginId;

    @Column(name="password")
    private String password;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    //유저 입장에서 유저 한명이 많은 계좌를 가질 수 있음 1:N
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accountList = new ArrayList<Account>();

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    //엔티티 생성되기 전에 특정 필드들에 기본값 지정
    @PrePersist
    protected void onCreate() {
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.createdAt = LocalDateTime.now();
    }

    //  pdatedAt 필드를 현재 시간으로 설정
    @PreUpdate
    public void updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    public User encodePassword(BCryptPasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
        return this;
    }

    public boolean checkPassword(BCryptPasswordEncoder passwordEncoder, String input_password) {
        return passwordEncoder.matches(input_password, this.password);
    }
}
