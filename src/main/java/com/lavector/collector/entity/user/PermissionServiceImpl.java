package com.lavector.collector.entity.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on 12/10/2017.
 *
 * @author seveniu
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    PermissionRepository permissionRepository;

    @Override
    public List<Permission> all() {
        return permissionRepository.findAll();
    }

    @Override
    public Page<Permission> all(Pageable pageable) {
        return permissionRepository.findAll(pageable);
    }

    @Override
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }
}
