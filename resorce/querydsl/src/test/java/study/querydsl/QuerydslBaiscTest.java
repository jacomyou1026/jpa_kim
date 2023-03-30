package study.querydsl;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import java.util.Arrays;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.InstanceOfAssertFactories.ARRAY;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional
public class QuerydslBaiscTest {

    @Autowired
    EntityManager em;
    //스프링 프레임워크는 여러 쓰레드에서 동시에 같은
    //EntityManager에 접근해도, 트랜잭션 마다 별도의 영속성 컨텍스트를 제공하기 때문에, 동시성 문제는
    //걱정하지 않아도 된다.
    JPAQueryFactory queryFactory; ;

    //실행전 데이터 insert
    @BeforeEach
    public void before() {
        queryFactory= new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);
        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",20,teamA);
        Member member3 = new Member("member3",30,teamB);
        Member member4 = new Member("member4",40,teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

    }

    @Test
    public void startJPQL() {
        //member1을 찾아라
        Member findMember=em.createQuery("select m from Member m where m.username =:username", Member.class)
                .setParameter("username", "member1") //파라미터 바인딩 직접
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void startQuerydsl() {
        //JPAQueryFactory queryFactory = new JPAQueryFactory(em);


        //QMember m = QMembemr.member;
        
        
        //member1을 찾아라.
        QMember m1 = new QMember("m1");
        //컴파일 시점에 오류 잡아줌
        Member member1 = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1")) ////파라미터 바인딩 자동
                .fetchOne();
        System.out.println("member1 = " + member1);

    }

    @Test
    public void search() {
        List<Integer> ids = Arrays.asList(10, 20);
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(ids))
                .fetch();

        assertThat(result.size()).isEqualTo(2);


    }

    //김영한 선호
    @Test
    public void searchAndParam() {
        Member findMember = queryFactory
                //select(member) from (member)
                .selectFrom(member)
                .where(
                        member.username.eq("member 1"),
                        member.age.eq(10)
                )
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void resultFetch() {
//        //List
//        List<Member> fetch = queryFactory
//                .selectFrom(member)
//                .fetch();
//
//        //단건
//        //NonUniqueResultException : 결과가 둘 이상일 경우
//        Member fetchOne = queryFactory
//                .selectFrom(member)
//                .fetchOne();
//
//        //처음 한 건 조회
//        Member fetchFirst = queryFactory
//                .selectFrom(member)
//                .fetchFirst();

        //페이징에서 사용
        //cotent가져오는 쿼리와 totalcount를 가져오는 쿼리가 다를 때가 있다. 성능때문에
        //복잡한 성능이 중요한 쿼리일때 사용x -> 쿼리 2개를 따로 사용
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();

        //쿼리 두번 실행 - total count를 실행해야 하기에
         results.getTotal(); //count
        List<Member> content = results.getResults(); // content

        //count 쿼리로 변경
        long count = queryFactory
                .selectFrom(member)
                .fetchCount();
        System.out.println("count = " + count);

    }

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 올림차순(asc)
     * 단 2에서 회원 이름이 없으면 마지막에 출력 (nulls last)
     */
    @Test
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();


    }

    @Test
    public void paging1() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1) //0부터 시작
                .limit(2) //최대 2건 조회
                .fetch();

        assertThat(result.size()).isEqualTo(2);

    }

    @Test
    public void paging2() {
        //전체 조회수 필요
        //쿼리 2번실행(count, content쿼리) -실무에서는 단순하면 이렇게 사용
        //join/where을 붙을경우 성능상 불리 -> 각각 분리하여 작업
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1) //0부터 시작
                .limit(2)
                .fetchResults();

        assertThat(results.getTotal()).isEqualTo(4);
        assertThat(results.getLimit()).isEqualTo(2);
        assertThat(results.getOffset()).isEqualTo(1);
        assertThat(results.getResults().size()).isEqualTo(2);

    }

    @Test
    public void aggregation() {
        //Tuple : 데이터 타입 여러개 조회
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                ).from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);


    }


    /**
     * 팀의 이름과 각팀의 연령 구해라
     */
    @Test
    public void group() throws Exception{
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                //member의 team과 team을 조인해준다.
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();


        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);


        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15); //(10+20) /2

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35); //(30+40)/2 teamB의 평균 연령

    }

    /**
     * 팀 A에 소속된 모든 회원
     */

    @Test
    public void join() {
        List<Member> teamA = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(teamA).extracting("username")
                .containsExactly("member1", "member2");
    }

    /**
     * 세타 조인
     * 회원의 이름이 팀이름과 같은 회원조회
     */
    @Test
    public void theta_join() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        //모든 Member table 과 모든 team table 조인을 한 후,
        // 조인한 table에서 where조건문 실행
        
        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result).extracting("username")
                .containsExactly("teamA", "teamB");


    }


    /**
     * 예) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원 모두 조회
     * JPQL: select m,t  from Member m left join m.team t on t.name ='teamA'
     * ID로 조인
     */
    @Test
    public void join_on_filtering() {
        
//                .join(member.team, team) : id 매칭
        
        //Tuple : select가 여러가지 타입이 나와서
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .join(member.team, team)
                .on(team.name.eq("teamA"), member.age.eq(10))
                //.where(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }


    }

    /**
     * 연관관계 없는 엔티티를 외부 조인
     * 회원 이름과 팀의 이름이 같음 대상 외부 조인
     *  jpql : select m,t Member m left join Team t on m.username = t.name
     * DB조인
     */
    @Test
    public void join_on_no_relation() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

//join 조건으로 필터링
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team)
                .on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void fetchJoinNo() {
        em.flush();
        em.clear();

        Member member1 = queryFactory
                .selectFrom(member)
                .join(member.team,team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(member1.getTeam());
        assertThat(loaded).as("패치 조인 미적용").isTrue();
    }

    /**
     * 나이가 가장 많은 회원 조회
     */
    @Test
    public void subQuery() {
        QMember memberSub = new QMember("memberSub");
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max())
                                .from(memberSub)
                )).fetch();


        assertThat(result).extracting("age")
                .containsExactly(40);

    }


    /**
     *나이가 평균 이상인 회원
     */

    @Test
    public void subQueryGoe() {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe( //>=
                        select(memberSub.age.avg())
                                .from(memberSub)
                )).fetch();


        assertThat(result).extracting("age")
                .containsExactly(30,40);

    }


    /**
     *나이가 10살 이상인 회원
     */

    @Test
    public void subQueryIn() {
        QMember memberSub = new QMember("memberSub");
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                )).fetch();


        assertThat(result).extracting("age")
                .containsExactly(20,30,40);

    }

    /**
     *단점
     *  from절에 subQuery안됨
     *
     */

    @Test
    public void selectSubquery() throws Exception{
        QMember memberSub = new QMember("memberSub");

    //given
        List<Tuple> fetch = queryFactory
                .select(member.username,
                        select(memberSub.age.avg())
                                .from(memberSub)
                ).from(member)
                .fetch();

    //then
        for (Tuple tuple : fetch) {
            System.out.println(tuple);

        }

    }

    @Test
    public void basicCase() {
        List<String> fetch = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("가타"))
                .from(member)
                .fetch();
        for (String s : fetch) {
            System.out.println("s = " + s);

        }

    }

    //DB에서 안하면 좋음
    @Test
    public void complexCase() {
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타")
                ).from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void rankpath() throws Exception{
    //given
        NumberExpression<Integer> rankPath = new CaseBuilder()
                .when(member.age.between(0, 20)).then(2)
                .when(member.age.between(21, 30)).then(1)
                .otherwise(3);

        List<Tuple> fetch = queryFactory
                .select(member.username, member.age, rankPath)
                .from(member)
                .orderBy(rankPath.desc())
                .fetch();

        for (Tuple tuple : fetch) {
            String username = tuple.get(member.username);
            System.out.println("username = " + username);
            Integer age = tuple.get(member.age);
            System.out.println("age = " + age);
            Integer rank = tuple.get(rankPath);
            System.out.println("rank = " + rank);
            System.out.println("-------------------");
        }

    //then
    }

    @Test
    public void constant() {
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A")) //상수 A를 넣어서 가지고 옴
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

    }

    /**
     * 문자 더하기
     * enum처리 시 사용 - stringValue() -문자변환
     */

    @Test
    public void concat() throws Exception{
    //given
        //{username}_{age} : member_10
        //age - 숫자 -> 문자변환
        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
        
    }


    @Test
    public void simpleProjection() throws Exception{
    //given
        List<Member> result =  queryFactory
                .select(member)
                .from(member)
                .fetch();

        System.out.println("result = " + result);
    }

    /**
     *
     *  Tuple을 repository안에서만 사용하고
     *  DTO로 바꾸어서 사용 권장
     */
    @Test
    public void tupludProject() throws Exception{
    //given
        List<Tuple>  result= queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);

        }

    }
    
    @Test
    public void findDtoJPQL() throws Exception{
    //given
        List<MemberDto> result = em.createQuery("select new study.querydsl.dto.MemberDto(m.username,m.age)  from Member m", MemberDto.class)
                .getResultList();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    /**
     *
     * Getter,Setter있어야함
     * Getter/setter로 이용한 dto 조회
     */

    @Test
    public void findDtoBySetter() throws Exception{
    //given
        List<MemberDto>  result= queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

    }

    /**
     *
     * Getter,Setter없어도 됨
     * 바로 필드로 값 주입
     */

    @Test
    public void findDtoByField() throws Exception{
        //given
        List<MemberDto>  result= queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

    }

    /**
     * 생성자 접근 방법
     * 타입을 맞춰야함
     * 단점
     *      * 오류 : 실행이 되나 , runtime오류 ->실행하는 순간 오류를 알수 있다 /
     *      코드가 길어질시 불편함
     */

    @Test
    public void findDtoByConstructor() throws Exception{
        //given
        List<MemberDto>  result= queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

    }

    /**
     * Projections.fields
     * ExpressionUtils.as : 서브쿼리사용시
     *
     *
     */

    @Test
    public void findUserDto() throws Exception{
        QMember memberSub = new QMember("memberSub");
        //given
        List<UserDto>  result= queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        ExpressionUtils.as(JPAExpressions
                                .select(memberSub.age.max()).from(memberSub),"age"
                        )))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
    }

    /**
     * 
     * 다른 타입이 추가로 오면 바로 오류나옴
     * 
     * 단점
     *  1. QFile 생성
     *  2.Dto가 querydsl어노테이션 의존
     */

    @Test
    public void findDtoQueryProjection() throws Exception{

        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    /**
     * 동적쿼리 해결
     * BooleanBuilder
     */

    @Test
    public void dynamicQuery_BooleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = null;

        List<Member> result =  searchMember1(usernameParam, ageParam);
        Assertions.assertThat(result.size()).isEqualTo(1);

    }

    private List<Member> searchMember1(String usernameCond, Integer ageCond) {

        //초기값을 넣을 수 있다
        BooleanBuilder builder = new BooleanBuilder(member.username.eq(usernameCond));
        if (usernameCond != null) {
            builder.and(member.username.eq(usernameCond));
        }

        if (ageCond != null) {
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    /**
     * 동적 쿼리2
     * @throws Exception
     *
     * where가 null이면 무시됨
     * 가독성
     * 재활용 가능
     *
     * BooleanExpression
     * - null 일때 무시될 수 있고, and또는 or절을 통해서 조합을 할 수 있다.
     * Predicate
     * - 단순 true or false 조건문 정도에 사용하는 걸로 생각
     */

    @Test
    public void dynamicQuery_WhereParam() throws Exception{
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result =  searchMember2(usernameParam, ageParam);
        Assertions.assertThat(result.size()).isEqualTo(1);

    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {

        return queryFactory
                .selectFrom(member)
//                .where(usenameEq(usernameCond),ageEq(ageCond))
                .where(allEq(usernameCond,ageCond))
                .fetch();
    }

    // 간단할 경우 삼항 연산자 사용
    private BooleanExpression usenameEq(String usernameCond) {
        return usernameCond != null ?  member.username.eq(usernameCond):null ;
    }
    private BooleanExpression ageEq(Integer ageCond) {
        return ageCond != null ? member.age.eq(ageCond):null;
    }

    //광고 상태 isValid, 날짜 IN : isServicable

    //조립가능
    //재활용 가능. 쿼리 자체 가독성 좋음
    private BooleanExpression allEq(String usernameCond, Integer ageCond) {
         return usenameEq(usernameCond).and(ageEq(ageCond));

    }

    /**
     * 벌크 연산
     * 
     * 우선권
     *-영속성 컨텍스트 >  DB
     */

    @Test
    public void bulkUpdate() {

        //member1 =10 ->비회원
        //member2 =20 ->비회원
        //member3 =30 ->회원

        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();

        em.flush();
        em.clear(); //영속성 초기화

        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();

        for (Member member1 : result) {
            System.out.println("member1 = " + member1);
        }

    }

    @Test
    @Commit
    public void bulkAdd() {
        List<Member> fetch = queryFactory.selectFrom(member).fetch();
        for (Member fetch1 : fetch) {
            System.out.println("fetch1 = " + fetch1);
        }



        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
                .execute();

        em.flush();
        em.clear();
        System.out.println("count = " + count);
        List<Member> fetch2 = queryFactory.selectFrom(member).fetch();
        for (Member member1 : fetch2) {
            System.out.println("member1 = " + member1);
        }


    }

    @Test
    public void bulkDelete() {
        long execute = queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();
    }


    /**
     * member.username에서  member -> M
     */
    @Test
    public void sqlFunction() {
        List<String> result = queryFactory
                .select(
                        Expressions.stringTemplate(
                                "function('replace',{0}, {1}, {2})",
                                member.username, "member", "M")
                ).from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }

    }

    @Test
    public void sqlFunction2() throws Exception{
    //given

        List<String> fetch = queryFactory
                .select(member.username)
                .from(member)
//                .where(member.username.eq(
//                        //Expressions.stringTemplate("function('lower', {0})", member.username)))
//
                .where(
                        member.username.eq(member.username.lower())
                )
                .fetch();
        for (String s : fetch) {
            System.out.println("s = " + s);

        }


    }

}
