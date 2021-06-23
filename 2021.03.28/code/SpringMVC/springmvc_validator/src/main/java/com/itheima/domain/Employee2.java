package com.itheima.domain;


import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

//提供给各位同学一套实用的校验范例，仅供参考
public class Employee2 implements Serializable {

    private String id;//员工ID

    private String code;//员工编号

    @NotBlank(message = "员工名称不能为空")
    private String name;//员工姓名

    @NotNull(message = "员工年龄不能为空")
    @Max(value = 60,message = "员工年龄不能超过60岁")
    @Min(value = 18,message = "员工年里不能小于18岁")
    private Integer age;//员工年龄

    @NotNull(message = "员工生日不能为空")
    @Past(message = "员工生日要求必须是在当前日期之前")
    private Date birthday;//员工生日

    @NotBlank(message = "请选择员工性别")
    private String gender;//员工性别

    @NotEmpty(message = "请输入员工邮箱")
    @Email(regexp = "@",message = "邮箱必须包含@符号")
    private String email;//员工邮箱

    @NotBlank(message = "请输入员工电话")
    @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$",message = "手机号不正确")
    private String telephone;//员工电话

    @NotBlank(message = "请选择员工类别")
    private String type;//员工类型：正式工为1，临时工为2

    @Valid//表示需要嵌套验证
    private Address address;//员工住址

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", type='" + type + '\'' +
                ", address=" + address +
                '}';
    }
}
