package hello.itemservice.service;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {
//인터페이스 도입 이유 : 미래에 구현체를 바꿀 가능성 있을때
//    service는 interface를 잘 사용x
    Item save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findItems(ItemSearchCond itemSearch);
}
