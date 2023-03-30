package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {
    @Id //primary key
    @GeneratedValue
    private Long id;

    private String name;

// 언제 사용 ?  - 하나의부모가 자식을 관리 (게시판- 첨부파일) : 소유자가 하나일 경우
    //파일이 여러 Entity에서 관리할 시 사용x
    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        childList.add(child);
        child.setParent(this);
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }
}
