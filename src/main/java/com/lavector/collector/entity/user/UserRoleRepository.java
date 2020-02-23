package com.lavector.collector.entity.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created on 12/10/2017.
 *
 * @author seveniu
 */
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Page<UserRole> findByProjectId(Long projectId, Pageable pageable);
}
