//package hello.upload.controller;
//
//import hello.upload.domain.Item;
//import hello.upload.domain.ItemRespository;
//import hello.upload.domain.UploadFile;
//import hello.upload.file.FileStore;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.tomcat.util.buf.UriUtil;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import org.springframework.web.util.UriUtils;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//@Slf4j
//@Controller
//@RequiredArgsConstructor
//public class ItemController {
//
//    private final ItemRespository itemRespository;
//    private final FileStore fileStore;
//
//    @GetMapping("/items/new")
//    public String newItem(@ModelAttribute ItemForm form) {
//        return "board-form";
//    }
//
//    @PostMapping("/items/new")
//    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
//
//        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
//        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());
//
//        //데이터베이스 저장
//        Item board = new Item();
//        board.setItemName(form.getItemName());
//        board.setAttachFile(attachFile);
//        board.setImageFiles(storeImageFiles);
//        itemRespository.save(board);
//
//        redirectAttributes.addAttribute("itemId", board.getId());
//        return "redirect:/items/{itemId}";
//
//    }
//
//    @GetMapping("/items/{id}")
//    public String items(@PathVariable Long id, Model model) {
//        Item board = itemRespository.findById(id);
//        model.addAttribute("board", board);
//        return "board-view";
//    }
//
//    //이미지 접근
//    @ResponseBody
//    @GetMapping("/images/{filename}")
//    public Resource downloadImag(@PathVariable String filename) throws MalformedURLException {
//        //file: - 내부 파일 접근
//        //file:/users/,,/.png
//
//        return new UrlResource("file:"+fileStore.getFullPath(filename));
//    }
//
//    //이미지 다운
//    @GetMapping("/attach/{itemId}")
//    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
//        Item board = itemRespository.findById(itemId);
//        String storeFileName = board.getAttachFile().getStoreFileName(); //서버
//        String uploadFileName = board.getAttachFile().getUploadFileName(); //고객
//
//        //인코딩 - 한글. 특수문자 깨짐주의
//        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
//
//        UrlResource urlResource=  new UrlResource("file:"+fileStore.getFullPath(storeFileName)); //서버 저장
//
//        log.info("uploadFileName ={} ", uploadFileName);
//        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
//                .body(urlResource);
//
//
//    }
//
//}
