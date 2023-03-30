package hello.core.member;

public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public void join(Member member) {
        memberRepository.save(member);
        System.out.println(member);

    }

    @Override
    public Member findMember(Long memberid) {
        return memberRepository.findById(memberid);
    }
}
