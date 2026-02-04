package com.org.iopts.exception.vo;

import java.util.HashMap;

public class detection {
	
	private int CHK;
	private int CHK_C;
	private int CHKOLD;
	private String SUBFILE;
	private String HOST_NAME;
	private String PATH;
	private String ACCOUNT;
	private String OWNER;
	private String APPROVAL_STATUS_PRINT_NAME;
	private String CREATE_DT;
	private String MODIFIED;
	private String APPROVAL_DT;
	private String TYPE;
	private String TYPE_DATA;
	private String TARGET_ID;
	private int AP_NO;
	private String HASH_ID;
	private String APPROVAL_STATUS;
	private String PROCESSING_GROUP_ID;
	private String FILENAME;
	private String NOTEPAD;
	private int LEVEL;
	private String FID;
	
	
	public detection() {
		// TODO Auto-generated constructor stub
	}

	
//	public detection() {
//		
//	}
	

	public detection(int cHK, int cHK_C, int cHKOLD, String sUBFILE, String hOST_NAME, String pATH, String aCCOUNT,
			String oWNER, String aPPROVAL_STATUS_PRINT_NAME, String cREATE_DT, String mODIFIED, String aPPROVAL_DT,
			String tYPE, String tYPE_DATA, String tARGET_ID, int aP_NO, String hASH_ID, String aPPROVAL_STATUS,
			String pROCESSING_GROUP_ID, String fILENAME, String nOTEPAD, int lEVEL, String fID) {
		super();
		CHK = cHK;
		CHK_C = cHK_C;
		CHKOLD = cHKOLD;
		SUBFILE = sUBFILE;
		HOST_NAME = hOST_NAME;
		PATH = pATH;
		ACCOUNT = aCCOUNT;
		OWNER = oWNER;
		APPROVAL_STATUS_PRINT_NAME = aPPROVAL_STATUS_PRINT_NAME;
		CREATE_DT = cREATE_DT;
		MODIFIED = mODIFIED;
		APPROVAL_DT = aPPROVAL_DT;
		TYPE = tYPE;
		TYPE_DATA = tYPE_DATA;
		TARGET_ID = tARGET_ID;
		AP_NO = aP_NO;
		HASH_ID = hASH_ID;
		APPROVAL_STATUS = aPPROVAL_STATUS;
		PROCESSING_GROUP_ID = pROCESSING_GROUP_ID;
		FILENAME = fILENAME;
		NOTEPAD = nOTEPAD;
		LEVEL = lEVEL;
		FID = fID;
	}


	public detection(HashMap<String, Object> findMap) {
		CHK = (int) findMap.get("CHK");
		CHK_C = (int) findMap.get("CHK_C");
		CHKOLD = (int) findMap.get("CHKOLD");
		SUBFILE = (String) findMap.get("SUBFILE");
		HOST_NAME = (String) findMap.get("HOST_NAME");
		PATH = (String) findMap.get("PATH");
		ACCOUNT = (String) findMap.get("ACCOUNT");
		OWNER = (String) findMap.get("OWNER");
		APPROVAL_STATUS_PRINT_NAME = (String) findMap.get("APPROVAL_STATUS_PRINT_NAME");
		CREATE_DT = (String) findMap.get("CREATE_DT");
		MODIFIED = (String) findMap.get("MODIFIED");
		APPROVAL_DT = (String) findMap.get("APPROVAL_DT");
		TYPE = (String) findMap.get("TYPE");
		TYPE_DATA = (String) findMap.get("TYPE_DATA");
		TARGET_ID = (String) findMap.get("TARGET_ID");
		AP_NO = (int) findMap.get("AP_NO");
		HASH_ID = (String) findMap.get("HASH_ID");
		APPROVAL_STATUS = (String) findMap.get("APPROVAL_STATUS");
		PROCESSING_GROUP_ID = (String) findMap.get("PROCESSING_GROUP_ID");
		FILENAME = (String) findMap.get("FILENAME");
		NOTEPAD = (String) findMap.get("NOTEPAD");
		LEVEL = (int) findMap.get("LEVEL");
		FID = (String) findMap.get("FID");
	}


	public int getCHK() {
		return CHK;
	}


	public void setCHK(int cHK) {
		CHK = cHK;
	}


	public int getCHK_C() {
		return CHK_C;
	}


	public void setCHK_C(int cHK_C) {
		CHK_C = cHK_C;
	}


	public int getCHKOLD() {
		return CHKOLD;
	}


	public void setCHKOLD(int cHKOLD) {
		CHKOLD = cHKOLD;
	}


	public String getSUBFILE() {
		return SUBFILE;
	}


	public void setSUBFILE(String sUBFILE) {
		SUBFILE = sUBFILE;
	}


	public String getHOST_NAME() {
		return HOST_NAME;
	}


	public void setHOST_NAME(String hOST_NAME) {
		HOST_NAME = hOST_NAME;
	}


	public String getPATH() {
		return PATH;
	}


	public void setPATH(String pATH) {
		PATH = pATH;
	}


	public String getACCOUNT() {
		return ACCOUNT;
	}


	public void setACCOUNT(String aCCOUNT) {
		ACCOUNT = aCCOUNT;
	}


	public String getOWNER() {
		return OWNER;
	}


	public void setOWNER(String oWNER) {
		OWNER = oWNER;
	}


	public String getAPPROVAL_STATUS_PRINT_NAME() {
		return APPROVAL_STATUS_PRINT_NAME;
	}


	public void setAPPROVAL_STATUS_PRINT_NAME(String aPPROVAL_STATUS_PRINT_NAME) {
		APPROVAL_STATUS_PRINT_NAME = aPPROVAL_STATUS_PRINT_NAME;
	}


	public String getCREATE_DT() {
		return CREATE_DT;
	}


	public void setCREATE_DT(String cREATE_DT) {
		CREATE_DT = cREATE_DT;
	}


	public String getMODIFIED() {
		return MODIFIED;
	}


	public void setMODIFIED(String mODIFIED) {
		MODIFIED = mODIFIED;
	}


	public String getAPPROVAL_DT() {
		return APPROVAL_DT;
	}


	public void setAPPROVAL_DT(String aPPROVAL_DT) {
		APPROVAL_DT = aPPROVAL_DT;
	}


	public String getTYPE() {
		return TYPE;
	}


	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}


	public String getTYPE_DATA() {
		return TYPE_DATA;
	}


	public void setTYPE_DATA(String tYPE_DATA) {
		TYPE_DATA = tYPE_DATA;
	}


	public String getTARGET_ID() {
		return TARGET_ID;
	}


	public void setTARGET_ID(String tARGET_ID) {
		TARGET_ID = tARGET_ID;
	}


	public int getAP_NO() {
		return AP_NO;
	}


	public void setAP_NO(int aP_NO) {
		AP_NO = aP_NO;
	}


	public String getHASH_ID() {
		return HASH_ID;
	}


	public void setHASH_ID(String hASH_ID) {
		HASH_ID = hASH_ID;
	}


	public String getAPPROVAL_STATUS() {
		return APPROVAL_STATUS;
	}


	public void setAPPROVAL_STATUS(String aPPROVAL_STATUS) {
		APPROVAL_STATUS = aPPROVAL_STATUS;
	}


	public String getPROCESSING_GROUP_ID() {
		return PROCESSING_GROUP_ID;
	}


	public void setPROCESSING_GROUP_ID(String pROCESSING_GROUP_ID) {
		PROCESSING_GROUP_ID = pROCESSING_GROUP_ID;
	}


	public String getFILENAME() {
		return FILENAME;
	}


	public void setFILENAME(String fILENAME) {
		FILENAME = fILENAME;
	}


	public String getNOTEPAD() {
		return NOTEPAD;
	}


	public void setNOTEPAD(String nOTEPAD) {
		NOTEPAD = nOTEPAD;
	}


	public int getLEVEL() {
		return LEVEL;
	}


	public void setLEVEL(int lEVEL) {
		LEVEL = lEVEL;
	}


	public String getFID() {
		return FID;
	}


	public void setFID(String fID) {
		FID = fID;
	}


	@Override
	public String toString() {
		return "detection [CHK=" + CHK + ", CHK_C=" + CHK_C + ", CHKOLD=" + CHKOLD + ", SUBFILE=" + SUBFILE
				+ ", HOST_NAME=" + HOST_NAME + ", PATH=" + PATH + ", ACCOUNT=" + ACCOUNT + ", OWNER=" + OWNER
				+ ", APPROVAL_STATUS_PRINT_NAME=" + APPROVAL_STATUS_PRINT_NAME + ", CREATE_DT=" + CREATE_DT
				+ ", MODIFIED=" + MODIFIED + ", APPROVAL_DT=" + APPROVAL_DT + ", TYPE=" + TYPE + ", TYPE_DATA="
				+ TYPE_DATA + ", TARGET_ID=" + TARGET_ID + ", AP_NO=" + AP_NO + ", HASH_ID=" + HASH_ID
				+ ", APPROVAL_STATUS=" + APPROVAL_STATUS + ", PROCESSING_GROUP_ID=" + PROCESSING_GROUP_ID
				+ ", FILENAME=" + FILENAME + ", NOTEPAD=" + NOTEPAD + ", LEVEL=" + LEVEL + ", FID=" + FID + "]";
	}
	
	
}
