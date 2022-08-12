## Springsecurity

### 登录

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

> #### 以上是零碎知识点,loginServiceimpl才是梦开始的地方
我们从前端获取user传过来的user,其实也就是一个用户名和密码 然后我们进行账号密码校验(使用到了加密是加密数据库中的数据,防止曝光),账号密码通过后给出jwt(用户id的加密) 也是一种形式的加密,相当于用户的一卡通

```java

@Service
public class loginserviceimpl implements loginService {
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
//        进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        // 用户认证其实也就是校验账号面面是否匹配,框架自动调用UserDatailServiceImpl里面的实现
        Authentication authenticate = manager.authenticate(authenticationToken);
//        认证没通过 提示
        if (authenticate == null) {
            throw new RuntimeException("登录失败");
        }
//        通过给出随机jwt
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Long id = loginUser.getUser().getId();
        String jwt = JwtUtil.createJWT(id.toString());
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
//        把用户完整信息存入reds,uid作为key,实体作为value;
        redisCache.setCacheObject("login" + id, loginUser);
        return new ResponseResult(200, "登录成功", map);
    }
}
```

### 校验

定义JWT认证过滤器

* 获取token
* 解析token 获取userid
* 从redis获取用户信息
* 存入securitycontextholder
    * 这里必须要存入hodler中，因为我们过滤器不止一个,如果hodler中找不到你相关的认证信息,过滤器会默认将你拦截

---
不是很懂经历了什么,大体上好像配置了一个过滤器,然后再springconfig里面添加了过滤器,实现了

- 登录页面,不需要携带token,只需用户名和密码
- 其他页面需要在请求header里面携带token（userid的jwt加密）,可以访问,否则拦截

![](img/auth.png)
用户登录成功意味着 SecurityHoder中已经有用户认证相关信息了,用户发送其他请求(非登录),需要携带token 才能访问 用户注销功能就是 清除redis里面相关用户的缓存以及删除Sercurityhoder里面用户认证信息
> logout 重点删redis缓存信息,不需要参数,只需要header携带token告诉我删除哪一个,删除完以后即使携带token访问请求也不响应了
> 每次登陆token都会变
---
> 认证规则分配

```java
class A {
    public void test() {
// 对于登录接口 anonymous允许匿名访问(不携带token可以访问,携带不能访问)
                .antMatchers("/user/login").anonymous()
// 携不携带token都能访问
                .antMatchers("/hello").permitAll()
//.antMatchers("/testCors").hasAuthority("system:dept:list222")
// 除上面外的所有请求全部需要鉴权认证(访问需携带token)
                .anyRequest().authenticated();
    }
}

```

> ### 到此位置登陆登出操作已全部完成

### 权限系统

> 让对应用户看到对应的模块

- 开启权限系统`@EnableGlobalMethodSecurity(prePostEnabled = true)`
- 开启后可以在controller上分配权限了`@PreAuthorize("hasAuthority('test')")`
- JwtAuthenticationTokenFilter类,我再详细所以说
    - 本质是一个过滤器,首先看你有没有token(/login请求除外,这个是由我们在securityconfig里面配置的),没token直接滚,有token我从redis里面根据token查
      ,找出对应的loginuser实体,loginuser包含用户所有信息user实体,以及所有权限List集合
- 用户权限应该是我们在数据库中获取的,什么时候给呢?在用户密码验证成功后,我们包装Loginuser的时候给权限,也就是初始化一个Loginuser（UserdetailServiceImpl类）
- 之前说过在controller上可以规定拥有什么权限可以请求,什么角色可以请求(角色其实是权限的集和),几乎所有请求都由带token,securityconfig有配置不带 token的.两个概念别搞混了.
  所以我们模拟的时候要在请求头里面加上token



> 自定义处理失败

我们希望认证失败或者授权失败也能返回ResultJson,Hander文件夹下写异常处理
