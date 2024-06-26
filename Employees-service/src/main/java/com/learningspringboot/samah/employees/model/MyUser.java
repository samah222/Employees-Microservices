package com.learningspringboot.samah.employees.model;

import com.learningspringboot.samah.employees.Util.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyUser extends TrackingEntity { //implements UserDetails
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @NotBlank
    @Size(min = 8)
    private String password;
    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reset_verification_token_id")
    private ResetVerificationToken resetVerificationToken;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "change_password_verification_token_id")
    private ChangePasswordVerificationToken changePasswordVerificationToken;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "verification_token")
    private VerificationToken verificationToken;

    private int tokens = 0;
    private boolean enabled = false;

}
