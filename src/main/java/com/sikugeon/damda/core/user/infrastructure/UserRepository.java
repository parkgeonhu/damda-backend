package com.sikugeon.damda.core.user.infrastructure;

import com.sikugeon.damda.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String name);
}