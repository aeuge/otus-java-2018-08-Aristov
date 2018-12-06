package ru.otus.hibernateORM.dataset;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orm")
public class UsersDataSet extends DataSet {
    @Column
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Column(name="age")
    private int age;

    public void setAddress(String address) {
        this.address.setAddress(address);
    }

    @OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "user_id", nullable = false) // default is address_id
    private AddressDataSet address = null;


    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<PhoneDataSet> phones = new ArrayList<>();

    public UsersDataSet() {
    }
    public UsersDataSet(long id, String name, AddressDataSet address, PhoneDataSet... phones) {
        this.setId(id);
        this.setName(name);
        this.age = 10;
        this.address = address;
        List<PhoneDataSet> userPhones = Arrays.asList(phones);
        this.setPhones(userPhones);
        userPhones.forEach(phone -> phone.setUser(this));
    }

    public UsersDataSet(String name, AddressDataSet address, PhoneDataSet... phones) {
        this(1, name, address, phones);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public List<PhoneDataSet> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDataSet> phones) {
        this.phones.addAll(phones);
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
                "id='" + getId() + '\'' +
                ",name='" + name + '\'' +
                ",age='" + getAge() + '\'' +
                ", addresses=" + address +
                ", phones=" + phones +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) ) return false;
        if (!(o instanceof UsersDataSet)) return false;
        UsersDataSet that = (UsersDataSet) o;

        List<PhoneDataSet> listP = that.getPhones();
        if (this.getPhones() == null) {
            if (listP != null) {
                return false;
            }
        } else {
            if (listP.size() != phones.size()) {
                return false;
            }
            for (int i = 0; i < listP.size(); i++) {
                if (!(listP.get(i).equals(phones.get(i)))) {
                    return false;
                }
            }
        }
        return age == that.getAge() &&
                Objects.equals(address, that.getAddress()) &&
                Objects.equals(name, that.getName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), name, age, address, phones);
    }
}
