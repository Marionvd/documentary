package com.mariodoumbanov.documentqa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements UserDetails {
    @Transient
    public static final String DEFAULT_ROLE_NAME = "ROLE_USER";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column(length = 60, unique = true, nullable = false)
    private String email;
    @Column(length = 90)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_authorities",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="authority_id")
    )
    private Set<Authority> authorities = new HashSet<>();
    @Column(nullable = false)
    private Date createdAt;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<Document> files = new HashSet<>();

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.createdAt = new Date(System.currentTimeMillis());
        this.authorities = new HashSet<>();
        this.files = new HashSet<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getName())).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
