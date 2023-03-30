package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    private String city;
     private String street;
    private String zipcode;

    //
    protected Address() {
    }

    //리플렉션, 프록시 기술을 사용해야함으로 기본생성자로
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
