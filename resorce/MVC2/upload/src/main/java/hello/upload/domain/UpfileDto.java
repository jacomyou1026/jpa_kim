package hello.upload.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpfileDto {
    private String itemName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;
}
