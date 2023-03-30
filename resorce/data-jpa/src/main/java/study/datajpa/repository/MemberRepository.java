package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.SimpleTimeZone;

@Repository // 생략 가능
//jpa예외를 spirng 예외 변환하여 공통처리할 수 있게 만듦
//@Transactional 사용x - Spring Data JPA의 JpaRepository의 구현체를 사용하신다면 보시다시피 실제 구현체에 @Transactional이 붙습니다
public interface MemberRepository extends JpaRepository<Member,Long> ,MemberRepositoryCustom{
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findHelloBy();

    Member findHelloByUsername(String username);



    List<Member> findTop3HelloBy();

//    @Query(name = "Member.findByUsername")
    //1 . NamedQuert를 먼저 찾기에 없어도 상관없음 Member.findByUsername
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username")String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();


    //dto로 바로 바꾸어 재사용성 x
    @Query("select new study.datajpa.dto.MemberDto(m.id,m.username,t.name) from Member m join m.team t ")
    List<MemberDto> findMemberDto();

    @Query("select m from Member  m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);



    List<Member> findListByUsername(String username); //컬렉션 -빈값
    Member findMemberByUsername(String username); //단건 -null
    Optional<Member> findOptionalByUsername(String username); //단건 optional


    //@Query(value = "select m from Member m left join m.team t")

    //- 쿼리 분리(최적화)
    @Query(value = "select m from Member m",countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);


    @Modifying(clearAutomatically = true) //update문 실행 clearAutomatically = true - 영속성 콘텍스트 초기화(DB로 바로 반영 -> 1차캐시에 수정되지 않는 값 계속 남아있음)
    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);


    // ------------fetch join ----------------

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"}) //fetch join을 쓰는 거
    List<Member> findAll();


    //쿼리(JPQL) + fetch joins
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m where m.age = :age")
    List<Member> findMemberEntityGraphByAge(@Param("age") int age);


    //메서드 이름으로 쿼리에서 특히 편리하다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findHellodByUsername(String username);

    //@EntityGraph(attributePaths = {"team"}) //fetch join + where username
    @EntityGraph("Member.all")
    List<Member> findEntityGraphByUsername(@Param("username") String username);





    //read만할 경우
    //진짜 중요하고 진짜 트래픽이 많은것만
    //트래픽 : 서버와 스위치 등 네트워크 장치에서 일정 시간 내에 흐르는 데이터의 양
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly",value="true")) //성능 최적합 -> 스넵샷x(변경이 안된다고 가정)
    Member findReadOnlyByUsername(String username);

    //select for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);




}
