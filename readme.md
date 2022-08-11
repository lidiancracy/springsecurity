## Springsecurity

> 根据username数据库查询用户实体

```java
//实现接口里面的public UserDetails loadUserByUsername(String username)方法
implements UserDetailsService{}
```

可以看出返回对象是一个UserDetails,它其实是一个接口,所以我们需要实现它然后返回其实现类

```java
//本质上还是返回用户对象
public class LoginUser implements UserDetails {
    private User user;
}
```

> 加密 我们用什么加密算法直接在config里面配置

```java

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * @Author lidian
     * @Date 2022/8/11
     * @Description Bcrypt就是一个用于密码加密的工具, 可以对一段文本进行加密和解密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```
