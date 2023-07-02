package kr.co.demojar.controller;


import kr.co.demojar.exception.NoContentException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.security.InvalidParameterException;

@Slf4j
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    /**
     * {@link NoHandlerFoundException} 또는 404 에러가 발생하는 경우
     *
     * @param e {@link Exception}
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> e01(HttpServletRequest request, Exception e) {
        log.info("request Error 404 NOT_FOUND ! [{}] | {} | {} | {}", request.getMethod(), request.getRequestURL(), e.toString(), e.getMessage());
        return new ResponseEntity<>("페이지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }

    /**
     * {@link NoHandlerFoundException} 또는 404 에러가 발생하는 경우
     *
     * @param e {@link Exception}
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> e02(HttpServletRequest request, Exception e) {
        log.info("request Error 405 METHOD_NOT_ALLOWED ! [{}] | {} | {} | {}", request.getMethod(), request.getRequestURL(), e.toString(), e.getMessage());
        return new ResponseEntity<>("METHOD_NOT_ALLOWED", HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 파라미터 정보가 유효하지 않은 경우 400 ( BAD REQUEST ) 에러 코드를 반환한다.
     *
     * @param e {@link InvalidParameterException}
     * @return
     */
    @ResponseBody
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> e03(HttpServletRequest request, Exception e) {
        log.warn("request Error InvalidParameterException ! [{}] | {} | {} | {}", request.getMethod(), request.getRequestURL(), e.toString(), e.getMessage());
        return new ResponseEntity<>("파라미터 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView e04(HttpServletRequest request, Exception e) {
        log.warn("request param validate fail ! [{}] | {} | {} | {}", request.getMethod(), request.getRequestURL(), e.toString(), e.getMessage());
        ModelAndView mav = new ModelAndView("error/403");
        return mav;
    }

    @ExceptionHandler(NoContentException.class)
    public ModelAndView e05(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.warn("content find fail ! [{}] | {} | {} | {}", request.getMethod(), request.getRequestURL(), e.toString(), e.getMessage());
        ModelAndView mav = new ModelAndView("error/nocontent");
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return mav;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView e06(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.warn("content find fail ! [{}] | {} | {} | {}", request.getMethod(), request.getRequestURL(), e.toString(), e.getMessage());
        ModelAndView mav = new ModelAndView("error/nocontent");
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return mav;
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ModelAndView e07(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.warn("content find fail ! [{}] | {} | {} | {}", request.getMethod(), request.getRequestURL(), e.toString(), e.getMessage());
        ModelAndView mav = new ModelAndView("error/nocontent");
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return mav;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> e99(HttpServletRequest request, Exception e) {
        log.error("[{}] {} ... {{}} {{}}", request.getMethod(), request.getRequestURL(), e.toString(), e.getMessage(), e);
        return new ResponseEntity<>("ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
