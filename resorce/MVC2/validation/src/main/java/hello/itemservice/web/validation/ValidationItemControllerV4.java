package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

//ctrl+r : replace

@Slf4j
@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        //errors?.containsKey  = ?가 없으면 null은 nullPoitnExcetpion발생

        return "validation/v4/addForm";
    }

    //@Validated - bean validation적용됨
    //@PostMapping("/add")
    //shift+f6 : 단축키 변수 한번에 변경
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//@Validated  : Item에 대해 자동으로 검증

        //특정필드가 아닌 복합 룰 검점
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000)  {
//                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값은" + resultPrice);
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);

            }

        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            //bindingResult는 자동으로 model에 담아서 넘어간다.
            return "validation/v4/addForm";
        }


        //성공 로직

        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }


    //@Validated - bean validation적용됨
    @PostMapping("/add")
    public String addItem2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//@Validated  : Item에 대해 자동으로 검증

        //특정필드가 아닌 복합 룰 검점
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
//                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값은" + resultPrice);
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);

            }

        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            //bindingResult는 자동으로 model에 담아서 넘어간다.
            return "validation/v4/addForm";
        }


        //성공 로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

    //@PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {

        //특정필드가 아닌 복합 룰 검점
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if(bindingResult.hasErrors()){
            log.info("error{}",bindingResult);
            return "validation/v4/editForm";

        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }

   // @PostMapping("/{itemId}/edit")
    public String edit2(@PathVariable Long itemId, @Validated({UpdateCheck.class}) @ModelAttribute Item item, BindingResult bindingResult) {

        //특정필드가 아닌 복합 룰 검점
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if(bindingResult.hasErrors()){
            log.info("error{}",bindingResult);
            return "validation/v4/editForm";

        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }


    @PostMapping("/{itemId}/edit")
    public String edit3(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {

        //특정필드가 아닌 복합 룰 검점
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if(bindingResult.hasErrors()){
            log.info("error{}",bindingResult);
            return "validation/v4/editForm";

        }

        Item itemParam = new Item();
        itemParam.setItemName(form.getItemName());
        itemParam.setPrice(form.getPrice());
        itemParam.setQuantity(form.getQuantity());


        itemRepository.update(itemId, itemParam);
        return "redirect:/validation/v4/items/{itemId}";
    }

}
