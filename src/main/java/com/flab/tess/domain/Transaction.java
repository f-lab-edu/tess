package com.flab.tess.domain;

import com.flab.tess.util.BaseEntity;
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
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="transaction_id", columnDefinition = "BIGINT UNSIGNED")
    private BigInteger transactionId;

    @ManyToOne
    @JoinColumn(name="receiver_account_id", referencedColumnName = "account_id")
    private Account receiverAccountId;

    @ManyToOne
    @JoinColumn(name="sender_account_id", referencedColumnName = "account_id")
    private Account senderAccountId;

    @Column
    private BigDecimal receiverBalance;

    @Column
    private BigDecimal senderBalance;

    @Column
    private BigDecimal amount;

    @Column(name="transaction_at")
    private LocalDateTime transactionAt;

    @PrePersist
    public void transactionAt() {
        this.transactionAt = LocalDateTime.now();
    }

    public Transaction saveTransaction(BigDecimal amount, Account receiverAccount, Account senderAccount, BigDecimal receiverBalance, BigDecimal senderBalance){
        this.amount = amount;
        this.receiverAccountId = receiverAccount;
        this.senderAccountId = senderAccount;
        this.receiverBalance = receiverBalance;
        this.senderBalance = senderBalance;
        return this;
    }


}
