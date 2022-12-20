package com.nam.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nam.entity.ResetPasswordToken;

@Repository
public interface IResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long>{
	Optional<ResetPasswordToken>  findByToken(String token);
	
}
