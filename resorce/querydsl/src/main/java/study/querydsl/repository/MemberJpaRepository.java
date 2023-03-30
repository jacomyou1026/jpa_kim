package study.querydsl.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.*;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@Repository
public class MemberJpaRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MemberJpaRepository(EntityManager em) {
        this.em = em;
        this.queryFactory =new JPAQueryFactory(em);
    }

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findById(Long id) {
        Member findMember = em.find(Member.class, id);
        return Optional.ofNullable(findMember);
    }

    //고객엑
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //컴파일 오류
    public List<Member> findAll_querydsl() {
        return queryFactory
                .selectFrom(member)
                .fetch();
    }



    public List<Member> findByusername(String username) {
        return em.createQuery("select m from Member m where m.username =:username", Member.class)
                .setParameter("username",username)
                .getResultList();
    }

    public List<Member> findByusername_querydsl(String username) {
        return queryFactory
                .selectFrom(member)
                .where(member.username.eq(username))
                .fetch();
    }

    /**
     *
     * null이면 그냥 where문 무시
     *
     *
     lt <
     loe <=
     gt >
     goe >=
     */


    public List<MemberTeamDto> serachByBuider(MemberSearchCondition condition) {

        BooleanBuilder builder = new BooleanBuilder();
        if (hasText(condition.getUsername())) {
            builder.and(member.username.eq(condition.getUsername()));
        }
        if (hasText(condition.getTeamName())) {
            builder.and(team.name.eq(condition.getTeamName()));
        }
        if (condition.getAgeGoe() != null) {
            builder.and(member.age.goe(condition.getAgeGoe()));
        }
        if (condition.getAgeLoe() != null) {///<=
            builder.and(member.age.loe(condition.getAgeLoe()));
        }



        return queryFactory
                .select(new QMemberTeamDto(member.id.as("memberId")
                        ,member.username,
                        member.age
                        ,team.id.as("teamId"),team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(builder)
                .fetch();
    }


    public List<MemberTeamDto> search(MemberSearchCondition condition) {
        return queryFactory
                .select(new QMemberTeamDto(member.id.as("memberId")
                        ,member.username,
                        member.age
                        ,team.id.as("teamId"),team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageBetween(condition.getAgeLoe(),condition.getAgeGoe())

                )
                .fetch();
    }

        public List<Member> searchMember(MemberSearchCondition condition) {
        return queryFactory.selectFrom(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageBetween(condition.getAgeLoe(),condition.getAgeGoe())
                )
                .fetch();
    }

    //null체크만 조심해야함
    private BooleanExpression ageBetween(int ageLoe, int ageGoe) {
        return ageLoe(ageLoe).and(ageGoe(ageGoe));

    }
    
    //재사용 가능
    private BooleanExpression usernameEq(String username) {
        return hasText(username)?member.username.eq(username):null;
    }
    private BooleanExpression teamNameEq(String teamName) {
        return hasText(teamName)?member.team.name.eq(teamName):null;
    }
    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe!=null?member.age.goe(ageGoe):null;
    }
    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe):null;

    }








}
