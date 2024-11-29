package com.dear_tree.dear_tree.service;

import com.dear_tree.dear_tree.domain.Member;
import com.dear_tree.dear_tree.domain.MemberRoom;
import com.dear_tree.dear_tree.domain.Room;
import com.dear_tree.dear_tree.dto.request.room.CreateRoomRequestDto;
import com.dear_tree.dear_tree.dto.response.ResponseDto;
import com.dear_tree.dear_tree.dto.response.auth.AuthResponseDto;
import com.dear_tree.dear_tree.dto.response.room.RoomResponseDto;
import com.dear_tree.dear_tree.repository.MemberRepository;
import com.dear_tree.dear_tree.repository.MemberRoomRepository;
import com.dear_tree.dear_tree.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MemberRoomRepository memberRoomRepository;

    @Transactional
    public ResponseEntity<? super RoomResponseDto> createRoom(String username, CreateRoomRequestDto dto) {
        try {
            //방 추가
            Room room = new Room(dto);
            roomRepository.save(room);

            //매핑테이블 추가
            Member member = memberRepository.findByUsernameAndStatus(username, true);
            if (member == null || member.getStatus() == false)
                return ResponseDto.notExistUser();
            MemberRoom memberRoom = new MemberRoom();
            memberRoom.setMember(member);
            memberRoom.setRoom(room);
            memberRoomRepository.save(memberRoom);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return RoomResponseDto.success();
    }
}
