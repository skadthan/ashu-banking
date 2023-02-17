package com.ashu.banking.dao;

import org.springframework.data.repository.CrudRepository;

import com.ashu.banking.domain.security.Role;

public interface RoleDao extends CrudRepository<Role, Integer> {
    
    Role findByName(String name);
}
