package com.example.yamyam16.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;


    private String nickname;

    // 필드 초기화용 생성자
    public User(UserType userType, String email, String password, String nickname) {
        this.userType = userType;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    @Setter
    private boolean deleted;

    @Setter
    private LocalDateTime deletedAt;

}