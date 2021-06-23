package com.itheima.domain;



import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
//嵌套校验的实体中，对每个属性正常添加校验规则即可
public class Address {
    @NotBlank(message = "请输入省份名称")
    private String provinceName;//省份名称

    @NotBlank(message = "请输入城市名称")
    private String cityName;//城市名称

    @NotBlank(message = "请输入详细地址")
    private String detail;//详细住址

    @NotBlank(message = "请输入邮政编码")
    @Size(max = 6,min = 6,message = "邮政编码由6位组成")
    private String zipCode;//邮政编码

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "provinceName='" + provinceName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", detail='" + detail + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
