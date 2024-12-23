package vip.xiaozhao.intern.baseUtil.intf.entity;

import cn.hutool.core.util.RandomUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Base64MultipartFile implements MultipartFile {

    private final byte[] fileContent;
    private final String fileName;
    private final String contentType;

    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put("image/png", "png");
        MIME_TYPES.put("image/jpeg", "jpg");
        MIME_TYPES.put("image/gif", "gif");
        MIME_TYPES.put("application/pdf", "pdf");
        MIME_TYPES.put("text/plain", "txt");
        // 添加更多 MIME 类型
    }

    public Base64MultipartFile(String base64Image) {
        String[] parts = base64Image.split(",");
        this.fileContent = Base64.getDecoder().decode(parts[1]);
        this.contentType = parts[0].split(";")[0].split(":")[1];
        String extension = MIME_TYPES.getOrDefault(contentType, "jpg");
        this.fileName = RandomUtil.randomNumbers(6) + extension;
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public String getOriginalFilename() {
        return fileName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return fileContent == null || fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return fileContent.length;
    }

    @Override
    public byte[] getBytes() {
        return fileContent;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(fileContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (InputStream inputStream = new ByteArrayInputStream(fileContent)) {
            Files.copy(inputStream, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}