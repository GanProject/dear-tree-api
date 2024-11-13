package com.dear_tree.dear_tree.domain;

import com.dear_tree.dear_tree.dto.request.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private boolean status;

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
