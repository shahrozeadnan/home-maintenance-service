package com.example.homemaintanenceserviceapp.Model;

public class Worker {
    String name, Address, city, experience, cnic, phonenum,email;
    boolean plumbing;
    boolean carpentry;
    boolean paint;
    boolean electrical;
    String image;

    public Worker() {
    }

    public Worker(String name, String Address, String city, String experience, String cnic, String email, String phonenum, boolean plumbing, boolean carpentry, boolean paint, boolean electrical, String image) {
        this.name = name;
        this.Address = Address;
        this.city = city;
        this.experience = experience;
        this.cnic = cnic;
        this.phonenum = phonenum;
        this.email=email;
        this.plumbing = plumbing;
        this.carpentry = carpentry;
        this.paint = paint;
        this.electrical = electrical;
        this.image=image;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {return Address;}

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getExperience() {
        return experience;
    }

    public String getCnic() {
        return cnic;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public String getImage()
    {
        return  image;
    }

    public boolean isPlumbing() {
        return plumbing;
    }

    public boolean isCarpentry() {
        return carpentry;
    }

    public boolean isPaint() {
        return paint;
    }

    public boolean isElectrical() {
        return electrical;
    }
}