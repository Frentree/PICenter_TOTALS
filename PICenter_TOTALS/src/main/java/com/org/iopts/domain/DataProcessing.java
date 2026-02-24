package com.org.iopts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Processing Domain Entity
 * Maps to PI_DATA_PROCESSING table
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataProcessing {

    /** Primary key */
    private Long processId;

    /** Detection ID (FK to pi_find, hash_id varchar) */
    private String findId;

    /** Processing type (TRUE_POSITIVE, FALSE_POSITIVE) */
    private String processType;

    /** Processing result */
    private String processResult;

    /** User who processed */
    private String processUser;

    /** Processing comment */
    private String processComment;

    /** Processing date/time */
    private LocalDateTime processDt;
}
