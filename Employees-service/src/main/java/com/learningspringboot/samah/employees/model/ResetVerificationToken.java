package com.learningspringboot.samah.employees.model;

import com.learningspringboot.samah.employees.Util.TokenExpirationTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetVerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime expirationTime;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "resetVerificationToken")
    private MyUser myUser;

    public ResetVerificationToken(MyUser myUser, String token) {
        super();
        this.token = token;
        this.myUser = myUser;
        this.expirationTime = TokenExpirationTime.calculateExpirationDate();
    }

    public ResetVerificationToken(String token) {
        super();
        this.token = token;
        this.expirationTime = TokenExpirationTime.calculateExpirationDate();
    }
}
