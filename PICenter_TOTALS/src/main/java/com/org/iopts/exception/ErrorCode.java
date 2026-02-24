package com.org.iopts.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Error Code Enum
 */
@Getter
public enum ErrorCode {

    // Common Errors
    INVALID_PARAMETER("C001", "Invalid parameter", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("C002", "Unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("C003", "Forbidden", HttpStatus.FORBIDDEN),
    NOT_FOUND("C004", "Resource not found", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("C005", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    METHOD_NOT_ALLOWED("C006", "Method not allowed", HttpStatus.METHOD_NOT_ALLOWED),

    // Authentication Errors
    INVALID_CREDENTIALS("A001", "Invalid credentials", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("A002", "Token expired", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID("A003", "Invalid token", HttpStatus.UNAUTHORIZED),
    USER_LOCKED("A004", "User account is locked", HttpStatus.FORBIDDEN),
    USER_DISABLED("A005", "User account is disabled", HttpStatus.FORBIDDEN),
    PASSWORD_EXPIRED("A006", "Password expired", HttpStatus.FORBIDDEN),

    // User Errors
    USER_NOT_FOUND("U001", "User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("U002", "User already exists", HttpStatus.CONFLICT),
    DUPLICATE_USERNAME("U003", "Username already exists", HttpStatus.CONFLICT),
    INVALID_PASSWORD("U004", "Invalid password", HttpStatus.BAD_REQUEST),

    // Target/Scan Errors
    TARGET_NOT_FOUND("T001", "Target not found", HttpStatus.NOT_FOUND),
    SCAN_FAILED("T002", "Scan failed", HttpStatus.INTERNAL_SERVER_ERROR),
    AGENT_NOT_FOUND("T003", "Agent not found", HttpStatus.NOT_FOUND),

    // Detection Errors
    DETECTION_NOT_FOUND("D001", "Detection not found", HttpStatus.NOT_FOUND),
    PATTERN_NOT_FOUND("D002", "Pattern not found", HttpStatus.NOT_FOUND),
    APPROVAL_NOT_FOUND("D003", "Approval not found", HttpStatus.NOT_FOUND),
    INVALID_APPROVAL_STATUS("D004", "Invalid approval status", HttpStatus.BAD_REQUEST),
    PATH_EXCEPTION_NOT_FOUND("D005", "Path exception not found", HttpStatus.NOT_FOUND),

    // Notice Errors
    NOTICE_NOT_FOUND("N001", "Notice not found", HttpStatus.NOT_FOUND),

    // Group Errors
    GROUP_NOT_FOUND("G001", "Group not found", HttpStatus.NOT_FOUND),

    // Download Errors
    DOWNLOAD_NOT_FOUND("DL001", "Download not found", HttpStatus.NOT_FOUND),
    FILE_READ_FAILED("DL002", "File read failed", HttpStatus.INTERNAL_SERVER_ERROR),

    // FAQ Errors
    FAQ_NOT_FOUND("FQ001", "FAQ not found", HttpStatus.NOT_FOUND),

    // Settings Errors
    SETTINGS_SAVE_FAILED("S001", "Failed to save settings", HttpStatus.INTERNAL_SERVER_ERROR),

    // File Errors
    FILE_NOT_FOUND("F001", "File not found", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED("F002", "File upload failed", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_FAILED("F005", "File delete failed", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_FILE_TYPE("F003", "Invalid file type", HttpStatus.BAD_REQUEST),
    FILE_SIZE_EXCEEDED("F004", "File size exceeded", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
