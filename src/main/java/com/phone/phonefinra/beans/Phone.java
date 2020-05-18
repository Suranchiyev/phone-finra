package com.phone.phonefinra.beans;
import java.util.List;
import java.util.Objects;

public class Phone {
    private List<Byte> phone;

    public Phone(List<Byte> phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Phone)) return false;


        Phone phone1 = (Phone) o;

        return phone.equals(phone1.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone);
    }

    public List<Byte> getPhone() {
        return phone;
    }

    public void setPhone(List<Byte> phone) {
        this.phone = phone;
    }
}
