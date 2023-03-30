package hello.upload.domain;


import hello.upload.entity.board;
import hello.upload.entity.file;
import lombok.Data;

@Data
public class  UploadFile {

    private String uploadFileName; //고객이 업로드하는 파일명
    private String storeFileName; // 서부 내부에서 관리하는 파일명
    private String updateDate; // 서부 내부에서 관리하는 파일명



    public UploadFile(String uploadFileName, String storeFileName, String updateDate) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.updateDate = updateDate;
    }


    public UploadFile(file o) {
        storeFileName = o.getStoreFileName();
        uploadFileName = o.getUploadFileName();
        updateDate = o.getUpload_Path();
    }




}
