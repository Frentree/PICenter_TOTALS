package com.org.iopts.dto;

import java.util.Date;

public class Pi_RemediationVO {
    
    // PI_REMEDIATION_DETAIL 필드
    private Long detail_id;
    private String target_id;
    private String hash_id;
    private Integer ap_no;
    private String fid;
    private String original_path;    // 원본 경로
    private String new_path;         // 변경 경로 (이동 후 경로)
    private String status;
    private String error_message;
    private String user_no;          // 처리자
    private String action;           // 조치 유형 (quarantine, delete, encrypt)
    private String password;         // 암호화 비밀번호 (encrypt용)
    private String reason;           // 조치 사유
    private Date request_date;       // 요청 시간
    private Date process_date;       // 처리 시간
    private Date regdate;            // 등록 시간
    
    // 조인용 추가 필드
    private String user_name;        // 처리자명
    private String host_name;        // 대상 호스트명
    
    
    public Long getDetail_id() {
        return detail_id;
    }
    public void setDetail_id(Long detail_id) {
        this.detail_id = detail_id;
    }
    public String getTarget_id() {
        return target_id;
    }
    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }
    public String getHash_id() {
        return hash_id;
    }
    public void setHash_id(String hash_id) {
        this.hash_id = hash_id;
    }
    public Integer getAp_no() {
        return ap_no;
    }
    public void setAp_no(Integer ap_no) {
        this.ap_no = ap_no;
    }
    public String getFid() {
        return fid;
    }
    public void setFid(String fid) {
        this.fid = fid;
    }
    public String getOriginal_path() {
        return original_path;
    }
    public void setOriginal_path(String original_path) {
        this.original_path = original_path;
    }
    public String getNew_path() {
        return new_path;
    }
    public void setNew_path(String new_path) {
        this.new_path = new_path;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getError_message() {
        return error_message;
    }
    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
    public String getUser_no() {
        return user_no;
    }
    public void setUser_no(String user_no) {
        this.user_no = user_no;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public Date getRequest_date() {
        return request_date;
    }
    public void setRequest_date(Date request_date) {
        this.request_date = request_date;
    }
    public Date getProcess_date() {
        return process_date;
    }
    public void setProcess_date(Date process_date) {
        this.process_date = process_date;
    }
    public Date getRegdate() {
        return regdate;
    }
    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }
    
    // 조인용 필드
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getHost_name() {
        return host_name;
    }
    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }
    
    @Override
    public String toString() {
        return "Pi_RemediationVO [" +
                "detail_id=" + detail_id +
                ", target_id=" + target_id +
                ", hash_id=" + hash_id +
                ", ap_no=" + ap_no +
                ", action=" + action +
                ", status=" + status +
                ", original_path=" + original_path +
                ", new_path=" + new_path +
                ", user_no=" + user_no +
                ", user_name=" + user_name +
                ", host_name=" + host_name +
                ", request_date=" + request_date +
                ", process_date=" + process_date +
                ", regdate=" + regdate +
                "]";
    }
}