package com.flab.tess.domain;

import com.flab.tess.util.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_id", columnDefinition = "BIGINT UNSIGNED")
    private BigInteger accountId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="account_num")
    private String accountNum;

    @Column(name="account_name")
    private String accountName;

    @Column(name="account_type")
    private String accountType;

    @Column(name="balance")
    private BigDecimal balance;

    //하나의 계좌는 여러개의 계좌내역을 가짐
    @OneToMany(mappedBy = "receiverAccountId", cascade = CascadeType.ALL)
    private List<Transaction> receiveList = new ArrayList<Transaction>();

    //하나의 계좌는 여러개의 계좌내역을 가짐
    @OneToMany(mappedBy = "senderAccountId", cascade = CascadeType.ALL)
    private List<Transaction> sendList = new ArrayList<Transaction>();

    //잔액을 늘리는 메소드
    public Account deposit(BigDecimal amount){
        this.balance = this.balance.add(amount);
        return this;
    }

    //잔액 줄이는 메소드
    public Account withdraw(BigDecimal amount){
        //보내는 돈보다 현재 잔액이 많거나 같아야함
        if(this.balance.compareTo(amount)>=0){
            this.balance = this.balance.subtract(amount);
            return this;
        }
        else{
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
    }

}
