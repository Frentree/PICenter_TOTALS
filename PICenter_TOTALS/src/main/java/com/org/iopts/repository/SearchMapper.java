package com.org.iopts.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Search MyBatis Mapper
 *
 * Provides SQL operations for search registration and result management.
 * Namespace: com.org.iopts.repository.SearchMapper
 */
@Mapper
public interface SearchMapper {

    /**
     * Select paginated search registration list
     */
    List<Map<String, Object>> selectRegistrationList(Map<String, Object> params);

    /**
     * Count total search registrations matching filters
     */
    long countRegistrationList(Map<String, Object> params);

    /**
     * Insert a new search registration
     */
    int insertRegistration(Map<String, Object> params);

    /**
     * Delete a search registration
     */
    int deleteRegistration(@Param("registId") Long registId);

    /**
     * Select paginated search result list
     */
    List<Map<String, Object>> selectResultList(Map<String, Object> params);

    /**
     * Count total search results matching filters
     */
    long countResultList(Map<String, Object> params);

    /**
     * Select search result detail
     */
    Map<String, Object> selectResultDetail(@Param("resultId") Long resultId);

    /**
     * Select search status summary
     */
    Map<String, Object> selectSearchStatus();

    /**
     * Select ap_no for a given target_id
     */
    String selectApNoByTargetId(@Param("targetId") String targetId);

    /**
     * Select data type (profile) list
     */
    List<Map<String, Object>> selectDataTypeList(Map<String, Object> params);

    /**
     * Get max DATATYPE_ID for auto-generation
     */
    String selectMaxDatatypeId();

    /**
     * Get max STD_ID for auto-generation
     */
    String selectMaxStdId();

    /**
     * Insert a new data type (profile)
     */
    int insertDataType(Map<String, Object> params);

    /**
     * Update an existing data type (profile)
     */
    int updateDataType(Map<String, Object> params);

    /**
     * Soft-delete a data type (profile)
     */
    int deleteDataType(@Param("stdId") String stdId);

    /**
     * Check if data type is used in scan policy
     */
    int countPolicyByDataType(@Param("stdId") String stdId);
}
