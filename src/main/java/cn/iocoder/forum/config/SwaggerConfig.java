package cn.iocoder.forum.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc OpenAPI 3 配置类 (兼容Spring Boot 3.x)
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI 3 配置
     * @return OpenAPI配置对象
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("论坛系统API")
                        .description("论坛系统前后端分离API测试")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Wilbur Lee")
                                .url("https://edu.bitejiuyeke.com")
                                .email("lwb980210@163.com")));
    }
}
