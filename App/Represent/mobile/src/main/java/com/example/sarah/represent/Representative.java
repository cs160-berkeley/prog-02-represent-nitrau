package com.example.sarah.represent;

/**
 * Created by Sarah on 2/28/2016.
 */
public class Representative {

    private int portrait;
    private String type;
    private String name;
    private String party;
    private String email;
    private String website;
    private String tweet;

    public Representative(int portrait, String type, String name, String party, String email, String website, String tweet) {
        this.portrait = portrait;
        this.type = type;
        this.name = name;
        this.party = party;
        this.email = email;
        this.website = website;
        this.tweet = tweet;
    }

    public int getPortrait() {
        return portrait;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getTweet() {
        return tweet;
    }

    public String getType() {
        return type;
    }

    public String getWebsite() {
        return website;
    }
}
