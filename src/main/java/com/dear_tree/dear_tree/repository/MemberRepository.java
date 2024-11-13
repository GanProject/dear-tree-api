package com.dear_tree.dear_tree.repository;

import com.dear_tree.dear_tree.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsernameAndStatus(@Param("username") String username, @Param("status") boolean status);
}
