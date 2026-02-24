package com.org.iopts.controller;

import com.org.iopts.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Log Controller - 서버 로그 원격 조회 API
 *
 * 관리자(JWT 인증 필요)가 서버 로그를 웹에서 확인할 수 있는 엔드포인트
 */
@Slf4j
@Tag(name = "Logs", description = "서버 로그 조회 API")
@RestController
@RequestMapping("/api/v1/logs")
public class LogController {

    private static final String LOG_DIR = "logs";
    private static final int MAX_LINES = 500;

    /**
     * 로그 파일 목록 조회
     */
    @Operation(summary = "로그 파일 목록", description = "서버에 존재하는 로그 파일 목록 조회")
    @GetMapping("/files")
    public ApiResponse<List<Map<String, Object>>> getLogFiles() {
        log.debug("GET /api/v1/logs/files");

        Path logPath = Paths.get(LOG_DIR);
        if (!Files.exists(logPath)) {
            return ApiResponse.success(Collections.emptyList());
        }

        try (Stream<Path> paths = Files.walk(logPath, 1)) {
            List<Map<String, Object>> files = paths
                    .filter(Files::isRegularFile)
                    .map(p -> {
                        Map<String, Object> info = new LinkedHashMap<>();
                        info.put("fileName", p.getFileName().toString());
                        try {
                            info.put("size", Files.size(p));
                            info.put("sizeText", formatFileSize(Files.size(p)));
                            info.put("lastModified", Files.getLastModifiedTime(p).toString());
                        } catch (IOException e) {
                            info.put("size", 0);
                            info.put("sizeText", "0 B");
                            info.put("lastModified", "-");
                        }
                        return info;
                    })
                    .sorted((a, b) -> ((String)b.get("lastModified")).compareTo((String)a.get("lastModified")))
                    .collect(Collectors.toList());

            return ApiResponse.success(files);
        } catch (IOException e) {
            log.error("로그 파일 목록 조회 실패", e);
            return ApiResponse.success(Collections.emptyList());
        }
    }

    /**
     * 로그 파일 최근 내용 조회 (tail)
     */
    @Operation(summary = "로그 내용 조회 (tail)", description = "로그 파일의 최근 N줄 조회 (기본 100줄, 최대 500줄)")
    @GetMapping("/tail")
    public ApiResponse<Map<String, Object>> tailLog(
            @Parameter(description = "로그 파일명", example = "picenter.log")
            @RequestParam(defaultValue = "picenter.log") String file,
            @Parameter(description = "조회할 줄 수", example = "100")
            @RequestParam(defaultValue = "100") int lines,
            @Parameter(description = "검색 키워드 (선택)", example = "ERROR")
            @RequestParam(required = false) String keyword) {

        log.debug("GET /api/v1/logs/tail - file: {}, lines: {}, keyword: {}", file, lines, keyword);

        // 보안: 파일명에 경로 탐색 문자 차단
        if (file.contains("..") || file.contains("/") || file.contains("\\")) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("error", "잘못된 파일명입니다.");
            return ApiResponse.success(result);
        }

        int requestedLines = Math.min(lines, MAX_LINES);
        Path filePath = Paths.get(LOG_DIR, file);

        if (!Files.exists(filePath)) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("error", "파일을 찾을 수 없습니다: " + file);
            result.put("availableFiles", getAvailableFileNames());
            return ApiResponse.success(result);
        }

        try {
            List<String> tailLines = tailFile(filePath, requestedLines);

            // 키워드 필터링
            if (keyword != null && !keyword.isEmpty()) {
                String kw = keyword.toLowerCase();
                tailLines = tailLines.stream()
                        .filter(line -> line.toLowerCase().contains(kw))
                        .collect(Collectors.toList());
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("file", file);
            result.put("totalLines", tailLines.size());
            result.put("keyword", keyword);
            result.put("content", tailLines);

            return ApiResponse.success(result);
        } catch (IOException e) {
            log.error("로그 파일 읽기 실패: {}", file, e);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("error", "파일 읽기 실패: " + e.getMessage());
            return ApiResponse.success(result);
        }
    }

    /**
     * API 접근 로그 조회
     */
    @Operation(summary = "API 접근 로그 조회", description = "API 접근 로그의 최근 N줄 조회")
    @GetMapping("/api-access")
    public ApiResponse<Map<String, Object>> getApiAccessLog(
            @Parameter(description = "조회할 줄 수", example = "100")
            @RequestParam(defaultValue = "100") int lines,
            @Parameter(description = "HTTP 상태 필터 (예: 500, 404)", example = "500")
            @RequestParam(required = false) String status,
            @Parameter(description = "검색 키워드", example = "/api/v1/users")
            @RequestParam(required = false) String keyword) {

        log.debug("GET /api/v1/logs/api-access - lines: {}, status: {}", lines, status);

        Path filePath = Paths.get(LOG_DIR, "picenter-api.log");
        if (!Files.exists(filePath)) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("message", "API 로그 파일이 아직 없습니다. API 호출 후 생성됩니다.");
            return ApiResponse.success(result);
        }

        try {
            int requestedLines = Math.min(lines, MAX_LINES);
            List<String> tailLines = tailFile(filePath, requestedLines);

            // 상태 코드 필터링
            if (status != null && !status.isEmpty()) {
                tailLines = tailLines.stream()
                        .filter(line -> line.contains("| " + status + " |"))
                        .collect(Collectors.toList());
            }

            // 키워드 필터링
            if (keyword != null && !keyword.isEmpty()) {
                String kw = keyword.toLowerCase();
                tailLines = tailLines.stream()
                        .filter(line -> line.toLowerCase().contains(kw))
                        .collect(Collectors.toList());
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("file", "picenter-api.log");
            result.put("totalLines", tailLines.size());
            result.put("statusFilter", status);
            result.put("keyword", keyword);
            result.put("content", tailLines);

            return ApiResponse.success(result);
        } catch (IOException e) {
            log.error("API 로그 읽기 실패", e);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("error", "파일 읽기 실패: " + e.getMessage());
            return ApiResponse.success(result);
        }
    }

    /**
     * 에러 로그 조회
     */
    @Operation(summary = "에러 로그 조회", description = "에러 전용 로그의 최근 N줄 조회")
    @GetMapping("/errors")
    public ApiResponse<Map<String, Object>> getErrorLog(
            @Parameter(description = "조회할 줄 수", example = "100")
            @RequestParam(defaultValue = "100") int lines,
            @Parameter(description = "검색 키워드", example = "NullPointer")
            @RequestParam(required = false) String keyword) {

        log.debug("GET /api/v1/logs/errors - lines: {}", lines);

        Path filePath = Paths.get(LOG_DIR, "picenter-error.log");
        if (!Files.exists(filePath)) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("message", "에러 로그가 없습니다. 현재 에러가 발생하지 않았습니다.");
            return ApiResponse.success(result);
        }

        try {
            int requestedLines = Math.min(lines, MAX_LINES);
            List<String> tailLines = tailFile(filePath, requestedLines);

            if (keyword != null && !keyword.isEmpty()) {
                String kw = keyword.toLowerCase();
                tailLines = tailLines.stream()
                        .filter(line -> line.toLowerCase().contains(kw))
                        .collect(Collectors.toList());
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("file", "picenter-error.log");
            result.put("totalLines", tailLines.size());
            result.put("keyword", keyword);
            result.put("content", tailLines);

            return ApiResponse.success(result);
        } catch (IOException e) {
            log.error("에러 로그 읽기 실패", e);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("error", "파일 읽기 실패: " + e.getMessage());
            return ApiResponse.success(result);
        }
    }

    // ===== Utility Methods =====

    /**
     * 파일 끝에서 N줄 읽기 (효율적 tail 구현)
     */
    private List<String> tailFile(Path filePath, int lines) throws IOException {
        List<String> result = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "r")) {
            long fileLength = raf.length();
            if (fileLength == 0) return result;

            long pos = fileLength - 1;
            int lineCount = 0;
            StringBuilder sb = new StringBuilder();

            while (pos >= 0 && lineCount < lines) {
                raf.seek(pos);
                int ch = raf.read();
                if (ch == '\n') {
                    if (sb.length() > 0) {
                        result.add(0, sb.reverse().toString());
                        sb.setLength(0);
                        lineCount++;
                    }
                } else {
                    sb.append((char) ch);
                }
                pos--;
            }
            if (sb.length() > 0 && lineCount < lines) {
                result.add(0, sb.reverse().toString());
            }
        }
        return result;
    }

    private List<String> getAvailableFileNames() {
        try (Stream<Path> paths = Files.walk(Paths.get(LOG_DIR), 1)) {
            return paths.filter(Files::isRegularFile)
                    .map(p -> p.getFileName().toString())
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.1f KB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.1f MB", size / (1024.0 * 1024));
        return String.format("%.1f GB", size / (1024.0 * 1024 * 1024));
    }
}
