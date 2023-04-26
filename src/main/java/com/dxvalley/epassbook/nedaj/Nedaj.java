package com.dxvalley.epassbook.nedaj;

import com.dxvalley.epassbook.user.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nedaj {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String agentId;
    private String merchantId;
    private String fuelType;
    private String messageId;
    private String transactionID;
    private String debitAcctNo;
    private String debitAmount;
    private String transactionOrderedDate;
    private String transactionCompletedDate;
    private String paymentStatus;
    @ManyToOne
    private Users user;
}