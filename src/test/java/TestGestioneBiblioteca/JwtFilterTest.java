package TestGestioneBiblioteca;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import it.unisa.c07.biblionet.filter.JwtFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

public class JwtFilterTest {

    @Test
    public void testDoFilter() throws ServletException, IOException {
        JwtFilter jwtFilter = new JwtFilter();
        ReflectionTestUtils.setField(jwtFilter, "secret", "pretzel");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        jwtFilter.doFilter(request, response, filterChain);

        Claims claims = (Claims) request.getAttribute("claims");
        assertNotNull(claims);
        assertEquals("token", claims.getAudience());

        assertEquals("id", request.getAttribute("blog"));

        verify(filterChain).doFilter(request, response);
    }
}