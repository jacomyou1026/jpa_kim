package hellojpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity //jpa사용한다고 인식
public class Member extends BaseEntity{

    @Id //primary key
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME") //DB는 컬럼명 : names
    private String username;

    @Embedded
    private Address homeAddress;


    /**
     * 컬렉션 타입
     * - 식별 x - 추적 어려움
     */
    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns =
        @JoinColumn(name = "MEMBER_ID"))
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

    //값타임 컬렉션 => 일대다 관계 사용
//    @ElementCollection
//    @CollectionTable(name = "ADDRESS",joinColumns =
//    @JoinColumn(name = "MEMBER_ID"))
//    private List<Address> addressHistory = new ArrayList<>();

    @OneToMany(cascade = ALL,orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID")
    private List<AddressEntity> addressHistory = new ArrayList<>();




    //기간
    @Embedded //둘 중 하나 생략가능, 김영한- 둘다 넣는것을 권장
    private Period workPeriod;

//
//    //주소
//    @Embedded
//    private Address address;

//    //주소
//    @Embedded
//    @AttributeOverrides({
//                    @AttributeOverride(name="city", column = @Column(name="WORK_CITY",insertable = false,updatable = false)),
//                    @AttributeOverride(name="street", column = @Column(name="WORK_STREET",insertable = false,updatable = false)),
//                    @AttributeOverride(name="zipcode", column = @Column(name="WORK_ZIPCODE",insertable = false,updatable = false))
//    })
//    private Address homeaddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Period getWorkPeriod() {
        return workPeriod;
    }


    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }


    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

    public List<AddressEntity> getAddressHistory() {
        return addressHistory;
    }

    public void setAddressHistory(List<AddressEntity> addressHistory) {
        this.addressHistory = addressHistory;
    }
}
