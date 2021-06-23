package com.itheima.controller;

import com.itheima.controller.groups.GroupA;
import com.itheima.domain.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class EmployeeController {

    @RequestMapping(value = "/addemployee")
    public String addEmployee(@Validated({GroupA.class}) Employee employee, Errors errors, Model m){
        if(errors.hasErrors()){
            List<FieldError> fieldErrors = errors.getFieldErrors();
            System.out.println(fieldErrors.size());
            for(FieldError error : fieldErrors){
                System.out.println(error.getField());
                System.out.println(error.getDefaultMessage());
                m.addAttribute(error.getField(),error.getDefaultMessage());
            }
            return "addemployee.jsp";
        }
        return "success.jsp";
    }

    @RequestMapping(value = "/addemployee2")
    //使用@Valid开启校验，使用@Validated也可以开启校验
    //Errors对象用于封装校验结果，如果不满足校验规则，对应的校验结果封装到该对象中，包含校验的属性名和校验不通过返回的消息
    public String addEmployee2(@Valid Employee employee, Errors errors, Model m){
        //判定Errors对象中是否存在未通过校验的字段
        if(errors.hasErrors()){
            //获取所有未通过校验规则的信息
            List<FieldError> fieldErrors = errors.getFieldErrors();
            System.out.println(fieldErrors.size());
            for(FieldError error : fieldErrors){
                System.out.println(error.getField());
                System.out.println(error.getDefaultMessage());
                //将校验结果信息添加到Model对象中，用于页面显示，后期实际开发中无需这样设定，返回json数据即可
                m.addAttribute(error.getField(),error.getDefaultMessage());
            }
            //当出现未通过校验的字段时，跳转页面到原始页面，进行数据回显
            return "addemployee.jsp";
        }
        return "success.jsp";
    }
}
