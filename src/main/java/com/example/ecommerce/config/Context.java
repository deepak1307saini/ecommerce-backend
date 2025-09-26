package com.example.ecommerce.config;

public class Context {
    private static final ThreadLocal<Context> contextHolder = new ThreadLocal<>();

    private final Long userId; // Database user ID (e.g., 2)
    private final String keycloakId; // JWT sub (e.g., 9586d656-74f5-45fd-8acb-bb7ea1b9f0d8)
    private final String userRole; // Primary role (e.g., USER)

    private final String userName;
    private final Long tenantId;

    private Context(Long userId, String userName, String keycloakId, String userRole, Long tenantId) {
        this.userId = userId;
        this.keycloakId = keycloakId;
        this.userRole = userRole;
        this.userName = userName;
        this.tenantId = tenantId;
    }

    public static void setContext(Long userId, String userName, String keycloakId, String userRole, Long tenantId) {
        contextHolder.set(new Context(userId, userName, keycloakId, userRole, tenantId));
    }

    public static void clear() {
        contextHolder.remove();
    }

    public static Long getUserId() {
        Context context = contextHolder.get();
        if (context == null) {
            throw new IllegalStateException("Context not initialized");
        }
        return context.userId;
    }

    public static String getKeycloakId() {
        Context context = contextHolder.get();
        if (context == null) {
            throw new IllegalStateException("Context not initialized");
        }
        return context.keycloakId;
    }

    public static String getUserRole() {
        Context context = contextHolder.get();
        if (context == null) {
            throw new IllegalStateException("Context not initialized");
        }
        return context.userRole;
    }

    public static String getUserName() {
        Context context = contextHolder.get();
        if (context == null) {
            throw new IllegalStateException("Context not initialized");
        }
        return context.userName;
    }

    public static Long getTenantId() {
        Context context = contextHolder.get();
        if (context == null) {
            throw new IllegalStateException("Context not initialized");
        }
        return context.tenantId;
    }
}