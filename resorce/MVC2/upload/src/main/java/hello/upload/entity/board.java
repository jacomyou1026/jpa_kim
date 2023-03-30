package hello.upload.entity;

import hello.upload.domain.Item2;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class board {

    @Id @GeneratedValue
    @Column(name = "bno")
    private Long id;
    private String filename;

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL)
    private List<file> files = new ArrayList<>();

    @Builder
    public board(String filename) {
        this.filename = filename;
    }

    //양방향 설정
    public void changefile(file file) {
        files.add(file);
        file.setBoard(this);

    }


}
