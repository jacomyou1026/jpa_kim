package hello.upload.domain;

import hello.upload.entity.board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<board,Integer> {
}
