package com.nam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nam.entity.ProfileUser;
import com.nam.entity.User;

@Repository
public interface IProfileRepository extends JpaRepository<ProfileUser, Long>{
	
	List<ProfileUser> findByUser(User user);
	
	
}
