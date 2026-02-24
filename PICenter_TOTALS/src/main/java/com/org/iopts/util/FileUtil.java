package com.org.iopts.util;

import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * File Utility Component
 *
 * Provides common file operations: save, read, delete, and extension extraction.
 * Upload base path is configured via application.yml (file.upload.path).
 */
@Slf4j
@Component
public class FileUtil {

    @Value("${file.upload.path:/data/picenter/uploads}")
    private String uploadPath;

    /**
     * Save a MultipartFile to disk.
     *
     * @param file         the uploaded file
     * @param subDirectory subdirectory under uploadPath (e.g. "downloads", "reports")
     * @return the absolute path of the saved file
     */
    public String saveFile(MultipartFile file, String subDirectory) {
        try {
            if (file == null || file.isEmpty()) {
                throw new CustomException(ErrorCode.INVALID_PARAMETER, "File is empty");
            }

            // Build target directory
            Path directory = Paths.get(uploadPath, subDirectory);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
                log.info("Created upload directory: {}", directory);
            }

            // Generate unique file name to avoid collisions
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String uniqueFileName = UUID.randomUUID().toString() + (extension.isEmpty() ? "" : "." + extension);

            Path targetPath = directory.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            log.info("File saved: {} -> {}", originalFilename, targetPath);
            return targetPath.toString();
        } catch (CustomException e) {
            throw e;
        } catch (IOException e) {
            log.error("Failed to save file: {}", file.getOriginalFilename(), e);
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED, "Failed to save file: " + e.getMessage());
        }
    }

    /**
     * Read a file from disk and return its bytes.
     *
     * @param filePath absolute path to the file
     * @return byte array of file content
     */
    public byte[] readFile(String filePath) {
        try {
            Path path = Paths.get(filePath);

            if (!Files.exists(path)) {
                log.error("File not found: {}", filePath);
                throw new CustomException(ErrorCode.FILE_NOT_FOUND, "File not found: " + filePath);
            }

            byte[] bytes = Files.readAllBytes(path);
            log.debug("File read: {} ({} bytes)", filePath, bytes.length);
            return bytes;
        } catch (CustomException e) {
            throw e;
        } catch (IOException e) {
            log.error("Failed to read file: {}", filePath, e);
            throw new CustomException(ErrorCode.FILE_READ_FAILED, "Failed to read file: " + e.getMessage());
        }
    }

    /**
     * Delete a file from disk.
     *
     * @param filePath absolute path to the file
     * @return true if file was deleted, false otherwise
     */
    public boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);

            if (!Files.exists(path)) {
                log.warn("File not found for deletion: {}", filePath);
                return false;
            }

            boolean deleted = Files.deleteIfExists(path);
            if (deleted) {
                log.info("File deleted: {}", filePath);
            }
            return deleted;
        } catch (IOException e) {
            log.error("Failed to delete file: {}", filePath, e);
            throw new CustomException(ErrorCode.FILE_DELETE_FAILED, "Failed to delete file: " + e.getMessage());
        }
    }

    /**
     * Extract file extension from a filename.
     *
     * @param fileName original file name (e.g. "report.xlsx")
     * @return extension without dot (e.g. "xlsx"), or empty string if none
     */
    public String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex < 0 || lastDotIndex == fileName.length() - 1) {
            return "";
        }

        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}
