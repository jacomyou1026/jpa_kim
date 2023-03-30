package com.example.itme_service.web.basic;

import com.example.itme_service.item.ItemRepository;
import com.example.itme_service.item.item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
public class BasicItemController {

    //    동적으로 만들기 위해 templates사용 . 타임리프 사용
    private final ItemRepository itemRepository;

    @Autowired
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public String items(Model model) {
        List<item> items = itemRepository.fianlAll();
        System.out.println(items.get(0).getId() + ": id");
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
//     @PathVariable : URL 경로에 변수를 넣어주는거에요
    public String item(@PathVariable long itemId, Model model) {
        item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";

    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    //    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quanity,
                            Model model) {
        item item = new item();
        item.setItemName(itemName);
        item.setQuanity(quanity);
        item.setPrice(price);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    //    @PostMapping("/add")
//    item의로 모델에 넣어줌
    public String addItemV2(@ModelAttribute("item") item item, Model model) {

        itemRepository.save(item);

//        model.addAttribute("item",item); 자동 추가 , 생략 가능

        return "basic/item";
    }

    //@PostMapping("/add")
//    클래스명의 첫글자를 소문자로 바꾸어서 model에 넣어줌
    public String addItemV3(@ModelAttribute item item, Model model) {

        itemRepository.save(item);

//        model.addAttribute("item",item); 자동 추가 , 생략 가능

        return "basic/item";
    }

    //새로고침을 할시 마지막으로 한 행위 반복하여 계속 상품 저장됨
//    redirect를 할 경우 URL을 바뀌면서 새로 요청하게됨
//    @PostMapping("/add")
    public String addItemV4(item item) {

        itemRepository.save(item);

//        model.addAttribute("item",item); 자동 추가 , 생략 가능

        return "basic/item";
    }
    
    //@PostMapping("/add")
    public String addItemV5(item item) {

        itemRepository.save(item);

        return "redirect:/basic/items/"+item.getId();
    }

    //addFlashAttribute

    /**
     *
     * @param item
     * @param redirectAttributes
     * @return
     *
     * addAttribute로 전달한 값은 url뒤에  붙으며,
     *리프레시해도 데이터가 유지된다
     *
     * addFlashAttribute :전달한 값은 url뒤에 붙지 않는다.
     *일회성이라 리프레시할 경우 데이터가 소멸한다
     * addAttribute는 값을 지속적으로 사용해야할때 addFlashAttribute는 일회성으로 사용해야할때 사용해야합니다
     */

    @PostMapping("/add")
    public String addItemV6(item item, RedirectAttributes redirectAttributes) {
        item saveItem = itemRepository.save(item);

        //http://localhost:8080/basic/items/3?status=true

        //{itemId}로 들어감 이렇게 치환을 해준다. 없으면 쿼리로 들어감
        redirectAttributes.addAttribute("itemId", saveItem.getId());
        //쿼리로 들어감
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edite")
    public String edit(@PathVariable Long itemId, Model model) {
        item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";

    }

    @PostMapping("/{itemId}/edite")
    public String editFrom(@PathVariable Long itemId, @ModelAttribute item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";

    }




    /*
     * 테스터용 데이터 추가*/

    @PostConstruct
    public void init() {
        itemRepository.save(new item("itemA", 10000, 20));
        itemRepository.save(new item("itemB", 10000, 20));
    }

}
