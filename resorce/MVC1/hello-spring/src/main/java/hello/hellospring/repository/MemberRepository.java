package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member menber);
    Optional<Member> finaById(Long id);
    Optional<Member> findByName(String name);
//    Optional - null일수도 있어서 null을 그래로 반환하는대신
    List<Member> finalAll();




}
