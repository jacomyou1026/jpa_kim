package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //더티체킹x, 읽기용모드로 읽어라 -> 성능이  더 좋아짐
@RequiredArgsConstructor
public class MemberService {

    private final MemberRespository memberRespository;
    
    //런타임 : 컴퓨터 프로그램이 실행되고 있는 동안의 동작

//    @Autowired // 런타임시 중간에 바꿀 수 있음
//    public void setMemberRespository(MemberRespository memberRespository) {
//        this.memberRespository = memberRespository;
//    }

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        valdiateDuplicateMember(member); //중복 회원 검증
        memberRespository.save(member);
        return member.getId();
    }
    //회원 중복체크
    private void valdiateDuplicateMember(Member member) {
        //Exception
        //문제 : WAS 여러개여서 두개 동시 실행시,문제 => 디비에 member.getName() unique제약조건 추가
        List<Member> findByMembers = memberRespository.findByName(member.getName());
        if (!findByMembers.isEmpty()) {
            throw new IllegalStateException("이미존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRespository.findAll();
    }
    
    public Member findOne(Long MemberId) {
        return memberRespository.findOne(MemberId);
    }

}
