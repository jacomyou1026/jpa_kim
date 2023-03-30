package hello.upload.entity;

import hello.upload.domain.UploadFile;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class file {

    @Id
    private String storeFileName; //서버저장명
    private String uploadFileName;//원본
    private String Upload_Path; //날자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="bno")
    private board board;


    public file(String uploadFileName, String storeFileName, String upload_Path) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        Upload_Path = upload_Path;
    }

    @Builder
    public file(UploadFile uploadFile) {
        this.storeFileName= uploadFile.getStoreFileName();
        this.uploadFileName = uploadFile.getUploadFileName();
        this.Upload_Path = uploadFile.getUpdateDate();
    }
}
