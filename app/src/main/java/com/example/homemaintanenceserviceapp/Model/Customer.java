package com.example.homemaintanenceserviceapp.Model;

public class Customer {
    String name, city, cnic, phonenum,email;
    public Customer() {
    }

    public Customer(String name, String city, String cnic, String phonenum, String email) {
        this.name = name;
        this.city = city;
        this.cnic = cnic;
        this.phonenum = phonenum;
        this.email=email;
    }


    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCnic() {
        return cnic;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public String getEmail() {
        return email;
    }
}
