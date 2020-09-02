package com.er706.backend;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

import com.fasterxml.classmate.TypeResolver;
import java.lang.reflect.WildcardType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile({"develop", "integration"})
public class SwaggerConfig {

  @Autowired
  private TypeResolver typeResolver;

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.er706"))
        .paths(PathSelectors.any())
        .build().alternateTypeRules( //自定义规则，如果遇到DeferredResult，则把泛型类转成json
            newRule(typeResolver.resolve(JsonResult.class,
                typeResolver.resolve(WildcardType.class)),
                typeResolver.resolve(WildcardType.class)));
  }
}