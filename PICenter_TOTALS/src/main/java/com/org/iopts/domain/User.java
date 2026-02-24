package com.org.iopts.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User Entity - pi_user 테이블 매핑 (picenter DB)
 *
 * user_no: PK (숫자)
 * user_id: 로그인 ID (예: admin)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pi_user")
public class User {

    @Id
    @Column(name = "user_no", length = 50)
    private String userNo;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "password", length = 256)
    private String password;

    @Column(name = "user_name", length = 40)
    private String userName;

    @Column(name = "user_grade", length = 1)
    private String userGrade;

    @Column(name = "user_email", length = 200)
    private String userEmail;

    @Column(name = "user_phone", length = 50)
    private String userPhone;

    @Column(name = "user_ecrypted", length = 255)
    private String userEcrypted;

    @Column(name = "insa_code", length = 50)
    private String insaCode;

    @Column(name = "jikwee", length = 50)
    private String jikwee;

    @Column(name = "jikguk", length = 50)
    private String jikguk;

    @Column(name = "boss_name", length = 40)
    private String bossName;

    @Column(name = "sosok", length = 200)
    private String sosok;

    @Column(name = "access_ip", length = 300)
    private String accessIp;

    @Column(name = "acc_yn", length = 2)
    @Builder.Default
    private String accYn = "Y";

    @Column(name = "lock_status")
    @Builder.Default
    private Integer lockStatus = 0;

    @Column(name = "LOCK_DATE")
    private LocalDateTime lockDate;

    @Column(name = "FAILED_COUNT", length = 10)
    @Builder.Default
    private String failedCount = "0";

    @Column(name = "logindate")
    private LocalDateTime logindate;

    @Column(name = "regdate")
    private LocalDateTime regdate;

    @Column(name = "enddate")
    private LocalDateTime enddate;

    @Column(name = "startdate")
    private LocalDateTime startdate;

    @Column(name = "pwd_upt_dt")
    private LocalDateTime pwdUptDt;

    @Column(name = "pwd_reset_status")
    @Builder.Default
    private Integer pwdResetStatus = 0;

    @Column(name = "unlock_reason", columnDefinition = "TEXT")
    private String unlockReason;

    @Column(name = "lock_email", length = 50)
    private String lockEmail;

    @Column(name = "secret_key", length = 50)
    private String secretKey;
}
