package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    //엔티티 직접 반환x

    @GetMapping("/api/v1/members")
    public List<Member> memberV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect =  findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        //map() : 스트림 내의 요소들을 하나씩 특정 값으로 변환해준다.

        return new Result(collect.size(),collect);
    }


    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long join = memberService.join(member);
        return new CreateMemberResponse(join);
    }

    //Entity와 API 스펙 명확하게 분리
    //Entity가 변해도 API 스펙안변함
    //실무에서는 Entitiy를 외부 노출 x , 파라미터로 받는것은 보안상 문제

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);

        return new CreateMemberResponse(id);

    }


    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberResponse(@PathVariable("id")Long id,@RequestBody @Valid updateMemberRequest request) {
        //조회
        Member findMember = memberService.findOne(id);
        //update
        memberService.update(id, request.getName());

        return new UpdateMemberResponse(findMember.getId(),findMember.getName());

    }

    @Data
    @AllArgsConstructor
    static class  Result<T>{
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;

    }

    @Data
    static class updateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}



