package com.org.iopts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Location Response DTO
 * Maps to pi_locations: location_id(PK varchar), path, protocol, description, LOCATION_USE
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponse {

    private String locationId;
    private String locationName;
    private String locationPath;
    private String locationType;
    private String useYn;
}
