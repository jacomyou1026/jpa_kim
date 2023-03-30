package hello.upload.controller;

import hello.upload.domain.Item2;
import hello.upload.domain.UpfileDto;
import hello.upload.domain.UploadFile;
import hello.upload.entity.board;
import hello.upload.entity.file;
import hello.upload.file.FileStore2;
import hello.upload.service.BoardService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@Slf4j
public class Item2Controller {

    private final FileStore2 fileStore2;
    private final BoardService service;

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/items/new2")
    public String newItem(@ModelAttribute ItemForm form) {
        return "item-form";
    }


    @PostMapping("/items/new2")
    public String SaveItem(ItemForm form, RedirectAttributes redirectAttributes) throws IOException {

        UploadFile attachFile = fileStore2.storeFile(form.getAttachFile(), true);
        List<UploadFile> files = fileStore2.storeFiles(form.getImageFiles());
        log.info("그 외 파일들 : {}", files);



        Item2 item2 = new Item2();
        item2.setItemName(form.getItemName());
        files.add(attachFile);
        item2.setImageFiles(files);


        board boardsave = service.insert(item2);


        redirectAttributes.addAttribute("id", boardsave.getId());
        return "redirect:/items/{id}";

    }

        @GetMapping("/items/{bno}")
        @ResponseBody
        public UpfileDto ItemOne(@PathVariable Integer bno, Model model) {
            System.out.println("bno = " + bno);

            //파일.g
            List<file> files = service.findByBno(bno);
            List<UploadFile> result = files.stream().map(o -> new UploadFile(o)).collect(Collectors.toList());


            UpfileDto upfileDto = new UpfileDto();
            List list = new ArrayList();

        for (UploadFile file : result) {
            if (file.getStoreFileName().startsWith("s_")) {
                upfileDto.setAttachFile(file);
                continue;
            }
            if (file != null) {
                list.add(file);
            }
        }

        if(!list.isEmpty()){
            upfileDto.setImageFiles(list);
        }

        upfileDto.setItemName(files.get(0).getBoard().getFilename());

        return upfileDto;
    }


    @GetMapping("/image")
    @ResponseBody
    public ResponseEntity<byte[]> display(@RequestParam String storeFileName,@RequestParam String updateDate ) throws IOException {
        //파일전체경로
        String filename = updateDate + "/" + storeFileName;
        File file = new File(fileDir + filename);

        ResponseEntity<byte[]> result = null; //byte로 이미지를 가져옴
        HttpHeaders headers = new HttpHeaders();
        log.info("file.toPath() : {}", file.toPath());
        headers.add("Content-Type", Files.probeContentType(file.toPath()));
        result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
        return result;
    }

    @PutMapping("/change/{bno}")
    @ResponseBody
    public void change(@PathVariable int bno, ItemForm form) throws IOException {

        //bno로 먼저 기존 파일 있는지 확인
        List<file> findfiles = service.findByfile(bno);
        if (findfiles != null) {
            //파일이 존재하면 삭제
            for (file findfile : findfiles) {
                String uploadPath = findfile.getUpload_Path();
                String storeFileName = findfile.getStoreFileName();//서버 저장
                String filename = uploadPath + "/" + storeFileName;
                File file = new File(fileDir + URLDecoder.decode(filename, "UTF-8"));
                file.delete();
            }
        }

        
        //파일및 수정 업로드
        UploadFile attachFile = fileStore2.storeFile(form.getAttachFile(), true);
        List<UploadFile> files = fileStore2.storeFiles(form.getImageFiles());

        log.info("그 외 파일들 : {}", files);

        Item2 item2 = new Item2();
        item2.setItemName(form.getItemName());
        files.add(attachFile);
        item2.setImageFiles(files);

          service.update(item2);




    }

    //파일만 삭제(게시글 삭제x) -requestParam
    @DeleteMapping("/deleteFiles")
    @ResponseBody
    public boolean deleteFiles(String storefilename,String DatePath) throws UnsupportedEncodingException {
        boolean result = false;
        //파일 삭제
        File file = new File(fileDir + URLDecoder.decode(DatePath+"/"+storefilename, "UTF-8"));

        //파일이 존재하면 삭제
        if (file.isFile()) {
            file.delete();
            result = true;
        }

        System.out.println("storefilename = " + storefilename);
        //DB에서 삭제
        service.deltefile(storefilename);
        return result;
    }

    //게시글 삭제 ( 파일, 게시글 다 삭제) - date+파일명
    //{id} =게시글 id(item id)
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String  deleteItem(@PathVariable int id) {
        System.out.println("id = " + id);
        //게시글 삭제
        service.deleteByBno(id);

        //파일들 없애기
        //deleteFiles();
        return "true";
    }
}
