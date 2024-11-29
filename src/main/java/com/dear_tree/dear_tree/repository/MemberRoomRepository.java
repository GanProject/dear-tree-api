package com.dear_tree.dear_tree.repository;

import com.dear_tree.dear_tree.domain.MemberRoom;
import com.dear_tree.dear_tree.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRoomRepository extends JpaRepository<MemberRoom, Long> {

}
