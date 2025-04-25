package com.example.yamyam16.auth.entity;

import com.example.yamyam16.store.entity.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // 자식 컬렉션을 가진 부모‑측 매핑
    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,   // 부모 → 자식으로 영속성 전이
            orphanRemoval = true)        // 컬렉션에서 빠지면 자식 레코드 삭제
    private List<Store> items = new ArrayList<>();

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
