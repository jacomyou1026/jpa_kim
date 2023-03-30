package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

//디비접근
//test바로만들기 단축키 - cmd+shift+t : crate new test
public class MemberMemberRepository implements MemberRepository
{
    private static Map<Long,Member> store = new HashMap<>();
    private static long sequence = 0L;
    @Override
    public Member save(Member menber) {
        menber.setId(++sequence);
        store.put(menber.getId(), menber);
        return menber;
    }

    @Override
    public Optional<Member> finaById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream().
                filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> finalAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }
}



