package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 기본적으로 실무에선 테이블 등록,수정을 남긴다.
 * 언제 수정, 언제 등록 - 모든 테이블에 다 적용
 * 
 * 기본적으로
 * -등록일,수정일,등록자,수정자
 */

@MappedSuperclass // 속성을 테이블에서 같이 쓸수 있도록
@Getter
public class JpaBaseEntity {

    @Column(updatable = false) //update되지 않음
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist //persist하기전 이벤트 발생
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate //update하기 전에
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }



}
