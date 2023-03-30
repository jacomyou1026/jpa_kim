package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.jpa.JpaItemRepositoryV2;
import hello.itemservice.repository.jpa.JpaItemRespoitory;
import hello.itemservice.repository.memory.MemoryItemRepository;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

@Configuration
public class JpaConfig {

    private final EntityManager em;

    public JpaConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
        public ItemService itemService() {
            return new ItemServiceV1(itemRepository());
        }

        @Bean
        public ItemRepository itemRepository() {
            return new JpaItemRespoitory(em);
        }



}
