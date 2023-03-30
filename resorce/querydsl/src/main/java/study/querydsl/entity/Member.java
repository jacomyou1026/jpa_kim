package study.querydsl.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //무분별한 객체 생성대해 한번 더 체크
@ToString(of = {"id","username","age"}) //team출력x -> 무한루프될 수 있음
@NamedQuery(
        name = "Member.findByUsername",
        query="select m from Member m where m.username =:username"
)
@NamedEntityGraph(name = "Member.all",attributeNodes = @NamedAttributeNode("team")) //fetch join
public class Member{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩
    @JoinColumn(name = "team_id")
    private Team team;

//    //jpa프록시 기술 -> private일시 막혀버리
//    protected Member() {

//    }


    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }


    }

    //setTeam 양방향 한번에
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}


