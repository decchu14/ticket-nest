package com.ticketnest.user_service.Repository;

import com.ticketnest.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
