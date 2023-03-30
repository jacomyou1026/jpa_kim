package hello.servelet.web.frontController.v4.controller;

import hello.servelet.domain.member.Member;
import hello.servelet.domain.member.MemberRepository;
import hello.servelet.web.frontController.ModelView;
import hello.servelet.web.frontController.v3.ControllerV3;
import hello.servelet.web.frontController.v4.ControllerV4;

import java.util.Map;

public class MemberSaveControllerV4 implements ControllerV4 {
    MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        model.put("member", member); //model

        return "save-result"; //viewName
    }
}
