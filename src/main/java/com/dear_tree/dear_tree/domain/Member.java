package com.dear_tree.dear_tree.domain;

import com.dear_tree.dear_tree.dto.request.auth.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberRoom> memberRooms = new HashSet<>();

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean status;

    @CreatedDate
    private LocalDateTime created_at;

    @CreatedDate
    private LocalDateTime updated_at;

    public Member(SignUpRequestDto dto) {
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.status = true;
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

}
