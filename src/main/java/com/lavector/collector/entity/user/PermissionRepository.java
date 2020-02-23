package com.lavector.collector.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created on 12/10/2017.
 *
 * @author seveniu
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
