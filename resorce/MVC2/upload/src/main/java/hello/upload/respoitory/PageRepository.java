package hello.upload.respoitory;

import hello.upload.entity.board;
import hello.upload.entity.file;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Optional;

public interface PageRepository extends JpaRepository<file,String>{

    @Query("select  b from file b   where b.board.id = :bno")
    List<file> findByBno(@Param("bno") Integer bno);


    @Query("select f from file f where f.board.id =:bno")
    List<file> findByFile(@Param("bno") int bno);

}
