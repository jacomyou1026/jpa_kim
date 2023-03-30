package hello.upload.service;

import hello.upload.domain.BoardRepository;
import hello.upload.domain.Item2;
import hello.upload.domain.UploadFile;
import hello.upload.entity.board;
import hello.upload.entity.file;
import hello.upload.respoitory.PageRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j

public class BoardService {
    private final BoardRepository boardRepository;
    private final PageRepository pageRepository;


    public board insert(Item2 item2) {
        board board = new board(item2.getItemName());

        List<UploadFile> imageFiles = item2.getImageFiles();
        for (UploadFile imageFile : imageFiles) {
            if (imageFile == null) {
                continue;
            }

            file file2 = new file(imageFile);
            board.changefile(file2); //연관매핑
        }


        hello.upload.entity.board boardsave = boardRepository.save(board);
        log.info("boardsave=  {}", boardsave);




        return boardsave;

    }

    public List<file> findByBno(Integer bno) {
        return pageRepository.findByBno(bno);
    }


    //파일 삭제
    public void deltefile(String filename) {
        pageRepository.deleteById(filename);

    }


    //게시글 삭제
    public void deleteByBno(int bno) {
        boardRepository.deleteById(bno);
    }

    //파일 찾기
    public List<file> findByfile(int bno) {
        return pageRepository.findByFile(bno);
    }
    

    //파일 수정
    public String update(Item2 item) throws IllegalArgumentException {
        System.out.println("item.getId() = " + item.getId());
        Long id = item.getId();
        //게시글 update
        board board = boardRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        board.builder()
                .filename(item.getItemName())
                .build();

        //파일 update

        List<file> byFile = pageRepository.findByFile(Math.toIntExact(item.getId()));
        for (file file : byFile) {
            deleteByBno(Math.toIntExact(file.getBoard().getId()));
        }

        List<UploadFile> imageFiles = item.getImageFiles();
        for (UploadFile imageFile : imageFiles) {
            if (imageFile == null) {
                continue;
            }
            file file2 = new file(imageFile);
            board.changefile(file2); //연관매핑
        }

        return "수정";
    }
}
