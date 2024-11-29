package com.dear_tree.dear_tree.repository;

import com.dear_tree.dear_tree.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r " +
            "JOIN MemberRoom mr ON mr.room = r " +
            "JOIN Member m ON mr.member = m " +
            "WHERE m.username = :username AND m.status = :status")
    List<Room> findByUsernameAndStatus(String username, boolean status);

}
