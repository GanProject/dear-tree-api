package com.dear_tree.dear_tree.domain;

import com.dear_tree.dear_tree.dto.request.auth.SignUpRequestDto;
import com.dear_tree.dear_tree.dto.request.room.CreateRoomRequestDto;
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
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberRoom> memberRooms = new HashSet<>();

    private String roomname;

    private Icon icon;

    @CreatedDate
    private LocalDateTime created_at;

    @CreatedDate
    private LocalDateTime updated_at;

    public Room(CreateRoomRequestDto dto) {
        this.roomname = dto.getRoomname();
        this.icon = dto.getIcon();
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }
}
