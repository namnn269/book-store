package com.nam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nam.entity.RegistrationToken;
@Repository
public interface IRegistraionTokenRepository extends JpaRepository<RegistrationToken, Long>{
	RegistrationToken findByToken(String token);
}
