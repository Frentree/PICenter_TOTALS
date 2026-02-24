package com.org.iopts.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Detection Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetectionResponse {

    /** Detection ID (hash_id from pi_find, varchar) */
    private String findId;

    /** Target ID */
    private String targetId;

    /** Target name */
    private String targetName;

    /** Detected file path */
    private String filePath;

    /** Detected file name */
    private String fileName;

    /** Data type name */
    private String datatypeName;

    /** Pattern name */
    private String patternName;

    /** Matched data (masked) */
    private String matchData;

    /** Number of matches */
    private Integer matchCount;

    /** Processing status */
    private String processStatus;

    /** Processing type */
    private String processType;

    /** User who processed */
    private String processUser;

    /** Processing date/time */
    private LocalDateTime processDt;

    /** Detection date/time */
    private LocalDateTime findDt;

    /** Registration date/time */
    private LocalDateTime regDt;

    /** Display label for process status */
    private String processStatusLabel;

    /** Display label for data type */
    private String datatypeLabel;
}
