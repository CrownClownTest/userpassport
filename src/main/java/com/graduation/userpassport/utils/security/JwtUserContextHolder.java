package com.graduation.userpassport.utils.security;

public class JwtUserContextHolder {
    private static final ThreadLocal<JwtUserContext> CONTEXT = new ThreadLocal<>();

    public static void set(JwtUserContext ctx) {
        CONTEXT.set(ctx);
    }

    public static JwtUserContext get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
