package com.example.thmleaf.basic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/basic")
public class BasicController {
    @GetMapping("text-basic")
    public String textBasic(Model model){
        model.addAttribute("data","Hello <b>Spring</b>");
        return "basic/text-basic";
    }

    @GetMapping("/variable")
    public String variable(Model model){
        var useA = new User("userA",10);
        var useB = new User("userB",20);
        List<User> list = new ArrayList<>(Arrays.asList(useA,useB));

        Map<String,User> map = new HashMap<>();
        map.put("userA",useA);
        map.put("userA",useB);

        model.addAttribute("user",useA);
        model.addAttribute("users",list);
        model.addAttribute("userMap",map);
        return "basic/variable";



    }


    @GetMapping("/basic-objects")
    public String basicObject(HttpSession session) {
        session.setAttribute("sessionData", "Hello Session");
        return "basic/basic-objects";
    }

    @Component("helloBean")
    static class HelloBean{
        public String hello(String data) {
            return "Hello " + data;
        }
    }

    @GetMapping("/data")
    public String hello(Model model){
            model.addAttribute("localDateTime", LocalDateTime.now());
        return "basic/date";
    }

    @GetMapping("/link")
    public String link(Model model){
        model.addAttribute("param1", "data1");
        model.addAttribute("param2", "data2");
        return "basic/link";

    }

    @GetMapping("/literal")
    public String literal(Model model){
        model.addAttribute("data", "Spring!");
        return "basic/literal";

    }

    @GetMapping("/operation")
    public String operation(Model model){
        model.addAttribute("nullData",null);
        model.addAttribute("data", "Spring!");
        return "basic/operation";

    }

    @GetMapping("/attribute")
    public String operation(){
        return "basic/attribute";
    }

    @GetMapping("/each")
    public String each(Model model) {
        addUsers(model);
        return "basic/each";
    }


    private void addUsers(Model model) {
        List<User> users = Arrays.asList(new User("userA", 10),
                new User("userB", 20),
                new User("userC", 30));

        model.addAttribute("users", users);
    }

    @GetMapping("/condition")
    public String condition(Model model){
            addUsers(model);
        return "basic/condition";
    }

    @GetMapping("/comments")
    public String comments(Model model){
        model.addAttribute("data", "Spring!");
        return "basic/comments";
    }

    @GetMapping({"/block"})
    public String block(Model model) {
        this.addUsers(model);
        return "basic/block";
    }

//    @GetMapping("/javascript")
//    public String javascript(Model model) throws JsonProcessingException{
//        model.addAttribute("user",new User("userA",10));
//        addUsers(model);
//        return "basic/javascript";
//    }
    @GetMapping("/javascript")
    public String javascript(Model model) throws JsonProcessingException{
        model.addAttribute("user", new User("userA", 10));
        addUsers(model);
        ObjectMapper om = new ObjectMapper();

        String userD = om.writeValueAsString(new User("userD", 30));
        model.addAttribute("userD", userD);

        return "basic/javascript";
    }



    @Data
    static class User{
        private String username;
        private int age;

        public User(String username, int age) {
            this.username = username;
            this.age = age;
        }
    }

}
