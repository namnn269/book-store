package com.nam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nam.entity.Author;

@Repository
public interface IAuthorRepository extends JpaRepository<Author, Long> {
	
	Author findByFullname(String fullname);
}
