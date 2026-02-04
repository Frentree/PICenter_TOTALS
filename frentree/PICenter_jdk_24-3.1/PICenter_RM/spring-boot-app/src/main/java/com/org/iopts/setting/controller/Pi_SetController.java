package com.org.iopts.setting.controller;

import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.org.iopts.service.Pi_UserService;
import com.org.iopts.setting.service.Pi_SetService;
import com.org.iopts.util.SessionTimeoutFilter;
import com.org.iopts.util.SessionUtil;

/**
 * Handles requests for the application home page.
 */

@Controller
@RequestMapping(value = "/setting")
public class Pi_SetController {

	private static Logger logger = LoggerFactory.getLogger(Pi_SetController.class);
	
	@Inject
	private Pi_SetService service;
	
	@Inject
	private Pi_UserService userService;
	
	private String requestUrl = null;
	private String retrunUrl  = null;
	
	@RequestMapping(value = "/pi_interlock", method = RequestMethod.GET)
	public String pi_interlock(Locale locale, Model model, HttpServletRequest request) throws Exception {
		
		requestUrl = "/setting/pi_interlock";
		
		try {
			Map<String, Object> pageGrade = service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));

				List<Map<Object, Object>> setMap = service.selectSetting(1);
				List<Map<Object, Object>> approvalMap = service.selectApprovalStatus();
				List<Map<String, Object>> resultList = service.selectbatchData("mail");
				
				 // 현재 세션 가져오기
		        HttpSession session = request.getSession();
		        
		        // 세션 타임아웃 값 가져오기 (초 단위)
		        int timeoutInSeconds = session.getMaxInactiveInterval();
		        
		        // 시간을 시, 분, 초로 변환
		        int hours = timeoutInSeconds / 3600;
		        int minutes = (timeoutInSeconds % 3600) / 60;
		        int seconds = timeoutInSeconds % 60;
		        
		        Map<String, Integer> sessionMap = new HashMap<>();
		        sessionMap.put("hours", hours);
		        sessionMap.put("minutes", minutes);
		        sessionMap.put("seconds", seconds);
		        sessionMap.put("timeoutInSeconds", timeoutInSeconds);
		        
		        model.addAttribute("session", sessionMap);
				model.addAttribute("setMap", setMap);
				model.addAttribute("approvalMap", approvalMap);
				model.addAttribute("resultList", resultList);
				model.addAttribute("resultSize", resultList.size());
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
			e.printStackTrace();
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value = "/pi_system", method = RequestMethod.GET)
	public String pi_system(Locale locale, Model model, HttpServletRequest request) throws Exception {
		
		requestUrl = "/setting/pi_system";
		
		try {
			Map<String, Object> pageGrade = service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
			e.printStackTrace();
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value = "/crypt", method = RequestMethod.GET)
	public String crypt(Locale locale, Model model) throws Exception {
		
		String grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		try {
			
			if(grade == null || !grade.equals("9") ) {
				retrunUrl = "/error/notPageGrade";
			}else {
				retrunUrl = "/crypt";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value = "/cryptPWDChk", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> cryptPWDChk(HttpServletRequest request, Model model){
		logger.info("cryptPWDChk");
		String grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		Map<String, Object> resultMap = new HashMap<>();
		String password = request.getParameter("password");
		try {
			
			if(grade == null || !grade.equals("9") ) {
				resultMap.put("resultMessage", "올바르지 않은 정보");
				resultMap.put("resultCode", -10);
			}else {
				if(!password.equals("tree0119!@")) {
					resultMap.put("resultMessage", "올바르지 않은 정보");
					resultMap.put("resultCode", -10);
				}else {
					resultMap.put("resultMessage", "chk user");
					resultMap.put("resultCode", 0);
				}
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return resultMap;
	}
	
	@RequestMapping(value = "/cryptPWD", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> cryptPWD(HttpServletRequest request, Model model){
		logger.info("cryptPWD");
		String grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		Map<String, Object> resultMap = new HashMap<>();
		String password = request.getParameter("password");
		try {
			
			if(grade == null || !grade.equals("9") ) {
				resultMap.put("resultMessage", "올바르지 않은 정보");
				resultMap.put("resultCode", -10);
			}else {
				resultMap = service.cryptPWD(request);
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return resultMap;
	}
	
	@RequestMapping(value = "/patternList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> patternList(HttpServletRequest request, Model model){
		logger.info("patternList");
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = service.patternList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/approvalAlert", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> approvalAlert(HttpServletRequest request, Model model){
		logger.info("approvalAlert");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = service.approvalAlert(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/selectGroupApprovalUser", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>>selectGroupApprovalUser(HttpServletRequest request, Model model){
		logger.info("selectGroupApprovalUser");
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		try {
			resultList = service.selectGroupApprovalUser(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	@RequestMapping(value = "/deleteGroupApprovalUser", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object>deleteGroupApprovalUser(HttpServletRequest request, Model model){
		logger.info("deleteGroupApprovalUser");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			service.deleteGroupApprovalUser(request);
			resultMap.put("resultCode", 200);
			resultMap.put("resultMessage", "SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다.</br>관리자에게 문의하세요.");
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/insertGroupApprovalUser", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object>insertGroupApprovalUser(HttpServletRequest request, Model model){
		logger.info("insertGroupApprovalUser");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			service.insertGroupApprovalUser(request);
			resultMap.put("resultCode", 200);
			resultMap.put("resultMessage", "SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다.</br>관리자에게 문의하세요.");
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/updateCustomPattern", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> updateCustomPattern(HttpServletRequest request, Model model){
		logger.info("updateCustomPattern");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = service.updateCustomPattern(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/deleteCustomPattern", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteCustomPattern(HttpServletRequest request, Model model){
		logger.info("deleteCustomPattern");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = service.deleteCustomPattern(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/insertCustomPattern", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertCustomPattern(HttpServletRequest request, Model model){
		logger.info("insertCustomPattern");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = service.insertCustomPattern(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/customPatternChagne", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> customPatternChagne(HttpServletRequest request, Model model){
		logger.info("customPatternChagne");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = service.customPatternChagne(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
//	서버 담당자 지정
	@RequestMapping(value = "/nameList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> nameList(HttpServletRequest request, Model model){
		logger.info("nameList");
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = service.nameList(request.getParameter("status"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/nameListUpdate", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> nameListUpdate(HttpServletRequest request, Model model){
		logger.info("nameListUpdate");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = service.nameListUpdate(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/nameListDelete", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> nameListDelete(HttpServletRequest request, Model model){
		logger.info("nameListDelete");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = service.nameListDelete(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/nameListCreate", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> nameListCreate(HttpServletRequest request, Model model){
		logger.info("nameListCreate");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = service.nameListCreate(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/conDataList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> conDataList(HttpServletRequest request, Model model){
		logger.info("conDataList");
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = service.conDataList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/ConListUpdate", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> ConListDelete(HttpServletRequest request, Model model){
		logger.info("ConListUpdate");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = service.ConListUpdate(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	// 팀별 결재자 관리
	@RequestMapping(value = "/groupApprovalList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> groupApprovalList(HttpServletRequest request, Model model){
		logger.info("groupApprovalList");
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = service.groupApprovalList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	// 필수 결재자 관리
	@RequestMapping(value = "/approvalList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> approvalList(HttpServletRequest request, Model model){
		logger.info("approvalList");
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = service.approvalList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@RequestMapping(value="/updateBatchApproval", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> updateBatchApproval(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("updateBatchApproval");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = service.updateBatchApproval(request);
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다. \n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	
//	조치 계획 관리
	@RequestMapping(value = "/pi_search_manage", method = RequestMethod.GET)
	public String search_manage(Model model, HttpServletRequest request) throws Exception {
		logger.info("pi_search_manage");
		
		requestUrl = "/setting/pi_search_manage";
		try {
			Map<String, Object> pageGrade = service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());

				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				List<Map<Object, Object>> setMap = service.selectSetting(2);
				
				model.addAttribute("setMap", setMap);
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value = "/getProcessingFlag", method ={RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody  List<Map<String,Object>> getProcessingFlag(HttpServletRequest request) throws Exception {
		logger.info("getProcessingFlag");
		
		List<Map<String,Object>> list = service.getProcessingFlag(request.getParameter("gridName"));
		
		return list;
	}
	
	@RequestMapping(value = "/updateProcessingFlag", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Map<String, Object> updateProcessingFlag(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			System.out.println("request :: "+request.getParameter("processing_flag_name"));
			service.updateProcessingFlag(request);
			
			resultMap.put("resultCode", 200);
			resultMap.put("resultMessage", "SUCCESS");
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다.</br>관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	@RequestMapping(value = "/deleteProcessingFlag", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Map<String, Object> deleteProcessingFlag(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			System.out.println("request :: "+request.getParameter("processing_flag_name"));
			service.deleteProcessingFlag(request);
			
			resultMap.put("resultCode", 200);
			resultMap.put("resultMessage", "SUCCESS");
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다.</br>관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
	}
	@RequestMapping(value = "/updateExceptionFlag", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Map<String, Object> updateExceptionFlag(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			service.updateExceptionFlag(request);
			
			resultMap.put("resultCode", 200);
			resultMap.put("resultMessage", "SUCCESS");
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다.</br>관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
	}
	@RequestMapping(value = "/insertProcessingFlag", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Map<String, Object> insertProcessingFlag(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			System.out.println("request :: "+request.getParameter("processing_flag_name"));
			service.insertProcessingFlag(request);
			
			resultMap.put("resultCode", 200);
			resultMap.put("resultMessage", "SUCCESS");
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다.</br>관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
	}
	@RequestMapping(value = "/insertExceptionFlag", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Map<String, Object> insertExceptionFlag(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			service.insertExceptionFlag(request);
			
			resultMap.put("resultCode", 200);
			resultMap.put("resultMessage", "SUCCESS");
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다.</br>관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	
	@RequestMapping(value="/updateBatchSchedule", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> updateBatchSchedule(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("updateBatchSchedule");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = service.updateBatchSchedule(request);
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다. \n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	@RequestMapping(value = "/chkPattern", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertProfile(HttpServletRequest request, Model model){
		logger.info("chkPattern");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = service.chkPattern(request);
		}
		catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}

		return resultMap;

	}

//	무결성 검증
	@RequestMapping(value = "/pi_node", method = RequestMethod.GET)
	public String pi_node(Model model, HttpServletRequest request) throws Exception {
		logger.info("pi_node");
		
		requestUrl = "/setting/pi_node";
		try {
			Map<String, Object> pageGrade = service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());

				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				Properties properties = new Properties();
				String resource = "/property/config.properties";
				Reader reader = Resources.getResourceAsReader(resource);
				
				properties.load(reader);
				
				String p_md5 = properties.getProperty("pic.md5");
				String p_sha1 = properties.getProperty("pic.sha1");
				String p_sha256 = properties.getProperty("pic.sha256");
				String p_filename = properties.getProperty("pic.filename");
				
				String r_md5 = properties.getProperty("recon.md5");
				String r_sha1 = properties.getProperty("recon.sha1");
				String r_sha256 = properties.getProperty("recon.sha256");
				String r_filename = properties.getProperty("recon.filename");
				String r_version = properties.getProperty("recon.version");
				
				Map<String, Object> picNodeMap = new HashMap<>();
				picNodeMap.put("version", "0.1");
				picNodeMap.put("filename", p_filename);
				picNodeMap.put("md5_hash", p_md5);
				picNodeMap.put("sha1_hash", p_sha1);
				picNodeMap.put("sha256_hash", p_sha256);
				
				Map<String, Object> reconNodeMap = new HashMap<>();
				reconNodeMap.put("version", "0.1");
				reconNodeMap.put("filename", "/home/picenter/Tomcat/webapps/ROOT.war");
				reconNodeMap.put("md5_hash", r_md5);
				reconNodeMap.put("r_version", r_version);
				reconNodeMap.put("filename", r_filename);
				reconNodeMap.put("sha1_hash", r_sha1);
				reconNodeMap.put("sha256_hash", r_sha256);
				
				model.addAttribute("reconNodeMap", reconNodeMap);
				 model.addAttribute("picNodeMap", picNodeMap);


			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value = "/reconNodeSelect", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> glovalFilterDetail(HttpServletRequest request, Model model){
		logger.info("reconNodeSelect");
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = service.reconNodeSelect(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/PICNodeSelect", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> PICNodeSelect(HttpServletRequest request, Model model){
		logger.info("PICNodeSelect");
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = service.PICNodeSelect(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/backupTables", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> backupTables(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {
	    Map<String, Object> resultMap = new HashMap<>();
	    try {
	    	int value = (int) requestData.get("value");
	        List<String> tables = (List<String>) requestData.get("tables");
	        resultMap = service.backupTables(tables, value, request);
	        if ((int) resultMap.get("resultCode") == 0) {
	            return new ResponseEntity<>(resultMap, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    } catch (Exception e) {
	        logger.error("Error: " + e.getMessage(), e);
	        resultMap.put("resultCode", -1);
	        resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
	        return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
    
    @RequestMapping(value = "/rollBackTables", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> rollBackTables(@RequestBody Map<String, String> requestData, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String fileName = requestData.get("fileName");  // 파일 이름을 JSON 바디에서 추출
            resultMap = service.rollBackTables(fileName, request);
            if ((int) resultMap.get("resultCode") == 0) {
                return new ResponseEntity<>(resultMap, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage(), e);
            resultMap.put("resultCode", -1);
            resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
            return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/rollBackList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> rollBackList(HttpServletRequest request, Model model){
		logger.info("rollBackList");
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = service.rollBackList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
    @RequestMapping(value = "/systemLog", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> systemLog(HttpServletRequest request, Model model){
    	logger.info("systemLog");
    	
    	Map<String, Object> resultMap = null;
    	try {
    		resultMap = service.systemLog(request);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return resultMap;
    }
	
	@RequestMapping(value = "/userSesstionUpdate", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> userSesstionUpdate(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
        	String grade = SessionUtil.getSession("memberSession", "USER_GRADE");
        	
        	if(grade == null || !grade.equals("9") ) {
				resultMap.put("resultMessage", "올바르지 않은 접근");
				resultMap.put("resultCode", -10);
				
				return resultMap;
			}
        	
//        	int hours = Integer.parseInt(request.getParameter("hours"));
//            int minutes = Integer.parseInt(request.getParameter("minutes"));
//            int seconds = Integer.parseInt(request.getParameter("seconds"));
        	
            int session_sec = Integer.parseInt(request.getParameter("session"));

            try {
            	   SessionTimeoutFilter.setSessionTimeoutInSeconds(session_sec);

                   session = request.getSession();
                   session.setMaxInactiveInterval(session_sec);

                   resultMap.put("resultCode", 0);
                   resultMap.put("resultMessage", "세션 타임아웃 설정이 변경되었습니다.");
                   
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("error1 ::: " + e);
	        	resultMap.put("resultCode", -2);
	        	resultMap.put("resultMessage", "타임아웃 설정 중 오류 발생");
				
	        	return resultMap;
			}
            
         
            service.userlogUpdate(request, "세션 타임아웃 설정 변경 : " +session_sec + " 초");
            
        } catch (Exception e) {
        	e.printStackTrace();
        	logger.error("error :: " + e);
        	resultMap.put("resultCode", -2);
        	resultMap.put("resultMessage", "타임아웃 설정 중 오류 발생");
        	
        	return resultMap;
        }
        return resultMap;
    }
	
	@RequestMapping(value = "/reportHeaderList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> reportHeaderList(HttpServletRequest request, Model model){
		logger.info("reportHeaderList");
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = service.reportHeaderList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	
	
}
