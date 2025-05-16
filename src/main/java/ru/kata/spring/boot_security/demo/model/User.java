package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "first_name", nullable = false, length = 100)
   private String firstName;

   @Column(name = "last_name", nullable = false, length = 100)
   private String lastName;

   @Column(name = "email", nullable = false, length = 100)
   private String email;

   @Column(name = "username", nullable = false, unique = true, length = 100)
   private String username;

   @Column(name = "password", nullable = false, length = 100)
   private String password;

   @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinTable(
           name = "users_roles",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "role_id")
   )

   private Set<Role> roles;

   @Transient
   private List<Long> roleIds;


   public User() {}

   public User(String username, String password, String firstName, String lastName, String email, Set<Role> roles) {
      this.username = username;
      this.password = password;
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.roles = roles;
   }


   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }


   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }


   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }


   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }


   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }


   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }


   public Set<Role> getRoles() {
      return roles;
   }

   public void setRoles(Set<Role> roles) {
      this.roles = roles;
   }


   public List<Long> getRoleIds() { return roleIds; }

   public void setRoleIds(List<Long> roleIds) { this.roleIds = roleIds; }


   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }


   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return roles;
   }


   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      User user = (User) o;
      return Objects.equals(id, user.id);
   }

   @Override
   public int hashCode() {
      return 31 * Objects.hash(id);
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder("User - ");
      sb.append("id: ").append(id).append("\n");
      sb.append("firstName: ").append(firstName).append("\n");
      sb.append("lastName: ").append(lastName).append("\n");
      sb.append("email: ").append(email).append("\n");
      sb.append("username: ").append(username).append("\n");
      return sb.toString();
   }

}