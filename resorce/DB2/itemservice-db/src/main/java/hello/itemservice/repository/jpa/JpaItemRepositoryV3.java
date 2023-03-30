package hello.itemservice.repository.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.domain.QItem;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static hello.itemservice.domain.QItem.*;
import static hello.itemservice.domain.QItem.item;

@Repository
@Transactional
public class JpaItemRepositoryV3 implements ItemRepository {

    private final EntityManager em;
    private  final JPAQueryFactory query; //querydsl


    public JpaItemRepositoryV3(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Item save(Item item) {
         em.persist(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = em.find(Item.class, itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    public List<Item> findAllold(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        QItem qItem = item;
        
        //동적쿼리
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(itemName)) {
            builder.and(item.itemName.like("%" + itemName + "%"));
        }
        if (maxPrice != null) {
            builder.and(item.price.loe(maxPrice)); //loe = 작거나 같다.(<=)
        }


        List<Item> result = query.select(item).from(item).
                where(builder).fetch();
        return result;
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        QItem qItem = item;


        return query.select(item).from(item).
                where(likeItemName(itemName),maxPrice(maxPrice)).fetch();
    }

    //동적쿼리1
    private Predicate maxPrice(Integer maxPrice) {
        if (maxPrice != null) {
            return item.price.loe(maxPrice); //loe = 작거나 같다.
        }
        return null;

    }

    private BooleanExpression likeItemName(String itemName) {
        //null일 시 where조건 무시

        if (StringUtils.hasText(itemName)) {
            return item.itemName.like("%" + itemName + "%");
        }
        return null;
    }





}
