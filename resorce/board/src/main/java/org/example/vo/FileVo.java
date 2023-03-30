package org.example.vo;

import lombok.Data;

@Data
public class FileVo {
    private String uuid;
    private String uploadPath;
    private String fileName;
    private boolean fileType;
}
