package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepositroy teamRepositroy;

    @PersistenceContext
    EntityManager em;


    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savemember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savemember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);

    }
    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member1");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        findMember1.setUsername("nmmebrere!");


        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);



        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        //카운트 검증
        long deletcount = memberRepository.count();
        assertThat(count).isEqualTo(0);

    }


    @Test
    public void findByAndGreter() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result =memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);

    }

    @Test
    public void findHelloBy() {
        List<Member> top3HelloBy = memberRepository.findTop3HelloBy();
        for (Member member : top3HelloBy) {
            System.out.println("해수"+member.getUsername());
        }
    }

    @Test
    public void testNamedQuery() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result =memberRepository.findByUsername("AAA");
        Member findMember= result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        System.out.println("result = " + result.get(0));
        assertThat(result.get(0)).isEqualTo(m1);

    }



    @Test
    public void findusernameList() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }


    @Test
    public void FindMemberDto() {

        Team team = new Team("teamA");
        teamRepositroy.save(team);
        System.out.println("team = " + team+"이 save");

        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);
        m1.changeTeam(team); //양방향 setting


        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }

    }



    @Test
    public void findByNames() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }



    }


    @Test
    public void findByUSername() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);
        Member member = memberRepository.findHelloByUsername("AAA");
        System.out.println("member.getUsername() = " + member.getUsername());


    }

    @Test
    public void returnType() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

       // List<Member> aaa = memberRepository.findListByUsername("dfsdfds"); -> 빈 컬렉션 반환
        Member findMember0 = memberRepository.findMemberByUsername("sdf");// null
        Optional<Member> findMember = memberRepository.findOptionalByUsername("AAA");
        System.out.println("findMember = " + findMember.get());


    }

    @Test
    public void paging() {
        //shift +f6 : 한꺼번에 변하기

        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;

        //0부터 시작
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        
        //where절이 많을 경우 -> count 별도 조회 -> 성능 최적화
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //Entity -> DTO
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));



        //then
        List<Member> content = page.getContent();

        //long totalElements = page.getTotalElements();
        for (Member member : content) {
            System.out.println("member = " + member);
        }
        //  System.out.println("totalElements = " + totalElements);
        assertThat(content.size()).isEqualTo(3);
        //assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();//첫번째 페이지 인가?
        //assertThat(page.getTotalPages()).isEqualTo(2); //전체 페이지 개수
        assertThat(page.hasNext()).isTrue(); //다음 페이지가 있냐

        System.out.println(page.getPageable());
        System.out.println("다음페이지"+page.nextPageable());


        int pageNumber = page.getPageable().previousOrFirst().getPageNumber();
        int next = page.getPageable().next().getPageNumber();
        System.out.println("pageNumber = " + pageNumber);
        System.out.println("next = " + next);

        boolean hasNext = page.hasNext();
        System.out.println("hasNext = " + hasNext);

        boolean hasPrevious = page.hasPrevious();
        System.out.println("hasPrevious = " + hasPrevious);


    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 30));

        //when
        int resultCount =  memberRepository.bulkAgePlus(20);
//        em.flush();
//        em.clear();


        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);
        

        //then
        assertThat(resultCount).isEqualTo(3);


    }

    @Test
    public void findMemberLazy() {
        //given
        //member1 -> teamA
        //member2 ->teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        Team teamC = new Team("teamC");

        teamRepositroy.save(teamA);
        teamRepositroy.save(teamB);
        teamRepositroy.save(teamC);

        Member member1 = new Member("member1",10, teamA);
        Member member3 = new Member("member1", 20, teamC);
        Member member2 = new Member("member1", 10, teamB);
        Member member4 = new Member("member4", 10, teamB);


        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        em.flush();
        em.clear();

        //when N(2) +1 -> N : 추가 쿼리
        //select Member
        //List<Member> members = memberRepository.findEntityGraphByUsername("member1");
        //member만 team은 프록시라는 가짜객체를 만들어 team을 호출 시, 실제 디비에서 가져옴 (team)
        //List<Member> members = memberRepository.findMemberEntityGraphByAge(10);
        List<Member> members = memberRepository.findHellodByUsername("member1");

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass()); //
            System.out.println("member = " + member.getTeam().getName());
        }

    }

    @Test
    public void queryHint() {

        //given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();
        
        //when
        Member findMember1 = memberRepository.findById(member1.getId()).get();//실무에선 .get()바로 사용 x
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush(); //변경 감지 :원본이 있어 => 메모리 낭비


    }

    @Test
    public void lock() {

        //given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        //when
        List<Member> result = memberRepository.findLockByUsername("member1");
        //for update
    }

    @Test
    public void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();

    }






}