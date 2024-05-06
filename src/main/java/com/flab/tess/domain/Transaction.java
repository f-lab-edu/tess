package com.flab.tess.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="transaction_id", columnDefinition = "BIGINT UNSIGNED")
    private BigInteger transactionId;

    @ManyToOne
    @JoinColumn(name="receiver_account_id", referencedColumnName = "account_id")
    private Account receiverAccountId;

    //보내는 사람 = 나 = user
    @ManyToOne
    @JoinColumn(name="sender_account_id", referencedColumnName = "account_id")
    private Account senderAccountId;

    @Column
    private BigDecimal amount;

    @Column(name="transaction_at")
    private LocalDateTime transactionAt;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    // createdAt 필드를 현재 시간으로 설정
    @PrePersist
    public void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

    // updatedAt 필드를 현재 시간으로 설정
    @PreUpdate
    public void updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    public void transactionAt(LocalDateTime transactionAt){
        this.transactionAt = transactionAt;
    }

    public void saveAmount(BigDecimal amount){
        this.amount = amount;
    }

    public void saveReceiver(Account account){
        this.receiverAccountId = account;
    }

    public void saveSender(Account account){
        this.senderAccountId = account;
    }

}
