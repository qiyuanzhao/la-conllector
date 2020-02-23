package com.lavector.collector.entity.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created on 12/10/2017.
 *
 * @author seveniu
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public Page<UserRole> all(Pageable pageable) {
        return userRoleRepository.findAll(pageable);
    }

    @Override
    public Page<UserRole> findByProjectId(Long projectId, Pageable pageable) {
        return userRoleRepository.findByProjectId(projectId, pageable);
    }

    @Override
    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public void delete(Long id) {
        userRoleRepository.deleteById(id);
    }
}
