package com.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.registration.model.RegUser;

public interface RegUserRepository extends JpaRepository<RegUser, Long> {

	RegUser findByEmail(String email);

	@Transactional
	Long deleteByEmail(String email);

	@Modifying
	@Transactional
	@Query(value = "UPDATE regusers SET rolerest = :userRoleRest WHERE email = :userEmail", nativeQuery = true)

	void updateRoleRest(@Param("userEmail") String userEmail, @Param("userRoleRest") String userRoleRest);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE regusers SET email = :userEmail, first_name = :userFirstName, last_name = :userLastName WHERE id = :userId", nativeQuery = true)

	void updateUserDetails(@Param("userEmail") String userEmail, @Param("userFirstName") String userFirstName, @Param("userLastName") String userLastName, @Param("userId") Long userId);


}