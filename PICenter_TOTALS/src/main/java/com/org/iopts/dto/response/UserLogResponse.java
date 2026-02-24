package com.org.iopts.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

/**
 * User Log Response DTO
 *
 * Returned by user activity log API endpoints.
 * Represents a single audit log entry for user actions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLogResponse {

    private String logNo;

    private String userNo;

    private String userId;

    private String userName;

    private String logType;

    private String logDetail;

    private String ipAddress;

    private LocalDateTime logDt;
}
