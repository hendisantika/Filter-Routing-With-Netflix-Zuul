package com.hendisantika.gateway.filter;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
class LoginPreFilterTest {

    private final LoginPreFilter filter = new LoginPreFilter();

    @Test
    void passesRequestThroughUnchanged() {
        ServerRequest request = ServerRequest.create(
                new MockHttpServletRequest("GET", "/loginapp/login"), List.of());

        assertThat(filter.apply(request)).isSameAs(request);
    }

}
