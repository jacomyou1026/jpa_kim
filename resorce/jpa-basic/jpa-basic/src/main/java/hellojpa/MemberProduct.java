package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;

//단방향으로도 이미 연관관계 매핑 완료
@Entity
public class MemberProduct {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name="Product_ID")
    private Product product;

    private int count;
    private int price;
    private LocalDateTime orderDateTime;

}
