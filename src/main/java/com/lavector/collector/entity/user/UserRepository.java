package com.lavector.collector.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author tao
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
    List<User> findAll();

    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' AND u.id=:id")
    User findOneActive(@Param("id") long id);

    @Query("SELECT u FROM User u WHERE u.username =:username AND u.status ='ACTIVE'")
    User findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.email =:email AND u.status ='ACTIVE'")
    User findByEmailAndActive(@Param("email") String email);

    @Query("SELECT COUNT(u) FROM User u WHERE u.id <> :id AND u.email = :email and u.status = 'ACTIVE'")
    Integer countEmailAndActive(@Param("id") Long userId, @Param("email") String email);
}
