package com.codepath.apps.restclienttemplate.models;

import android.arch.persistence.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class Tweet {
    // list out attributes
    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        return tweet;
    }
}
