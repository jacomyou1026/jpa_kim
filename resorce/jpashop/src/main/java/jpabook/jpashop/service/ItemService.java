package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
//위임만 하는 class
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional //readOnly이면 저장안됨
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    //변경감지
    @Transactional
    public void updateItem(Long itemId, int price, String name) {
        Item findItem = itemRepository.findOne(itemId); //영속상태
        findItem.setPrice(price);
        findItem.setName(name);
        //findItem.chanve(pirce, name); 의미 있는 메서드를 사용 -setter를 사용x

    }
    
    //병합(merge)
    //가급적이면 merge를 사용하지 말고, 변경감지로 사용


    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long ItemId) {
        return itemRepository.findOne(ItemId);

    }




}

