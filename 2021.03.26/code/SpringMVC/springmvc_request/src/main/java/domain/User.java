package domain;

import java.util.List;
import java.util.Map;

public class User {
    private String name;
    private Integer age;

    private Address address;

    private List<String> nick;

    private List<Address> addresses;

    public List<Address> getAddresses() {
        return addresses;
    }

    public Map<String,Address> addressMap;

    public Map<String, Address> getAddressMap() {
        return addressMap;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", nick=" + nick +
                ", addresses=" + addresses +
                ", addressMap=" + addressMap +
                '}';
    }

    public void setAddressMap(Map<String, Address> addressMap) {
        this.addressMap = addressMap;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<String> getNick() {
        return nick;
    }

    public void setNick(List<String> nick) {
        this.nick = nick;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

}
