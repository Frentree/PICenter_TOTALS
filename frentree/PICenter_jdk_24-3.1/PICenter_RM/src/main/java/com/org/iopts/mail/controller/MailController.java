package com.org.iopts.mail.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.iopts.mail.service.MailService;
import com.org.iopts.mail.vo.MailVo;
import com.org.iopts.mail.vo.UserVo;

import com.google.gson.JsonArray;

@Controller
@RequestMapping(value = "/mail")
@Configuration
@PropertySource("classpath:/property/config.properties")
public class MailController {

	private static Logger logger = LoggerFactory.getLogger(MailController.class);

	@Value("${recon.api.version}")
	private String api_ver;
	
	@Inject
	private MailService mailService;
	
	// 메일 발송
	@RequestMapping(value="/serverGroupMailContent", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> serverGroupMailContent(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("serverGroupMailContent");
		Map<String, Object> serverGroupMail = new HashMap<String, Object>();
		
		try {
			serverGroupMail = mailService.serverGroupMailContent(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return serverGroupMail;
	}

	// 메일 발송
    @RequestMapping(value="/serverGroupMail", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> serverGroupMail(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
        logger.info("serverGroupMail");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, List<String>> serverGroupMail = new HashMap<String, List<String>>();
        
        try {
        	serverGroupMail = mailService.serverGroupMail(request);
        	resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "메일 발송이 완료되었습니다.");
		} catch (Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "메일 발송에 실패하였습니다.");
			e.printStackTrace();
		}
        
        return resultMap;
    }
    
    // LG생건 전용 메일 발송
    @RequestMapping(value="/serverGroupMailLg", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> serverGroupMailLg(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
        logger.info("serverGroupMailLg");
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {
            resultMap = mailService.serverGroupMailLg(request);
            resultMap.put("resultCode", 0);
            resultMap.put("resultMessage", "메일 발송이 완료되었습니다.");
        } catch (Exception e) {
            resultMap.put("resultCode", -100);
            resultMap.put("resultMessage", "메일 발송에 실패하였습니다.");
            e.printStackTrace();
        }

        return resultMap;
    }

    // 결재 관리 메일 발송
    @RequestMapping(value="/approvalSendMail", method={RequestMethod.POST})
	@ResponseBody
    public Map<String, Object> approvalSendMail(@RequestBody HashMap<String, Object> params) throws Exception 
	{
    	Map<String, Object> resultMap = new HashMap<String, Object>();
		List<UserVo> chargeMap = new ArrayList<>();

		try {

			chargeMap = mailService.approvalSendMail(params);
			
			if(chargeMap.get(0).getUser_email() != null) {
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "결재 요청을 등록 하였습니다. \n해당 기안자에게 메일 발송이 완료되었습니다.");
			}else {
				resultMap.put("resultCode", -1);
				resultMap.put("resultMessage", "결재 요청을 등록 하였습니다. \n해당 기안자에게 등록된 메일 주소가 없습니다.");
			}
			
		}
		catch (Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "결재 요청을 등록 하였습니다. \n메일발송에 실패하였습니다.");
			e.printStackTrace();
		}

		return resultMap;
    }
	
    @RequestMapping(value="/templateInsert", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> templateInsert(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("templateInsert");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			mailService.templateInsert(request);
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "SUCCESS");
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다.</br>관리자에게 문의하세요.");
			e.printStackTrace();
		}

		return resultMap;
    }

    // 메일 템플릿 목록 조회
    @RequestMapping(value="/mailTemplateList", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> mailTemplateList() throws Exception {
        logger.info("mailTemplateList");

        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> list = mailService.getTemplateList();
            resultMap.put("resultCode", 0);
            resultMap.put("data", list);
        } catch(Exception e) {
            resultMap.put("resultCode", -1);
            resultMap.put("resultMessage", "템플릿 목록 조회에 실패하였습니다.");
            e.printStackTrace();
        }

        return resultMap;
    }

    // 메일 템플릿 상세 조회
    @RequestMapping(value="/mailTemplate", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> mailTemplate(@RequestParam("idx") int idx) throws Exception {
        logger.info("mailTemplate idx: " + idx);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            Map<String, Object> data = mailService.getTemplateDetail(idx);
            resultMap.put("resultCode", 0);
            resultMap.put("data", data);
        } catch(Exception e) {
            resultMap.put("resultCode", -1);
            resultMap.put("resultMessage", "템플릿 조회에 실패하였습니다.");
            e.printStackTrace();
        }

        return resultMap;
    }

    // 메일 템플릿 등록
    @RequestMapping(value="/insertMailTemplate", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> insertMailTemplate(@RequestParam("name") String name, @RequestParam("con") String con) throws Exception {
        logger.info("insertMailTemplate");

        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("name", name);
            params.put("template_con", con);
            mailService.insertTemplate(params);
            resultMap.put("resultCode", 0);
            resultMap.put("resultMessage", "템플릿이 등록되었습니다.");
        } catch(Exception e) {
            resultMap.put("resultCode", -1);
            resultMap.put("resultMessage", "템플릿 등록에 실패하였습니다.");
            e.printStackTrace();
        }

        return resultMap;
    }

    // 메일 템플릿 수정
    @RequestMapping(value="/updateMailTemplate", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> updateMailTemplate(@RequestParam("idx") int idx, @RequestParam("name") String name, @RequestParam("con") String con) throws Exception {
        logger.info("updateMailTemplate idx: " + idx);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("idx", idx);
            params.put("name", name);
            params.put("template_con", con);
            mailService.updateTemplate(params);
            resultMap.put("resultCode", 0);
            resultMap.put("resultMessage", "템플릿이 수정되었습니다.");
        } catch(Exception e) {
            resultMap.put("resultCode", -1);
            resultMap.put("resultMessage", "템플릿 수정에 실패하였습니다.");
            e.printStackTrace();
        }

        return resultMap;
    }

    // 메일 템플릿 삭제
    @RequestMapping(value="/deleteMailTemplate", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> deleteMailTemplate(@RequestParam("idx") int idx) throws Exception {
        logger.info("deleteMailTemplate idx: " + idx);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            mailService.deleteTemplate(idx);
            resultMap.put("resultCode", 0);
            resultMap.put("resultMessage", "템플릿이 삭제되었습니다.");
        } catch(Exception e) {
            resultMap.put("resultCode", -1);
            resultMap.put("resultMessage", "템플릿 삭제에 실패하였습니다.");
            e.printStackTrace();
        }

        return resultMap;
    }

}