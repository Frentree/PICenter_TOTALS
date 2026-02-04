package com.org.iopts.detection.vo;

import java.io.Serializable;
import java.util.Arrays;

public class patternVo implements Serializable {
	
	private String pattern_idx;
	private String pattern_code;
	private String pattern_kr_name;
	private int pattern_type;
	private int pattern_cnt = 0;
	private String pattern_data;
	private String idx;
	private String color_code;
	
	private int mask_cnt;
	private String mask_type;
	private String mask_chk;
	
	private int matchLimitCnt = 0;
	
	
	public patternVo() {
		// TODO Auto-generated constructor stub
	}

	public patternVo(String pattern_code, int pattern_cnt) {
        this.pattern_code = pattern_code;
        this.pattern_cnt = pattern_cnt;
    }

	public patternVo(String pattern_idx, String pattern_code, String pattern_kr_name, int pattern_type, int pattern_cnt,
			String pattern_data, String idx, String color_code, int mask_cnt, String mask_type, String mask_chk,
			int matchLimitCnt) {
		super();
		this.pattern_idx = pattern_idx;
		this.pattern_code = pattern_code;
		this.pattern_kr_name = pattern_kr_name;
		this.pattern_type = pattern_type;
		this.pattern_cnt = pattern_cnt;
		this.pattern_data = pattern_data;
		this.idx = idx;
		this.color_code = color_code;
		this.mask_cnt = mask_cnt;
		this.mask_type = mask_type;
		this.mask_chk = mask_chk;
		this.matchLimitCnt = matchLimitCnt;
	}

	public String getPattern_idx() {
		return pattern_idx;
	}

	public void setPattern_idx(String pattern_idx) {
		this.pattern_idx = pattern_idx;
	}

	public String getPattern_code() {
		return pattern_code;
	}

	public void setPattern_code(String pattern_code) {
		this.pattern_code = pattern_code;
	}

	public String getPattern_kr_name() {
		return pattern_kr_name;
	}

	public void setPattern_kr_name(String pattern_kr_name) {
		this.pattern_kr_name = pattern_kr_name;
	}

	public int getPattern_type() {
		return pattern_type;
	}

	public void setPattern_type(int pattern_type) {
		this.pattern_type = pattern_type;
	}

	public int getPattern_cnt() {
		return pattern_cnt;
	}

	public void setPattern_cnt(int pattern_cnt) {
		this.pattern_cnt = pattern_cnt;
	}

	public String getPattern_data() {
		return pattern_data;
	}

	public void setPattern_data(String pattern_data) {
		this.pattern_data = pattern_data;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
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

	public int getMatchLimitCnt() {
		return matchLimitCnt;
	}

	public void setMatchLimitCnt(int matchLimitCnt) {
		this.matchLimitCnt = matchLimitCnt;
	}

	@Override
	public String toString() {
		return "patternVo [pattern_idx=" + pattern_idx + ", pattern_code=" + pattern_code + ", pattern_kr_name="
				+ pattern_kr_name + ", pattern_type=" + pattern_type + ", pattern_cnt=" + pattern_cnt
				+ ", pattern_data=" + pattern_data + ", idx=" + idx + ", color_code=" + color_code + ", mask_cnt="
				+ mask_cnt + ", mask_type=" + mask_type + ", mask_chk=" + mask_chk + ", matchLimitCnt=" + matchLimitCnt
				+ "]";
	}
	
}