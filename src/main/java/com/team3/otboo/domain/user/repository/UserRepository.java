package com.team3.otboo.domain.user.repository;

import com.team3.otboo.domain.user.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, UserRepositoryCustom {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
