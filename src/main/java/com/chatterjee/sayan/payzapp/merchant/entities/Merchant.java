package com.chatterjee.sayan.payzapp.merchant.entities;

import com.chatterjee.sayan.payzapp.common.enums.BusinessType;
import com.chatterjee.sayan.payzapp.common.enums.MerchantStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "merchant")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false,length = 200)
    private String name;
    @Column(nullable = false,length = 200)
    private String BusinessName;

    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false,length = 12)
    private String phone;
    @Column(nullable = false,length = 12)
    private String gstId;
    @Column(nullable = false)
    private String pan;


    @Enumerated(EnumType.STRING)
    private BusinessType businessType;
    @Column(nullable = false)
    private String businessWebsiteUrl;

//    @OneToMany(mappedBy = "id")
//    private List<AppUser> appUsers;



    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MerchantStatus merchantStatus = MerchantStatus.PENDING_KYC;
    @Column(nullable = false)
    private String settlementBankAccountId;
    @Column(nullable = false)
    private String settlementBankAccountIfsc;
    @Column(nullable = false)
    private String settlementBankAccountHolderName;

    // Todo : add auditing layer by extending a base class and add createdAt, updateAt fields



}
