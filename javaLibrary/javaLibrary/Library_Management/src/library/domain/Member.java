package library.domain;

import library.domain.enums.MemberType;
import library.repository.base.IEntity;

public class Member implements IEntity {
    private int id;
    private String name;
    private int age;
    private String gender;
    private String email;
    private String phone;
    private MemberType Type;

    public Member() { }


    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setType(MemberType memberType) {
        this.Type = memberType;
    }

    //getters
    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public int getAge() {
        return this.age;
    }
    public String getGender() {
        return this.gender;
    }
    public String getEmail() {
        return this.email;
    }
    public String getPhone() {
        return this.phone;
    }
    public MemberType getType() {
        return this.Type;
    }
}
