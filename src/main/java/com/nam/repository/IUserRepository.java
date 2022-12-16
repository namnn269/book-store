package com.nam.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.nam.entity.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
	Optional< User> findByUsername(String username);
	Optional< User> findByEmail(String email);
	
	@Query(value = "SELECT DISTINCT u FROM User u "
			+ " JOIN u.roles r "
			+ " WHERE r.id = :roleId "
			+ " AND u.enabled = :enabled "
			+ " AND ( u.fullName LIKE %:searchKey% OR u.username LIKE %:searchKey% OR u.email LIKE %:searchKey%) ")
	Page<User> findAllBySearchKeyAndRoleAndStatus(	@Param("searchKey") String searchKey,
													@Param("roleId") Long roleId, 
													@Param("enabled") boolean enabled, 
													Pageable pageable);
	
	@Query(value = "SELECT DISTINCT u FROM User u"
			+ " JOIN u.roles r "
			+ " WHERE r.id = :roleId "
			+ " AND ( u.fullName LIKE %:searchKey% OR u.username LIKE %:searchKey% OR u.email LIKE %:searchKey%) ")
	Page<User> findAllBySearchKeyAndRole(	@RequestParam("searchKey") String searchKey, 
											@RequestParam("roleId")	Long roleId, 
											Pageable pageable);
	
	@Query(value = "SELECT DISTINCT u FROM User u "
			+ " WHERE u.enabled = :enabled "
			+ " AND ( u.fullName LIKE %:searchKey% OR u.username LIKE %:searchKey% OR u.email LIKE %:searchKey%) ")
	Page<User> findAllBySearchKeyAndStatus(	@RequestParam("searchKey") String searchKey, 
											@RequestParam("enabled") boolean enabled, 
											Pageable pageable);
	
	@Query(value = "SELECT DISTINCT u FROM User u "
			+ " WHERE u.fullName LIKE %:searchKey% OR u.username LIKE %:searchKey% OR u.email LIKE %:searchKey% ")
	Page<User> findAllBySearchKey(@RequestParam("searchKey") String searchKey, Pageable pageable);
	

}












