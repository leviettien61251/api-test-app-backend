package com.example.apitestappbackend;

public enum ResponseCode {
    // SUCCESS
    SUCCESS("1000", "Request processed successfully"),

    // VALIDATION
    MISSING_PARAM("2001", "Missing required parameter"),
    INVALID_TYPE("2002", "Invalid parameter type"),
    INVALID_VALUE("2003", "Invalid parameter value"),
    METHOD_NOT_ALLOWED("2004", "Method not allowed"),
    INVALID_BODY("2005", "Request body invalid"),
    MISSING_BODY("2006", "Request body missing"),

    // AUTH
    TOKEN_INVALID("3001", "Token is invalid"),
    TOKEN_EXPIRED("3002", "Token expired"),
    UNAUTHENTICATED("3003", "User not authenticated"),
    OTP_INCORRECT("3004", "OTP incorrect"),
    OTP_EXPIRED("3005", "OTP expired"),
    USER_EXISTS("3006", "User already exists"),
    USER_NOT_FOUND("3007", "User not found"),
    PASSWORD_INCORRECT("3008", "Password incorrect"),
    USER_NOT_LOGGED_IN("3009", "User not logged in"),

    // AUTHORIZATION
    PERMISSION_DENIED("3101", "Permission denied"),
    ADMIN_REQUIRED("3102", "Admin role required"),

    // MAP
    FLOOR_NOT_FOUND("4001", "Floor not found"),
    NODE_NOT_FOUND("4002", "Node not found"),
    EDGE_NOT_FOUND("4003", "Edge not found"),
    INVALID_COORDINATE("4004", "Invalid coordinate"),
    BUILDING_NOT_FOUND("4005", "Building not found"),
    BUILDING_EXISTS("4006", "Building already exists"),

    // ROUTE
    INTERNAL_SERVER_ERROR("5000", "Internal Server Error"),
    INVALID_START("5001", "Invalid start location"),
    INVALID_DEST("5002", "Invalid destination"),
    PATH_NOT_FOUND("5003", "Path not found"),

    // FLOW
    INVALID_LOCATION_DATA("6001", "Invalid location data"),
    DENSITY_UNAVAILABLE("6002", "Density data unavailable"),

    // MEDICAL
    HIS_SERVICE_UNAVAILABLE("7001", "HIS service unavailable"),
    CLINICAL_TASK_NOT_FOUND("7002", "Clinical task not found"),

    // ASSET
    ASSET_NOT_FOUND("8001", "Asset not found"),
    ASSET_NOT_AVAILABLE("8002", "Asset not available"),

    // ENGINE
    ENGINE_UNAVAILABLE("9001", "Engine unavailable"),
    ENGINE_TIMEOUT("9002", "Engine timeout"),

    // SYSTEM
    DB_CONNECTION_FAILED("9901", "Database connection failed"),
    DB_QUERY_FAILED("9902", "Database query failed"),
    UNEXPECTED("9999", "Unexpected exception");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ResponseCode fromCode(String code) {
        for (ResponseCode rc : values()) {
            if (rc.code.equals(code)) {
                return rc;
            }
        }
        return null;
    }
}
