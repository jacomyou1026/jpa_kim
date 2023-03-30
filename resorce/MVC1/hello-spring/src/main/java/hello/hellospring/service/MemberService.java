package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

//repository접근
public class MemberService {
    private final MemberRepository memberMemberRepository ;
//    DI
    public MemberService(MemberRepository memberMemberRepository) {
        this.memberMemberRepository = memberMemberRepository;
    }

    //    회원가입
    public Long join(Member member) {
//        같은 이름이 있는 중복 회원x
//        null일 가능성 이 있을 시,Optional 사용
//        Optional<Member> 단축키 :cmd+option+v
//        Optional<Member>esult = memberMemberRepository.findByName(member.getName());
//        중복 회원 검증
        ValidateDuplicateMember(member);
        memberMemberRepository.save(member);
        return member.getId();
    }

    //    중복회원 겁증
    private void ValidateDuplicateMember(Member member) {
        memberMemberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");});
                }

//    전체 회원 조회
        public List<Member> findMembers () {
            return memberMemberRepository.finalAll();
        }

        public Optional<Member> findOne (Long memberId){
            return memberMemberRepository.finaById(memberId);
        }


    }
