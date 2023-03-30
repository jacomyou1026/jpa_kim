package hello.upload.respoitory;

import hello.upload.entity.board;
import hello.upload.entity.file;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PageRepositoryTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    PageRepository pageRepository;

    @Test
    public void findByBno() {
        board board1 = new board("board1");
        board board2 = new board("board1");


        file file1 = new file("sdsd","sdf","dsfsf",board1);
        file file2 = new file("sdsd`","sdf","dsfsf",board1);
        file file3 = new file("sdsd1","sdf","dsfsf",board1);
        file file4 = new file("sdsd23","sdf","dsfsf",board1);

        em.persist(board1);
        em.persist(file1);
        em.persist(file2);
        em.persist(file3);
        em.persist(file4);

        em.flush();
        em.clear();

        List<file> byBno = pageRepository.findByBno(1);
        for (file file : byBno) {
            System.out.println("file = " + file);
        }

        pageRepository.deleteByBoard(1);

    }

}