package hellojpa;


import jdk.jfr.DataAmount;
import jdk.jfr.Enabled;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


//@Entity // 상속관계
@MappedSuperclass //전체적으로 공통적인 속성 적용 (속성만 상속)
public abstract class BaseEntity {

    @Column(name = "Insert_Member")
    private LocalDateTime creadDate;
    private String createdBy;

    @Column(name = "update_Member")
    private String lastmodifiedBy;
    private LocalDateTime lastModifedDate;

    public LocalDateTime getCreadDate() {
        return creadDate;
    }

    public void setCreadDate(LocalDateTime creadDate) {
        this.creadDate = creadDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastmodifiedBy() {
        return lastmodifiedBy;
    }

    public void setLastmodifiedBy(String lastmodifiedBy) {
        this.lastmodifiedBy = lastmodifiedBy;
    }

    public LocalDateTime getLastModifedDate() {
        return lastModifedDate;
    }

    public void setLastModifedDate(LocalDateTime lastModifedDate) {
        this.lastModifedDate = lastModifedDate;
    }
}
