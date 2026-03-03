package com.org.iopts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Path Exception Domain Entity
 * Maps to PI_PATH_EXCEPTION table
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PathException {

    /** Primary key */
    private Long exceptionId;

    /** Exception path pattern */
    private String exceptionPath;

    /** Exception type (DIRECTORY, FILE, PATTERN) */
    private String exceptionType;

    /** Description of the exception */
    private String description;

    /** Target ID (FK to pi_targets) */
    private String targetId;

    /** Use flag (Y/N) */
    private Character useYn;

    /** Registration date/time */
    private LocalDateTime regDt;

    /** Registration user */
    private String regUser;

    /** Modification date/time */
    private LocalDateTime modDt;
}
