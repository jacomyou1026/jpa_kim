package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.CodePointLength;
import org.springframework.boot.autoconfigure.data.ConditionalOnRepositoryType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    /**
     * 화면에 fit한 formDATA에 만들어 -> Entity->DB
     * 
     * 요구사항이 간단할시 entity로 바로 사용
     * 실무에선 요구사항이 단순x
     *
     * 단점
     * - entity를 form으로 사용시, 화면 종속적인 기능이  생겨버림
     *  => 화면기능때문에 entity가 지저분해짐
     *  =>유지보수 어려워짐
     *
     * Entity를 최대한 순수하게 유지
     *   => 유지보수,
     */

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        //Member Entity를 그대로 뿌리기 보단 DTO로 변환을 해서 화면에 출력
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "/members/memberList";
    }
}
