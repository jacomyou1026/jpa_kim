package com.example.tymeLeaf.basic;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpSession;
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
