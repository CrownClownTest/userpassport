package com.graduation.userpassport.utils.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class ServletRequestUtils {
    private ServletRequestUtils() {
    }

    public static HttpServletRequest currentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }
        return null;
    }

    public static String getClientIp() {
        return getClientIp(currentRequest());
    }

    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String xForwardedFor = request.getHeader("X-Forwarded-For");
        String forwardedForIp = firstIpFromXForwardedFor(xForwardedFor);
        if (forwardedForIp != null) {
            return forwardedForIp;
        }

        String realIp = normalizeIpHeader(request.getHeader("X-Real-IP"));
        if (realIp != null) {
            return realIp;
        }

        String proxyClientIp = normalizeIpHeader(request.getHeader("Proxy-Client-IP"));
        if (proxyClientIp != null) {
            return proxyClientIp;
        }

        String wlProxyClientIp = normalizeIpHeader(request.getHeader("WL-Proxy-Client-IP"));
        if (wlProxyClientIp != null) {
            return wlProxyClientIp;
        }

        String httpClientIp = normalizeIpHeader(request.getHeader("HTTP_CLIENT_IP"));
        if (httpClientIp != null) {
            return httpClientIp;
        }

        String httpXForwardedFor = normalizeIpHeader(request.getHeader("HTTP_X_FORWARDED_FOR"));
        if (httpXForwardedFor != null) {
            return httpXForwardedFor;
        }

        String remoteAddr = request.getRemoteAddr();
        if (remoteAddr == null || remoteAddr.isBlank()) {
            return null;
        }
        return remoteAddr.trim();
    }

    public static String getUserAgent() {
        return getUserAgent(currentRequest());
    }

    public static String getUserAgent(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String ua = request.getHeader("User-Agent");
        if (ua == null) {
            return null;
        }
        String trimmed = ua.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        if (trimmed.length() <= 255) {
            return trimmed;
        }
        return trimmed.substring(0, 255);
    }

    private static String firstIpFromXForwardedFor(String xForwardedFor) {
        if (xForwardedFor == null || xForwardedFor.isBlank()) {
            return null;
        }

        int length = xForwardedFor.length();
        int start = 0;
        while (start < length) {
            int comma = xForwardedFor.indexOf(',', start);
            int end = comma < 0 ? length : comma;
            String candidate = normalizeIpHeader(xForwardedFor.substring(start, end));
            if (candidate != null) {
                return candidate;
            }
            if (comma < 0) {
                break;
            }
            start = comma + 1;
        }
        return null;
    }

    private static String normalizeIpHeader(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        if ("unknown".equalsIgnoreCase(trimmed)) {
            return null;
        }
        return trimmed;
    }
}
