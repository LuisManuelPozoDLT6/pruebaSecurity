package com.example.projecttest.repositories;

import com.example.projecttest.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthorityRepository extends JpaRepository<Authority, Long> {

    public Authority findById(long id);
    Authority findByAuthority(String authority);
}
