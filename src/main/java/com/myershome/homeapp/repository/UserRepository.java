package com.myershome.homeapp.repository;

import com.myershome.homeapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
