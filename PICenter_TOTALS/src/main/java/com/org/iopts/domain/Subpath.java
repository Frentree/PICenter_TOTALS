package com.org.iopts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Subpath Domain Entity
 * Maps to PI_SUBPATH table
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subpath {

    /** Primary key (node_id from pi_subpath) */
    private String subpathId;

    /** Target ID (FK to pi_targets, varchar) */
    private String targetId;

    /** File path */
    private String filePath;

    /** File name */
    private String fileName;

    /** File size in bytes */
    private Long fileSize;

    /** File date/time */
    private LocalDateTime fileDt;
}
