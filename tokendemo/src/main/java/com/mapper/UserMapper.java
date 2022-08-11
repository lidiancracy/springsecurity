package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.domain.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-08-11 15:22:37
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
