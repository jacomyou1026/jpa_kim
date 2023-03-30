package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "TEAM_ID") //기본값 필드명_기본키
    private Long id;
    private String name;
    
//    //일 대 다
//    @OneToMany(mappedBy = "team") //읽기 전용
//    private List<Member> members =new ArrayList<>();


//
//
//    public List<Member> getMembers() {
//        return members;
//    }
//
//    public void setMembers(List<Member> members) {
//        this.members = members;
//    }

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


}
