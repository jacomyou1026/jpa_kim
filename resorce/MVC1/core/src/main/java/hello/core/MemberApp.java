package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;

public class MemberApp {
    public static void main(String[] args) {
        MemberService memberservice = new MemberServiceImpl();

//      //ctrl+alt+v : 생성자+변수 자동생성
        Member member = new Member(1L, "member", Grade.VIP);
        memberservice.join(member);

        Member findMember = memberservice.findMember(1L);
        System.out.println("new findMember="+findMember.getName());
        System.out.println("find Member="+findMember.getName());



    }

}
