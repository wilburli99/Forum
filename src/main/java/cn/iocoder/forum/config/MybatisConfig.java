package cn.iocoder.forum.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("cn.iocoder.forum.dao")
public class MybatisConfig {
}
