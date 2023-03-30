package hello.itemservice;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;

    /**
     * 확인용 초기 데이터 추가
     */

    @EventListener(ApplicationReadyEvent.class) //스프링이 컨테이너가 완전히 초기화를 다 끝내고, 발생하는 이벤트
    public void initData() {
        log.info("test data init");
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item( "itemB", 20000, 20));
    }

}
