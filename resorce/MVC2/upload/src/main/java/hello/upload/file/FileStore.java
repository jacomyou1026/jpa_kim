package hello.upload.file;

import hello.upload.domain.UploadFile;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@Log4j
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public  String getFullPath(String filename) {
        return fileDir+ filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
            List<UploadFile> storeFileResult = new ArrayList<>();
            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) {
                    storeFileResult.add(storeFile(multipartFile));
                }
            }
            System.out.println("storeFileResult = " + storeFileResult);
        return storeFileResult;
    }

    //파일 업로드
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();


        //image.png
        String storeFileName = createdSoreFileName(originalFilename);

        String updateDate = getDirectoryForm(); //날짜
        File file = new File(getFullPath(storeFileName));

        multipartFile.transferTo(file);

        return new UploadFile(updateDate,originalFilename,storeFileName);
    }

    private String getDirectoryForm() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date today = new Date();
        return simpleDateFormat.format(today);
    }



    //서버에 저장할 파일명
    private String createdSoreFileName(String originalFilename) {
        String ext =  extractExt(originalFilename); //확장자
        //서버에 저장하는 파일명
        String uuid = UUID.randomUUID().toString();
        String storeFileName =uuid+"."+ext;
        return storeFileName;


    }

    //.png
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf("."); //.위치
        String ext = originalFilename.substring(pos + 1); // 확장자
        return ext;
    }

}
