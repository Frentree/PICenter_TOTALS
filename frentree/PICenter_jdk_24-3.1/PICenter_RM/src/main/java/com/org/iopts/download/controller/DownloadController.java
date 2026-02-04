package com.org.iopts.download.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.org.iopts.download.service.DownloadService;
import com.org.iopts.service.Pi_TargetService;

@Controller
@RequestMapping(value = "/download")
@Configuration
@PropertySource("classpath:/property/config.properties")
@PropertySource("classpath:/property/filepath.properties")
public class DownloadController {

	private static Logger logger = LoggerFactory.getLogger(DownloadController.class);

	@Value("${recon.api.version}")
	private String api_ver;

	@Value("${notice.file.path}")
	private String file_path;
	
	@Value("${download.file.path}")
	private String download_path;
	
	@Value("${download.netFile.path}")
	private String Excel_path;
	
	@Value("${download.excel.path}")
	private String excelDown_path;
	
	@Inject
	private DownloadService service;

	// 허용된 파일 확장자 화이트리스트
	private static final String[] ALLOWED_EXTENSIONS = {
		"jpg", "jpeg", "png", "gif", "bmp", "pdf", "doc", "docx",
		"xls", "xlsx", "ppt", "pptx", "txt", "zip", "hwp", "csv"
	};

	// 파일 확장자 검증
	private boolean isAllowedExtension(String filename) {
		if (filename == null || !filename.contains(".")) {
			return false;
		}
		String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
		return Arrays.asList(ALLOWED_EXTENSIONS).contains(ext);
	}

	// 파일명 정제 (Path Traversal 방지)
	private String sanitizeFilename(String filename) {
		if (filename == null) {
			return null;
		}
		return filename.replaceAll("\\.\\.", "").replaceAll("/", "").replaceAll("\\\\", "");
	}

	// 파일 경로 검증 (Path Traversal 방지)
	private File validateFilePath(String basePath, String filename) throws SecurityException, IOException {
		String safeFilename = sanitizeFilename(filename);
		File file = new File(basePath, safeFilename);
		String canonicalPath = file.getCanonicalPath();
		String allowedPath = new File(basePath).getCanonicalPath();

		if (!canonicalPath.startsWith(allowedPath)) {
			logger.warn("Path Traversal 시도 감지: " + filename);
			throw new SecurityException("잘못된 파일 경로입니다.");
		}
		return file;
	}

	@RequestMapping(value = "/upload", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> upload(@RequestParam("uploadFile") MultipartFile file, HttpServletRequest request)
			throws IllegalStateException, IOException {

		Map<String, Object> map = new HashMap<String, Object>();
		String fileName = file.getOriginalFilename();

		if (!file.getOriginalFilename().isEmpty()) {
			// 파일 확장자 검증
			if (!isAllowedExtension(fileName)) {
				logger.warn("허용되지 않은 파일 확장자 업로드 시도: " + fileName);
				map.put("resultCode", -2);
				map.put("resultMassage", "허용되지 않은 파일 형식입니다.");
				return map;
			}

			try {
				String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
				int index = service.selectDownloadIndex(map, request);
				String real_file_name = ((index +1)+"."+ext);
				file.transferTo(new File(file_path, real_file_name));

				map.put("file_name", fileName);
				map.put("real_file_name", real_file_name);

				int result = service.insertDownload(map, request);

				map.put("resultCode", 0);
				map.put("resultMassage", "파일 업로드 성공");
				map.put("fileName", fileName);
				map.put("fileNumber", result);


			} catch (Exception e) {
				logger.error("파일 업로드 실패: " + e.getMessage(), e);
				map.put("resultCode", -1);
				map.put("resultMassage", "파일 업로드 실패");
			}

		}

		return map;
	}

	/*
	 * 
	 */
	@RequestMapping(value = "/file", method = { RequestMethod.POST })
	public @ResponseBody byte[] file(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response, @RequestParam String filename, @RequestParam String realfilename) throws Exception {
		String header = request.getHeader("User-Agent");
		boolean ie = (header.indexOf("MSIE") > -1) || (header.indexOf("Trident") > -1);
		String fileName = "";

		logger.info("filename >> " + filename +  ", realFilename >> " + realfilename);
		logger.info("path >> " + file_path +  ", realFilename >> " + realfilename);

		// Path Traversal 검증
		File file = validateFilePath(file_path, realfilename);

		byte[] bytes = FileCopyUtils.copyToByteArray(file);

		String fn = new String(filename.getBytes(), "utf-8");
		String name = "";
		if (header.contains("Edge")){
		    name = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		    response.setHeader("Content-Disposition", "attachment;filename=\"" + name + "\"");
		} else if (header.contains("MSIE") || header.contains("Trident")) { // IE 11버전부터 Trident로 변경되었기때문에 추가해준다.
		    name = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		    response.setHeader("Content-Disposition", "attachment;filename=\"" + name + "\"");
		} else if (header.contains("Chrome")) {
		    name = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
		} else if (header.contains("Opera")) {
		    name = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
		} else if (header.contains("Firefox")) {
		    name = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
		}

		
		//response.setHeader("Content-Disposition", "attachment;filename=\"" + fn + "\"");
		response.setContentLength(bytes.length);

		return bytes;
	}
	

	
	@RequestMapping(value = "/downloadUpload", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> downloadUpload(@RequestParam("uploadFile") MultipartFile file, HttpServletRequest request)
			throws IllegalStateException, IOException {

		Map<String, Object> map = new HashMap<String, Object>();
		String fileName = file.getOriginalFilename();

		if (!file.getOriginalFilename().isEmpty()) {
			// 파일 확장자 검증
			if (!isAllowedExtension(fileName)) {
				logger.warn("허용되지 않은 파일 확장자 업로드 시도: " + fileName);
				map.put("resultCode", -2);
				map.put("resultMassage", "허용되지 않은 파일 형식입니다.");
				return map;
			}

			try {
				String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
				int index = service.selectFileDownloadIndex(map, request);
				String real_file_name = ((index +1)+"."+ext);
				file.transferTo(new File(download_path, real_file_name));

				map.put("file_name", fileName);
				map.put("real_file_name", real_file_name);

				int result = service.insertFileDownload(map, request);

				map.put("resultCode", 0);
				map.put("resultMassage", "파일 업로드 성공");
				map.put("fileName", fileName);
				map.put("fileNumber", result);


			} catch (Exception e) {
				logger.error("파일 업로드 실패: " + e.getMessage(), e);
				map.put("resultCode", -1);
				map.put("resultMassage", "파일 업로드 실패");
			}

		}

		return map;
	}
	

	@RequestMapping(value = "/downloadfile", method = { RequestMethod.POST })
	public @ResponseBody byte[] downloadfile(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response, @RequestParam String filename, @RequestParam String realfilename) throws Exception {
		String header = request.getHeader("User-Agent");
		boolean ie = (header.indexOf("MSIE") > -1) || (header.indexOf("Trident") > -1);
		String fileName = "";

		logger.info("filename >> " + filename +  ", realFilename >> " + realfilename);
		logger.info("path >> " + download_path +  ", realFilename >> " + realfilename);

		// Path Traversal 검증
		File file = validateFilePath(download_path, realfilename);

		byte[] bytes = FileCopyUtils.copyToByteArray(file);

		String fn = new String(filename.getBytes(), "utf-8");
		String name = "";
		if (header.contains("Edge")){
		    name = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		    response.setHeader("Content-Disposition", "attachment;filename=\"" + name + "\"");
		} else if (header.contains("MSIE") || header.contains("Trident")) { // IE 11버전부터 Trident로 변경되었기때문에 추가해준다.
		    name = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		    response.setHeader("Content-Disposition", "attachment;filename=\"" + name + "\"");
		} else if (header.contains("Chrome")) {
		    name = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
		} else if (header.contains("Opera")) {
		    name = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
		} else if (header.contains("Firefox")) {
		    name = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
		}

		
		//response.setHeader("Content-Disposition", "attachment;filename=\"" + fn + "\"");
		response.setContentLength(bytes.length);

		return bytes;
	}
	
	@RequestMapping(value = "/downloadExcel", method = { RequestMethod.POST })
	public @ResponseBody byte[] downloadExcel(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response, @RequestParam String filename, @RequestParam String realfilename) throws Exception {
		String header = request.getHeader("User-Agent");
		boolean ie = (header.indexOf("MSIE") > -1) || (header.indexOf("Trident") > -1);
		String fileName = "";

		// Path Traversal 검증
		File file = validateFilePath(Excel_path, realfilename);
		
		byte[] bytes = FileCopyUtils.copyToByteArray(file);
		
		String fn = new String(filename.getBytes(), "utf-8");
		String name = "";
		if (header.contains("Edge")){
			name = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + name + "\"");
		} else if (header.contains("MSIE") || header.contains("Trident")) { // IE 11버전부터 Trident로 변경되었기때문에 추가해준다.
			name = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + name + "\"");
		} else if (header.contains("Chrome")) {
			name = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
		} else if (header.contains("Opera")) {
			name = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
		} else if (header.contains("Firefox")) {
			name = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
		}
		
		response.setContentLength(bytes.length);
		
		return bytes;
	}
	
	@RequestMapping(value = "/excelDownloadfile", method = { RequestMethod.POST })
	public @ResponseBody byte[] excelDownloadfile(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response, @RequestParam String filename, @RequestParam String realfilename) throws Exception {
		String header = request.getHeader("User-Agent");
		boolean ie = (header.indexOf("MSIE") > -1) || (header.indexOf("Trident") > -1);
		String fileName = "";

		logger.info("filename >> " + filename +  ", realFilename >> " + realfilename);
		logger.info("path >> " + excelDown_path +  ", realFilename >> " + realfilename);

		// Path Traversal 검증
		File file = validateFilePath(excelDown_path, realfilename);

		byte[] bytes = FileCopyUtils.copyToByteArray(file);

		String fn = new String(filename.getBytes(), "utf-8");
		String name = "";
		if (header.contains("Edge")){
		    name = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		    response.setHeader("Content-Disposition", "attachment;filename=\"" + name + "\"");
		} else if (header.contains("MSIE") || header.contains("Trident")) { // IE 11버전부터 Trident로 변경되었기때문에 추가해준다.
		    name = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		    response.setHeader("Content-Disposition", "attachment;filename=\"" + name + "\"");
		} else if (header.contains("Chrome")) {
		    name = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
		} else if (header.contains("Opera")) {
		    name = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
		} else if (header.contains("Firefox")) {
		    name = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
		}

		//response.setHeader("Content-Disposition", "attachment;filename=\"" + fn + "\"");
		response.setContentLength(bytes.length);

		return bytes;
	}
	
	@RequestMapping(value="/downLoadFileInformation", method={RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> downLoadFileInformation(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response, @RequestBody HashMap<String, Object> params) throws Exception {
		logger.info("downLoadFileInformation");
		
		Map<String, Object> map = new HashMap<>();
		try {  
			map = service.downLoadFileInformation(request, params);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return map;
	}
	
	@RequestMapping(value = "/downloadList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> downloadList(HttpServletRequest request, Model model){
		logger.info("downloadList"); 
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = service.downloadList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

}