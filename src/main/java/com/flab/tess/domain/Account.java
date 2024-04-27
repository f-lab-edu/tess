package com.flab.tess.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_id")
    private BigInteger accountId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="account_num")
    private String accountNum;

    @Column(name="account_name")
    private String accountName;

    @ManyToOne
    @JoinColumn(name="account_type_id")
    private AccountType accountType;

    @Column(name="balance")
    private BigDecimal balance;

    @Column(name="created_at")
    private Timestamp createdAt;

    @Column(name="updated_at")
    private Timestamp updatedAt;

    //하나의 계좌는 여러개의 계좌내역을 가짐
    @OneToMany(mappedBy = "receiverAccountId", cascade = CascadeType.ALL)
    private List<Transaction> receiveList = new ArrayList<Transaction>();

    //하나의 계좌는 여러개의 계좌내역을 가짐
    @OneToMany(mappedBy = "senderAccountId", cascade = CascadeType.ALL)
    private List<Transaction> sendList = new ArrayList<Transaction>();
}
