package com.at.healthylol;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anirudh Trigunayat on 11-02-2017.
 */

public class UserData {

    String name;
    String image;
    String doctor;
    public Map<String, Boolean> stars = new HashMap<>();

    UserData()
    {
        this.name=null;
        this.image=null;
        this.doctor=null;

    }
    UserData(String name,String image,String doctor)
    {
        this.name=name;
        this.image=image;
        this.doctor=doctor;

    }

    public String getDoctor() {
        return doctor;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("author",image);
        result.put("title", doctor);


        return result;
    }
}
