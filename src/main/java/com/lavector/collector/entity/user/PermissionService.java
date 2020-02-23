package com.lavector.collector.entity.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created on 12/10/2017.
 *
 * @author seveniu
 */
public interface PermissionService {
    List<Permission> all();

    Page<Permission> all(Pageable pageable);

    Permission save(Permission permission);

    void delete(Long id);

}
