package TestGestioneBiblioteca;

import it.unisa.c07.biblionet.filter.FilterConfig;
import it.unisa.c07.biblionet.filter.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import static org.junit.jupiter.api.Assertions.*;

public class FilterConfigTest {

    @Test
    public void testJwtFilter() {
        FilterConfig filterConfig = new FilterConfig();
        FilterRegistrationBean<JwtFilter> filterRegistrationBean = filterConfig.jwtFilter();

        assertNotNull(filterRegistrationBean);
        assertEquals(JwtFilter.class, filterRegistrationBean.getFilter().getClass());

        String[] urlPatterns = {
                "/biblioteca/inserimento-isbn",
                "/biblioteca/inserimento-archivio",
                "/biblioteca/inserimento-manuale",
                "/prenotazione-libri/conferma-prenotazione",
                "/prenotazione-libri/visualizza-richieste",
                "/prenotazione-libri/visualizza-prenotazioni"
        };
        assertArrayEquals(urlPatterns, filterRegistrationBean.getUrlPatterns().toArray());
    }
}