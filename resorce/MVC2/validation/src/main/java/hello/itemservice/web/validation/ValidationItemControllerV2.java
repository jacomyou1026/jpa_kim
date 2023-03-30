package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//ctrl+r : replace

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    //Controller가 호출될 때마다 호출 - 이 컨트롤러에서만
    //모든 메서드가 호출될때 검증0
    //요청이 올떄마다 WebDataBinder새로 만들어진다.
    @InitBinder
    public void init(WebDataBinder dataBinder){
        dataBinder.addValidators(itemValidator);
    }




    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        //errors?.containsKey  = ?가 없으면 null은 nullPoitnExcetpion발생

        return "validation/v2/addForm";
    }

    //BindingResult : item에 binding이 된 결과를 담는다.
    //순서 중요  - @ModelAttribute 다음에  BindingResult이 들어와야함 !!
//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {


        //검증 로직
//        springframework.util : 글자가 있냐
//        글자가 없으면
        //상품명이 없다면
        if (!StringUtils.hasText(item.getItemName())) {
          //  errors.put("itemName", "상품 이름은 필수 입니다.");
//            ctrl+p : 매개변수넣을 거 알려줌
            bindingResult.addError(new FieldError("item","itemName","상품 이름은 필수 입니다."));
        }
//가격오류
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
//            errors.put("price", "가격은 1,000~1,000,000까지 허용합니다.");
            bindingResult.addError(new FieldError("item","price","가격은 1,000~1,000,000까지 허용합니다."));

        }
//        수량 오류
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
//            errors.put("quantity", "수량은 최대 9,999까지 허용합니다.");
            bindingResult.addError(new FieldError("item","quantity","수량은 최대 9,999까지 허용합니다."));

        }

        //특정필드가 아닌 복합 룰 검점
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
//                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값은" + resultPrice);
                bindingResult.addError(new ObjectError("item","가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값은" + resultPrice));

            }

        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {}",bindingResult);
            //bindingResult는 자동으로 model에 담아서 넘어간다.
            return "validation/v2/addForm";
        }


        //성공 로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    //데이터 유지
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        
        if (!StringUtils.hasText(item.getItemName())) {
            //  errors.put("itemName", "상품 이름은 필수 입니다.");
//            ctrl+p : 매개변수넣을 거 알려줌

//            bindingFalse : 데이터 자체가 들어가는 것이 실패했는가
//          //데이터 유지를 위해
            bindingResult.addError(new FieldError("item","itemName",item.getItemName(),false,null,null,"상품 이름은 필수 입니다."));
        }
//가격오류
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
//            errors.put("price", "가격은 1,000~1,000,000까지 허용합니다.");
            bindingResult.addError(new FieldError("item","price",item.getPrice(),false,null,null,"가격은 1,000~1,000,000까지 허용합니다."));

        }
//        수량 오류
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
//            errors.put("quantity", "수량은 최대 9,999까지 허용합니다.");
            bindingResult.addError(new FieldError("item","quantity",item.getQuantity(),false,null,null,"수량은 최대 9,999까지 허용합니다."));

        }

        //특정필드가 아닌 복합 룰 검점
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
//                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값은" + resultPrice);
                bindingResult.addError(new ObjectError("item",null,null,"가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값은" + resultPrice));

            }

        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {}",bindingResult);
            //bindingResult는 자동으로 model에 담아서 넘어간다.
            return "validation/v2/addForm";
        }


        //성공 로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

   // @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        if (!StringUtils.hasText(item.getItemName())) {
//            ctrl+p : 매개변수넣을 거 알려줌

//            bindingFalse : 데이터 자체가 들어가는 것이 실패했는가
//          //데이터 유지를 위해
//            ctrl+E :최근 폴더 이동 단축키
            
//          new String[]{"","" } 없을 시  뒤에있는거 실행
            bindingResult.addError(new FieldError(bindingResult.getObjectName(),"itemName",item.getItemName(),false,new String[]{ "required.defualt","required.item.itemName"},null,null));
        }
        //가격오류
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
//            errors.put("price", "가격은 1,000~1,000,000까지 허용합니다.");
            bindingResult.addError(new FieldError("item","price",item.getPrice(),false,new String[]{"range.item.price"},new Object[]{1000,100000},null));

        }
//        수량 오류
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
//            errors.put("quantity", "수량은 최대 9,999까지 허용합니다.");
            bindingResult.addError(new FieldError("item","quantity",item.getQuantity(),false,new String[]{"max.item.quantity"},new Object[]{9999},null));

        }

        //특정필드가 아닌 복합 룰 검점
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
//                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값은" + resultPrice);
                bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"},new Object[]{10000,resultPrice},null));

            }

        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {}",bindingResult);
            //bindingResult는 자동으로 model에 담아서 넘어간다.
            return "validation/v2/addForm";
        }


        //성공 로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    //@PostMapping("/add")
    //데이터 유지
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        if(bindingResult.hasErrors()){
            log.info("errors = {}",bindingResult);
            //bindingResult는 자동으로 model에 담아서 넘어간다.
            return "validation/v2/addForm";
        }


        //공백
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult,"itemName","required");
//
//        if (!StringUtils.hasText(item.getItemName())) {
//            bindingResult.rejectValue("itemName","required");
//          //   new String[]{"required.item.itemName", "required"};
//        }



        //가격오류
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.rejectValue("price","range",new Object[]{1000,1000000},null);

        }
//        수량 오류
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.rejectValue("quantity","max",new Object[]{9999},null);

        }

        //특정필드가 아닌 복합 룰 검점
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null);


            }

        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {}",bindingResult);
            //bindingResult는 자동으로 model에 담아서 넘어간다.
            return "validation/v2/addForm";
        }


        //성공 로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }



    //@PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //빈등록x
//        new ItemValidator().validate(item,bindingResult);

        //검증
        itemValidator.validate(item,bindingResult);

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {}",bindingResult);
            //bindingResult는 자동으로 model에 담아서 넘어간다.
            return "validation/v2/addForm";
        }


        //성공 로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//@Validated  : Item에 대해 자동으로 검증

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {}",bindingResult);
            //bindingResult는 자동으로 model에 담아서 넘어간다.
            return "validation/v2/addForm";
        }


        //성공 로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

