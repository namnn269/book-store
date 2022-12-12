package com.nam.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nam.entity.Role;
@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByRoleName(String roleName);

}
