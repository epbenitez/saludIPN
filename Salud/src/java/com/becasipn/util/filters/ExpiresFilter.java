package com.becasipn.util.filters;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The expires filter adds the expires HTTP header based on the deployment
 * policy. Many sites have a fixed deployment schedule where deployments take
 * place based on timed regular intervals. This filter adds the expires header
 * of the next possible deployment time, to support browser caching.
 *
 * @author Chris Webster
 */
public class ExpiresFilter implements Filter {

    private static final String DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
    private static SimpleDateFormat sdf;
    private FilterConfig filterConfig;

    public ExpiresFilter() {
        sdf = new SimpleDateFormat(DATE_PATTERN);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private void addCacheHeaders(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        String requestURL = ((HttpServletRequest) request).getRequestURL().toString();
        HttpServletResponse sr = (HttpServletResponse) response;
        Calendar calendar = Calendar.getInstance();
        if (requestURL.contains("/resources/fonts/")) {
            calendar.add(Calendar.YEAR, 1);
            sr.setHeader("Expires", sdf.format(calendar.getTime()));
            sr.setHeader("Content-Type", "application/x-font-woff");
        } else if (requestURL.contains("/vendors/")) {
            calendar.add(Calendar.YEAR, 1);
            sr.setHeader("Expires", sdf.format(calendar.getTime()));
            if(requestURL.contains(".json")){
                sr.setHeader("Content-Type", " application/json");
                response.setCharacterEncoding("UTF-8");
            }
        } else if (requestURL.contains("/resources/")) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            sr.setHeader("Expires", sdf.format(calendar.getTime()));
        }
        sr.setHeader("Vary", "Accept-Encoding");
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
            return ("ExpiresFilter()");
        }
        StringBuilder sb = new StringBuilder("ExpiresFilter(");
        sb.append(getFilterConfig());
        sb.append(")");
        return (sb.toString());

    }

}
