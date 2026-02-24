package com.org.iopts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datatype Entity - Maps to PI_DATATYPES table
 * MyBatis only (no JPA annotations)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Datatype {

    private Long datatypeId;
    private String datatypeName;
    private String datatypePattern;
    private String description;
    private Character useYn;
}
