package jpabook.jpashop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Category extends BaseEntity{

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY )
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();



    //실전에서 다 : 대 사용 x
    @ManyToMany
    @JoinTable(name = "CATEGORY_ITEM",
    joinColumns =@JoinColumn(name = "CATEGORY_ID"), //내가 join해야 하는 것은 :CATEGORY_ID
    inverseJoinColumns = @JoinColumn(name = "ITEM_ID")) //반대쪽이 join해야하는 것 :ITEM_ID
    private List<Item> items = new ArrayList<>();

    public Category() {
    }
}
