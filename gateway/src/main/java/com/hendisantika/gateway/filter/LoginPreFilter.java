package com.hendisantika.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.function.Function;

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
public class LoginPreFilter implements Function<ServerRequest, ServerRequest> {

    private static final Logger logger = LoggerFactory.getLogger(LoginPreFilter.class);

    /**
     * "Pre" filter (formerly a Zuul {@code pre} filter): runs before the request is
     * forwarded to the downstream service and logs the incoming request.
     */
    @Override
    public ServerRequest apply(ServerRequest request) {
        logger.info("incoming request is captured by filter : {} - {}",
                request.method(), request.uri().getPath());
        return request;
    }

}
