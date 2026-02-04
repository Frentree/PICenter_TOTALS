package com.org.iopts.dto;

public class Pi_Custom_PatternVO {
	   private int pattern_idx;
	   private int pattern_cnt;
	   private String pattern_code;
	   private String pattern_rule;
	   private String pattern_en_name;
	   private String pattern_kr_name;
	   private String pattern_updated;
	   private String creuser_no;
	   private String color_code;
	   private int mask_cnt;
	   private String mask_type;
	   private String mask_chk;
	   private String regdate;

	   public int getPattern_idx() {
	       return pattern_idx;
	   }

	   public void setPattern_idx(int pattern_idx) {
	       this.pattern_idx = pattern_idx;
	   }

	   public int getPattern_cnt() {
	       return pattern_cnt;
	   }

	   public void setPattern_cnt(int pattern_cnt) {
	       this.pattern_cnt = pattern_cnt;
	   }

	   public String getPattern_code() {
	       return pattern_code;
	   }

	   public void setPattern_code(String pattern_code) {
	       this.pattern_code = pattern_code;
	   }

	   public String getPattern_rule() {
	       return pattern_rule;
	   }

	   public void setPattern_rule(String pattern_rule) {
	       this.pattern_rule = pattern_rule;
	   }

	   public String getPattern_en_name() {
	       return pattern_en_name;
	   }

	   public void setPattern_en_name(String pattern_en_name) {
	       this.pattern_en_name = pattern_en_name;
	   }

	   public String getPattern_kr_name() {
	       return pattern_kr_name;
	   }

	   public void setPattern_kr_name(String pattern_kr_name) {
	       this.pattern_kr_name = pattern_kr_name;
	   }

	   public String getPattern_updated() {
	       return pattern_updated;
	   }

	   public void setPattern_updated(String pattern_updated) {
	       this.pattern_updated = pattern_updated;
	   }

	   public String getCreuser_no() {
	       return creuser_no;
	   }

	   public void setCreuser_no(String creuser_no) {
	       this.creuser_no = creuser_no;
	   }

	   public String getColor_code() {
	       return color_code;
	   }

	   public void setColor_code(String color_code) {
	       this.color_code = color_code;
	   }

	   public int getMask_cnt() {
	       return mask_cnt;
	   }

	   public void setMask_cnt(int mask_cnt) {
	       this.mask_cnt = mask_cnt;
	   }

	   public String getMask_type() {
	       return mask_type;
	   }

	   public void setMask_type(String mask_type) {
	       this.mask_type = mask_type;
	   }

	   public String getMask_chk() {
	       return mask_chk;
	   }

	   public void setMask_chk(String mask_chk) {
	       this.mask_chk = mask_chk;
	   }

	   public String getRegdate() {
	       return regdate;
	   }

	   public void setRegdate(String regdate) {
	       this.regdate = regdate;
	   }

	   @Override
	   public String toString() {
	       return "PatternVo [pattern_idx=" + pattern_idx + ", pattern_cnt=" + pattern_cnt + ", pattern_code="
	               + pattern_code + ", pattern_rule=" + pattern_rule + ", pattern_en_name=" + pattern_en_name
	               + ", pattern_kr_name=" + pattern_kr_name + ", pattern_updated=" + pattern_updated + ", creuser_no="
	               + creuser_no + ", color_code=" + color_code + ", mask_cnt=" + mask_cnt + ", mask_type=" + mask_type
	               + ", mask_chk=" + mask_chk + ", regdate=" + regdate + "]";
	   }
}
