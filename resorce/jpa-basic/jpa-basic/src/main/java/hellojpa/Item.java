package hellojpa;

import javax.persistence.*;

@Entity
//InheritanceType.TABLE_PER_CLASS - 사용x
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn //자식타입의 어떤 애가 들어가는지 DTYPE은 항상 있으면 좋다
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
