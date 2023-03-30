package jpabook.jpashop.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

/**
 * 공통 적용 가능
 * 값타입을 의미있게 사용가능
 */
@Embeddable
public class Address {

    @Column(length = 10)
    private String city;
    @Column(length = 20)
    private String street;
    @Column(length = 5)
    private String zipcode;


    /**
     *의미 있는 비즈니스 메서드 사용가능
     */
    public String fullAddress() {
        return getCity() +" "+ getStreet() + " "+getZipcode();
    }

//    public boolean isValid() {
//
//    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipcode() {
        return zipcode;
    }

    private void setCity(String city) {
        this.city = city;
    }

    private void setStreet(String street) {
        this.street = street;
    }

    private void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    
    //getter로 접근하게 끔하기

    /**
     *
     * 필드에 직접 접근 시, 프록시일때 안됨
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) && Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }
}
