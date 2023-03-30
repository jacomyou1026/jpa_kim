package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
//annotations 적용x - 전체
@RestControllerAdvice(basePackages = "hello.exception.api")
public class ExControllerAdvice {
    //ExceptionResolver
    //1.@ExceptionHandler
    //2.ResponseStatusException
    //3.DefaultHandlerExceptionResolver

    //이 contoller안에서만 해결 , (다른 controller 해결x)
    //ExceptionHandlerExceptionResolver 실행 ->@ExceptionHandler있으면 호출
    //정상흐름으로 바꿔서 정상적으로 리턴(200) -> 400으로 변경해야함
    //지정 ,자식 모두
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[execeptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }


    //    @ExceptionHandler(UserException.class)
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부오류");
    }


    //자식이 먼저!
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(TypeMismatchException e) {
        log.error("[TypeMismatchException] ex", e);
        return new ErrorResult("타입오류", "문자로 입력해주세요");
    }

}
