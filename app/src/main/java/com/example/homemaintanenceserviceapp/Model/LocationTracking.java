package com.example.homemaintanenceserviceapp.Model;

public class LocationTracking {
    double latitude;
    double longitude;
    String UID;
    String phoneNumber;
    String workerName;
    String workerExperience;
    String image;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public LocationTracking()
    {}
    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWorkerExperience()
    {
        return workerExperience;
    }

    public void setWorkerExperience(String workerExperience) {
        this.workerExperience = workerExperience;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
