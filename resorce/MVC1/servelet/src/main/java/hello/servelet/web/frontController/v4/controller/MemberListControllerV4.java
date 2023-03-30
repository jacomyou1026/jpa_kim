package hello.servelet.web.frontController.v4.controller;

import hello.servelet.domain.member.Member;
import hello.servelet.domain.member.MemberRepository;
import hello.servelet.web.frontController.ModelView;
import hello.servelet.web.frontController.v3.ControllerV3;
import hello.servelet.web.frontController.v4.ControllerV4;

import java.util.List;
import java.util.Map;

public class MemberListControllerV4 implements ControllerV4 {
    MemberRepository memberRepository = MemberRepository.getInstance();
    
   @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        List<Member> members = memberRepository.findAll();

       model.put("members", members);

        return "members";
    }
}
