package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.*;

@Slf4j
@Repository
public class MemberRepository {
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    public Member save(Member member){
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id){
        return store.get(id);
    }

    //람다/ 스트림 공부
    public Optional<Member> findByLoginId(String loginId){
//        List<Member> all = findAll();
//        for (Member m : all) {
//            if(m.getLoginId().equals(loginId)){
//                return Optional.of(m);
//            }
//        }
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
//        return Optional.empty();

    }
    public void clearStore(){
        store.clear();
    }

    public List<Member> findAll(){
        System.out.println("store = " + store);
        return new ArrayList<>(store.values());
    }






}
