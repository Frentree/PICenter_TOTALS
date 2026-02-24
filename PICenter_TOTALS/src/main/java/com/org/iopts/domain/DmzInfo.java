package com.org.iopts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DmzInfo Entity - Maps to pi_dmz table
 * Columns: idx(PK auto int), dmz_ip(varchar50), memo(text)
 * MyBatis only (no JPA annotations)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DmzInfo {

    private Long dmzId;
    private String dmzIp;
    private String memo;
}
