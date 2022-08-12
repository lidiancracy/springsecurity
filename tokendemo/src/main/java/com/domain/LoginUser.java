package com.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {
    private User user;
    private List<String> permissions;

    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    /**
     * @author: 83799
     * @date: 2022/8/12 15:19
     * @description: redis忽略该成员的序列化
     */
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities!=null){
            return authorities;
        }
        //把List<String> permissions封装成 SimpleGrantedAuthority
        List<SimpleGrantedAuthority> collect = permissions.stream()
                .distinct()
                .map(new Function<String, SimpleGrantedAuthority>() {
                    @Override
                    public SimpleGrantedAuthority apply(String s) {
                        return new SimpleGrantedAuthority(s);
                    }
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
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
