package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Set<Role> findAll() {
        return new HashSet<>(entityManager
                .createQuery("SELECT r FROM Role r", Role.class)
                .getResultList());
    }

    @Override
    public Set<Role> findByIds(List<Long> ids) {
        return new HashSet<>(entityManager
                .createQuery("SELECT r FROM Role r WHERE r.id IN :ids", Role.class)
                .setParameter("ids", ids)
                .getResultList());
    }

}