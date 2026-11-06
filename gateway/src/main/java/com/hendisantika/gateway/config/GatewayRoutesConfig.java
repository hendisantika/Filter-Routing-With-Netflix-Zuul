package com.hendisantika.gateway.config;

import com.hendisantika.gateway.filter.LoginPreFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.stripPrefix;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;

/**
 * Created by IntelliJ IDEA.
 * Project : gateway
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Link : s.id/hendisantika
 * Date: 06/11/26
 * Time: 07.01
 */
@Configuration(proxyBeanMethods = false)
public class GatewayRoutesConfig {

    @Bean
    public LoginPreFilter loginPreFilter() {
        return new LoginPreFilter();
    }

    /**
     * Routes {@code /loginapp/**} to the login microservice, e.g.
     * {@code http://localhost:8080/loginapp/login -> http://localhost:1907/login}.
     */
    @Bean
    public RouterFunction<ServerResponse> loginAppRoute(LoginPreFilter loginPreFilter,
                                                        @Value("${gateway.routes.loginapp.url}") String loginAppUrl) {
        return route("loginapp")
                .route(path("/loginapp/**"), http())
                .before(loginPreFilter)
                .before(uri(loginAppUrl))
                .before(stripPrefix(1))
                .build();
    }

}
