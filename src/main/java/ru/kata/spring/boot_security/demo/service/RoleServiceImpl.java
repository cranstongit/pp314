package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

   private final RoleDao roleDao;

   public RoleServiceImpl(RoleDao roleDao) {
      this.roleDao = roleDao;
   }

   public Set<Role> findAll() {
      return roleDao.findAll();
   }

   @Override
   public Set<Role> findAllOrThrow() {

      Set<Role> roles = roleDao.findAll();

      if (roles == null) {
         throw new EntityNotFoundException("Roles not found");
      }
      return roles;
   }

   public Set<Role> findByIds(List<Long> ids) {
      return roleDao.findByIds(ids);
   }

}
