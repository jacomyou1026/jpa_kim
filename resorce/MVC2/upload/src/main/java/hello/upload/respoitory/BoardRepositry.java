package hello.upload.respoitory;

import hello.upload.entity.board;
import hello.upload.entity.file;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepositry extends JpaRepository<board,Integer> {
}
