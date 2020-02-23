package com.lavector.collector.entity.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created on 12/10/2017.
 *
 * @author seveniu
 */
public interface UserRoleService {
    Page<UserRole> all(Pageable pageable);

    Page<UserRole> findByProjectId(Long projectId, Pageable pageable);

    UserRole save(UserRole userRole);

    void delete(Long id);

}
