package com.example.projecttest.models;
import com.example.projecttest.models.Authority;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    @Size(min = 2, message = "El correo debe tener mínimo 2 caracteres")
    @Size(max = 150, message = "El correo debe tener máximo 150 caracteres")
    @NotBlank(message = "El correo no puede estar vacío")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "phone")
    @Size(min = 10, max = 10, message = "El teléfono debe tener mínimo 10 caracteres")
    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "\\d+", message = "Este campo solo puede contener números")
    private String phone;

    @Column(name = "name", length = 150, nullable = false)
    @Size(min = 2, message = "El nombre debe tener mínimo 2 caracteres")
    @Size(max = 150, message = "El nombre debe tener máximo 150 caracteres")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @Column(name = "lastname", length = 150, nullable = false)
    @Size(min = 2, message = "El primer apellido debe tener mínimo 2 caracteres")
    @Size(max = 150, message = "El primer apellido debe tener máximo 150 caracteres")
    @NotBlank(message = "El primer apellido no puede estar vacío")
    private String lastname;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_authorities",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id")})
    private Set<Authority> authorities = new HashSet<>();

    public Users() {
        this.enabled = true;
    }

    public Users(Long id, String username, String password, boolean enabled, Set<Authority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public Users(Long id, String username, String password, boolean enabled, String phone, String name, String lastname, Set<Authority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.phone = phone;
        this.name = name;
        this.lastname = lastname;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public void addRole(Authority authority) {
        if (authorities == null) {
            authorities = new HashSet<Authority>();
        }
        authorities.add(authority);
    }
}