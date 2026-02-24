package com.org.iopts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Location Entity - Maps to PI_LOCATIONS table
 * MyBatis only (no JPA annotations)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private Long locationId;
    private String locationName;
    private String locationPath;
    private String locationType;
    private Character useYn;
}
