package com.org.iopts.group.vo;

import java.io.Serializable;

public class targetServiceUserVo implements Serializable {
	private int level;
	private String target_id;
	private String location_id;
	private int ap_no;
	private String name;
	private String insa_code;
	private String up_idx;
	private String connected;
	private String ip;
	private String id;
	
	public targetServiceUserVo() {
		
	}

    public targetServiceUserVo(int level, String target_id, String location_id, int ap_no, String name, String insa_code,
    		String up_idx, String connected, String ip, String id) {
       
        this.level = level;
        this.target_id = target_id;
        this.location_id = location_id;
        this.ap_no = ap_no;
        this.name = name;
        this.insa_code = insa_code;
        this.up_idx = up_idx;
        this.connected = connected;
        this.ip = ip;
        this.id = id;
    }

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getTarget_id() {
		return target_id;
	}

	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}

	public String getLocation_id() {
		return location_id;
	}

	public void setLocation_id(String location_id) {
		this.location_id = location_id;
	}

	public int getAp_no() {
		return ap_no;
	}

	public void setAp_no(int ap_no) {
		this.ap_no = ap_no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInsa_code() {
		return insa_code;
	}

	public void setInsa_code(String insa_code) {
		this.insa_code = insa_code;
	}

	public String getUp_idx() {
		return up_idx;
	}

	public void setUp_idx(String up_idx) {
		this.up_idx = up_idx;
	}

	public String getConnected() {
		return connected;
	}

	public void setConnected(String connected) {
		this.connected = connected;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "targetServiceUserVo [level=" + level + ", target_id=" + target_id + ", location_id=" + location_id
				+ ", ap_no=" + ap_no + ", name=" + name + ", insa_code=" + insa_code + ", up_idx=" + up_idx
				+ ", connected=" + connected + ", ip=" + ip + ", id=" + id + "]";
	}

}