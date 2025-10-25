package com.sarahemmati.wallet.infra;


import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestContextLoggingFilter extends OncePerRequestFilter {

    private static final String HDR_REQUEST_ID = "X-Request-Id";
    private static final String HDR_IDEMPOTENCY = "Idempotency-Key";

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws java.io.IOException, jakarta.servlet.ServletException {

        String requestId = headerOrGen(req, HDR_REQUEST_ID);
        String idemKey   = req.getHeader(HDR_IDEMPOTENCY);
        String user      = (req.getUserPrincipal() != null) ? req.getUserPrincipal().getName() : "-";

        MDC.put("requestId", requestId);
        MDC.put("idemKey", idemKey != null ? idemKey : "-");
        MDC.put("user", user);

        long t0 = System.currentTimeMillis();
        try {
            res.setHeader(HDR_REQUEST_ID, requestId);
            chain.doFilter(req, res);
        } finally {
            long took = System.currentTimeMillis() - t0;
            org.slf4j.LoggerFactory.getLogger(getClass())
                    .info("{} {} -> {} ({} ms)",
                            req.getMethod(), req.getRequestURI(), res.getStatus(), took);
            MDC.clear();
        }
    }

    private String headerOrGen(HttpServletRequest req, String name) {
        String v = req.getHeader(name);
        return (v == null || v.isBlank()) ? UUID.randomUUID().toString() : v;
    }
}
