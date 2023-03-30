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
public class FileStore2 {

    @Value("${file.dir}")
    private String fileDir;


    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
            List<UploadFile> storeFileResult = new ArrayList<>();

            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) {
                    boolean th = false;
                    storeFileResult.add(storeFile(multipartFile,th));
                }
            }
            System.out.println("storeFileResult = " + storeFileResult);
        return storeFileResult;
    }

    //파일 업로드
    public UploadFile storeFile(MultipartFile multipartFile,Boolean th) throws IOException {
        if (multipartFile.isEmpty()) {
            System.out.println("값이 없다.");
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename(); //실제 파일명

        String servername = "";
        String updateDate = "";
        String filename = "";

        //서버저장
         updateDate = getDirectoryForm(); //날짜
         servername = createdSoreFileName(originalFilename);

        File uploadPaths = new File( fileDir,updateDate); //파일 저장
        System.out.println("file = " + uploadPaths);

        if (!uploadPaths.exists()) {
            uploadPaths.mkdirs();
        }

        if (th) { //썸네일일 경우
            originalFilename = multipartFile.getOriginalFilename(); //실제 파일명
            servername = "s_"+createdSoreFileName(originalFilename);

        }

        File file = new File( uploadPaths,servername); //파일 저장

        System.out.println("file = " + file);
        multipartFile.transferTo(file);


        return new UploadFile(originalFilename,servername,updateDate);

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
        System.out.println("storeFileName = " + storeFileName);
        return storeFileName;


    }

        //.png
        private String extractExt(String originalFilename) {
            int pos = originalFilename.lastIndexOf("."); //.위치
            String ext = originalFilename.substring(pos + 1); // 확장자
            return ext;
    }

}
