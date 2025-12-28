package com.moeezy.PokeTracker.data.repository;

import com.moeezy.PokeTracker.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
