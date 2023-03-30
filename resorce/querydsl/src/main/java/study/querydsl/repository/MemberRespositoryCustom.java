package study.querydsl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;

import java.util.List;

//구현체 : MemberRepositoryImpl
public interface MemberRespositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition condition);
    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable);

    //count쿼리 별도로 나뉘어서
    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable);
    Slice<MemberTeamDto> serachSlice(MemberSearchCondition condition, Pageable pageable);

}
