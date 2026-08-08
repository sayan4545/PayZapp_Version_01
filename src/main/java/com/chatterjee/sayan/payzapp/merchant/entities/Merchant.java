package com.chatterjee.sayan.payzapp.merchant.entities;

import com.chatterjee.sayan.payzapp.common.enums.BusinessType;
import com.chatterjee.sayan.payzapp.common.enums.MerchantStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "merchant",indexes = {
        @Index(name = "idx_merchant_status",columnList = "status")
})
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

    @Column(unique = true)
    private String email;

    @Column(length = 12)
    private String phone;

    @Column(length = 12)
    private String gstId;

    @Column(length = 20)
    private String pan;


    @Enumerated(EnumType.STRING)
    private BusinessType businessType;

    @Column(length = 200)
    private String businessWebsiteUrl;

//    @OneToMany(mappedBy = "id")
//    private List<AppUser> appUsers;



    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MerchantStatus merchantStatus = MerchantStatus.PENDING_KYC;
    @Column(unique = true, length = 16)
    private String settlementBankAccountId;
    @Column(length = 30)
    private String settlementBankAccountIfsc;
    @Column(length = 30)
    private String settlementBankAccountHolderName;

    // Todo : add auditing layer by extending a base class and add createdAt, updateAt fields



}
