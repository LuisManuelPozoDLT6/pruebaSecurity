package com.example.projecttest.repositories;

import com.example.projecttest.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsersRepository extends JpaRepository<Users, Long> {

    Boolean existsByUsername(String username);
}
