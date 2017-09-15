package com.example.crystaldave.andelaproject;

import java.io.Serializable;

/**
 * Created by CRYSTALDAVE on 9/15/2017.
 */

public class Developer implements Serializable {
    private String userName;
    private String profileImage;
    private String profileUrl;

    public Developer(String userName, String profileImage, String profileUrl) {
        this.userName = userName;
        this.profileImage = profileImage;
        this.profileUrl = profileUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

}
