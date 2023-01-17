package com.example.petway.Model;

public class Animals {

    private String animal_id, animal_name, date, time, gender, breed, birth, desc,  imageUrl;

    public Animals() {
    }

    public Animals(String animal_id, String animal_name, String date, String time, String gender, String breed, String birth, String desc, String imageUrl) {
        this.animal_id = animal_id;
        this.animal_name = animal_name;
        this.date = date;
        this.time = time;
        this.gender = gender;
        this.breed = breed;
        this.birth = birth;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }

    public String getAnimal_id() {
        return animal_id;
    }

    public void setAnimal_id(String animal_id) {
        this.animal_id = animal_id;
    }

    public String getAnimal_name() {
        return animal_name;
    }

    public void setAnimal_name(String animal_name) {
        this.animal_name = animal_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
