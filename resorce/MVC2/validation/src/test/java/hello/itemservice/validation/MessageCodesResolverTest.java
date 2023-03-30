package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ObjectError;

import static org.assertj.core.api.Assertions.*;

public class MessageCodesResolverTest {
    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        for(String messageCode: messageCodes){
        System.out.println("messageCode = " + messageCode);
        }

        //new ObjectError("item", new String[]{"required.item", "required"});
        assertThat(messageCodes).containsExactly("required.item", "required");
    }


    //디테일한거 먼저 - >
    @Test
    void messageCodesResovlerField(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        for(String messageCode : messageCodes){
            System.out.println("messageCode = " + messageCode);
        }
        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String"
                , "required"
        );
    }




}
