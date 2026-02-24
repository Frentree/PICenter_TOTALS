package com.org.iopts.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Path Exception Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PathExceptionResponse {

    /** Exception ID */
    private Long exceptionId;

    /** Exception path pattern */
    private String exceptionPath;

    /** Exception type */
    private String exceptionType;

    /** Description */
    private String description;

    /** Target ID */
    private Long targetId;

    /** Use flag */
    private Character useYn;

    /** Registration date/time */
    private LocalDateTime regDt;

    /** Registration user */
    private String regUser;

    /** Modification date/time */
    private LocalDateTime modDt;

    /** Target name (joined from pi_targets) */
    private String targetName;
}
