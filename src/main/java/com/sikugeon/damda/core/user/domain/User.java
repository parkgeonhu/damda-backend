package com.sikugeon.damda.core.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    @GeneratedValue
    @Id
    private Long id;
    private String username;
    private String password;
    private String authority;
    private String bucketName;
    private String accessKeyId;
    private String secretAccessKey;

    public User(
            String username,
            String password,
            String authority
    ) {
        this.username = username;
        this.password = password;
        this.authority = authority;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> authority);
    }

    public Boolean isAdmin() {
        return authority.equals("ROLE_ADMIN");
    }

    public void updateAWSKey(Map<String, String> awsKey){
        this.accessKeyId = awsKey.get("accessKeyId");
        this.secretAccessKey = awsKey.get("secretAccessKey");
    }

    public void updateBucketName(String bucketName){
        this.bucketName = bucketName;
    }

    public Map<String, String> getAwsKey(){
        Map<String, String> awsKey = new HashMap<>();
        awsKey.put("accessKeyId", accessKeyId);
        awsKey.put("secretAccessKey", secretAccessKey);

        return awsKey;
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
