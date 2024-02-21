package com.isep.acme.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@RedisHash
@Getter
@Setter
public class UserRedis implements UserDetails {


    private static final long serialVersionUID = 1L;

    @Indexed
    @org.springframework.data.annotation.Id
    private Long userId;

    @Indexed
    @Column(unique = true)
    @Email
    private String username;

    @Indexed
    private String password;

    @Indexed
    private String fullName;

    @Indexed
    @ElementCollection
    private Set<Role> authorities = new HashSet<>();

    @Indexed
    @Column(nullable = false, unique = true)
    private String nif;

    @Indexed
    @Column(nullable = false)
    private String morada;

/*    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> review = new ArrayList<Review>(); */

    protected UserRedis() {}

    public UserRedis(final String username, final String password){
        this.username = username;
        this.password = password;
    }


    public UserRedis(final String username, final String password, final String fullName, final String nif, final String morada) {
        long leftLimit = 1L;
        long rightLimit = 100000L;
        this.userId= leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        setNif(nif);
        this.morada = morada;
    }

    public void addAuthority(Role r) {
        authorities.add(r);
    }

    public void setNif(String nif) {
        if(nif.length() != 9) {
            throw new IllegalArgumentException("NIF must be 9 characters.");
        }
        this.nif = nif;
    }


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
}
