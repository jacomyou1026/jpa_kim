package org.example.controller;

import com.example.boarded.vo.FileVo;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@Log4j2
public class fileController {


    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<List<FileVo>> upload(MultipartFile[] multipartFiles) throws IOException {
        log.info("upload-----");
        List<FileVo> files = new ArrayList<>();
        String uploadFolderDictory = "C://upload";
        String uploadDatePath =getDirectoryForm();

        File uploadPath = new File(uploadFolderDictory, uploadDatePath); //연결
        log.info("uploadPath = {}",uploadPath);

        //오늘 처음 올린 경우에만 디렉토리 만들어줌
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        for (MultipartFile multipartFile: multipartFiles) {
            log.info("--------------");
            log.info("upload File Name: " + multipartFile.getOriginalFilename());
            log.info("upload File size: " + multipartFile.getSize());

            FileVo fileVo = new FileVo();
            String originalFilename = multipartFile.getOriginalFilename(); // 원본이름
            String filename = null;


            UUID uuid = UUID.randomUUID();
            filename = uuid + "_" + originalFilename;

            //set
            fileVo.setUuid(uuid.toString());
            fileVo.setFileName(originalFilename);
            fileVo.setUploadPath(uploadDatePath);


            File file = new File(uploadPath,filename);

            //해당 경로 파일 업로드
            multipartFile.transferTo(file);

            //업로드한 파일 경로를 읽기 위함
            InputStream in = new FileInputStream(file);
            if (checkImageType(file)) { //업로드된 파일이 이미지라면
                fileVo.setFileType(true); //이미지면 true
                //썸네일 작업(이름의 구분점 주기)
                FileOutputStream out= new FileOutputStream(new File(uploadPath,"t_"+filename));
                //어떤 파일을 만들지
                Thumbnailator.createThumbnail(in,out,100,100); //100 * 100

                in.close();
                out.close();


            }

            files.add(fileVo);

        }
        return new ResponseEntity<List<FileVo>>(files, HttpStatus.OK);


    }

    //년/월/일 - 오늘 올린지 ,어제 올린지 알기 위함
    private String getDirectoryForm() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date today = new Date();
        return simpleDateFormat.format(today);
    }

    //이미지인지 판단
    private boolean checkImageType(File file) throws IOException {
        //헤더의 포함된 파일의 정보(종류) -
        String contentType = Files.probeContentType(file.toPath());
        log.info("contentType = {} ",contentType);
        return contentType.startsWith("image"); // 이미지 파일인지

    }

}
