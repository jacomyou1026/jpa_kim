package study.querydsl.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.repository.MemberJpaRepository;
import study.querydsl.repository.MemberRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberJpaRepository memberJpaRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition) {
        return memberJpaRepository.serachByBuider(condition);
    }

    //페이지 default : page = 0 , size = 20
    @GetMapping("/v2/members")
    public Page<MemberTeamDto> searchMemberV2(MemberSearchCondition condition, @RequestParam(defaultValue = "1",required = false) int page, @PageableDefault(size = 10) Pageable pageable) {
        System.out.println("page = " + page);
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<MemberTeamDto> memberTeamDtos = memberRepository.searchPageSimple(condition, pageRequest);
        int number = memberTeamDtos.getNumber() + 1;
        long pageableOffest = memberTeamDtos.getPageable().getOffset() + 1;


        return memberTeamDtos;
    }

    @GetMapping("/v3/members")
    public Page<MemberTeamDto> searchMemberV3(MemberSearchCondition condition,  @PageableDefault(size = 10) Pageable pageable) {
        return memberRepository.searchPageComplex(condition,pageable);
    }

    @GetMapping("/v4/members")
    public Slice<MemberTeamDto> searchMemberV4(MemberSearchCondition condition,  @RequestParam(defaultValue = "1",required = false) int page ,@PageableDefault(size = 10) Pageable pageable) {
        page = page - 1;
        System.out.println("page = " + page);
        PageRequest pageRequest = PageRequest.of(page, 30);
        Slice<MemberTeamDto> memberTeamDtos = memberRepository.serachSlice(condition, pageable);
        System.out.println("memberTeamDtos = " + memberTeamDtos.hasNext());
        System.out.println("memberTeamDtos.nextPageable() = " + memberTeamDtos.nextPageable());
        System.out.println("memberTeamDtos.nextPageable().getPageNumber() = " + memberTeamDtos.nextPageable().getPageNumber());

        return memberRepository.serachSlice(condition,pageable);
    }


}
