package com.dxvalley.epassbook.models;

import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private Boolean emailConfirmed;
    private String gender;
    private String birthDate;
    private String imageUrl;
    private String ip;
    private String createdAt;
    private String deletedAt;
    private Integer languageCode;
    private String phoneNumber;
    private Integer accessFailedCount;
    private Boolean twoFactorEnabled;
    private Boolean isEnabled;

    // user address
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    // user roles
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();

    // user accounts
    @OneToMany(targetEntity = Account.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId_fk", referencedColumnName = "userId")
    private List<Account> accounts;
    
    public Users(String username, String password, String fullName, String email, Boolean emailConfirmed, String gender,
            String birthDate, String imageUrl, String ip, String createdAt, String deletedAt, Integer languageCode, String phoneNumber,
            Integer accessFailedCount, Boolean twoFactorEnabled, Boolean isEnabled) {
        this.username = username;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.fullName = fullName;
        this.email = email;
        this.emailConfirmed = emailConfirmed;
        this.gender = gender;
        this.birthDate = birthDate;
        this.imageUrl = imageUrl;
        this.ip = ip;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.languageCode = languageCode;
        this.phoneNumber = phoneNumber;
        this.accessFailedCount = accessFailedCount;
        this.twoFactorEnabled = twoFactorEnabled;
        this.isEnabled = isEnabled;
        
    }

    public Users orElseThrow(Object object) {
        return null;
    }

}
