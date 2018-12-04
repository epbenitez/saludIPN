package com.becasipn.util.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Filtro que nos permite agregar encabezados para la definición de seguridad en el sistema
 * un header
 * @author Patricia Benitez
 */
public class SecurityFilter implements Filter {

    private FilterConfig filterConfig;

    public SecurityFilter() {
    }

    private void addCacheHeaders(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        HttpServletResponse sr = (HttpServletResponse) response;
        
        sr.setHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
        sr.setHeader("X-XSS-Protection", "1; mode=block");
//        sr.setHeader("Access-Control-Allow-Origin", "*");
        sr.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        sr.setHeader("X-Permitted-Cross-Domain-Policies", "none");
        sr.setHeader("X-Content-Type-Options", "nosniff");
//        sr.setHeader("Content-Security-Policy", "default-src 'self' 'unsafe-inline'; "
//				+ "style-src 'self' 'unsafe-inline' http://fonts.googleapis.com https://www.gstatic.com/charts/ ; "
//				+ "font-src 'self' 'unsafe-inline' http://fonts.gstatic.com ;"
//				+ "script-src 'self' 'unsafe-inline' https://www.gstatic.com ;"
//				+ "frame-src 'self' 'unsafe-inline' http://maps.google.com https://www.google.com ;");
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        addCacheHeaders(request, response);
        chain.doFilter(request, response);
    }

    /**
     * Return the filter configuration object for this filter.
     */
    private FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    private void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     *
     */
    @Override
    public void destroy() {
    }

    /**
     * Init method for this filter
     *
     */
    @Override
    public void init(FilterConfig filterConfig) {
        setFilterConfig(filterConfig);
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (getFilterConfig() == null) {
            return ("SecurityFilter()");
        }
        StringBuilder sb = new StringBuilder("SecurityFilter(");
        sb.append(getFilterConfig());
        sb.append(")");
        return (sb.toString());

    }

}
