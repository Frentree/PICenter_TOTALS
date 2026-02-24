package com.org.iopts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Detection Domain Entity
 * Maps to PI_FIND table
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Detection {

    /** Primary key */
    private Long findId;

    /** Target ID (FK to pi_targets) */
    private Long targetId;

    /** Target name (denormalized for display) */
    private String targetName;

    /** Detected file path */
    private String filePath;

    /** Detected file name */
    private String fileName;

    /** Data type name (e.g., SSN, Credit Card) */
    private String datatypeName;

    /** Pattern name used for detection */
    private String patternName;

    /** Matched data content */
    private String matchData;

    /** Number of matches found */
    private Integer matchCount;

    /** Processing status (PENDING, PROCESSED, APPROVED, etc.) */
    private String processStatus;

    /** Processing type (TRUE_POSITIVE, FALSE_POSITIVE) */
    private String processType;

    /** User who processed the detection */
    private String processUser;

    /** Processing date/time */
    private LocalDateTime processDt;

    /** Detection date/time */
    private LocalDateTime findDt;

    /** Registration date/time */
    private LocalDateTime regDt;
}
