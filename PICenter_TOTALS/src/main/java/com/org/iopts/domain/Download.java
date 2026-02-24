package com.org.iopts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Download Entity - Maps to PI_DOWNLOAD table
 * MyBatis only (no JPA annotations)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Download {

    private Long downloadId;
    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private String description;
    private Integer downloadCount;
    private Character useYn;
    private LocalDateTime regDt;
    private String regUser;
}
