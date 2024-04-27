//package com.flab.tess.domain;
//
//import lombok.*;
//
//import javax.persistence.*;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Table
//public class AccountType {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="account_type_id")
//    private BigInteger accountTypeId;
//
//    @Column(name="account_type_name")
//    private String accountTypeName;
//
//    //type 한 개는 여러개의 account를 가질 수 있음
//    @OneToMany(mappedBy = "accountType")
//    private List<Account> accountList = new ArrayList<Account>();
//}
