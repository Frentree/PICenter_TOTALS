package com.org.iopts.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

/**
 * User Response DTO
 *
 * Returned by user management API endpoints.
 * Fields are selectively included (null fields omitted).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private String userNo;

    private String userId;

    private String userName;

    private String userGrade;

    private String teamCode;

    private String teamName;

    private String email;

    private String phone;

    private Character useYn;

    private Character lockYn;

    private LocalDateTime lockDt;

    private Integer loginFailCnt;

    private LocalDateTime lastLoginDt;

    private LocalDateTime pwdChangeDt;

    private LocalDateTime regDt;

    private String regUser;

    private LocalDateTime modDt;

    private String modUser;
}
