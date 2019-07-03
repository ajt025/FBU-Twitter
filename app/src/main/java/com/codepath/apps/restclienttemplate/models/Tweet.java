package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {
    // list out attributes
    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;
    public boolean liked;
    public int likeCount;
    public String mediaUrl;

    public Tweet() {}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.liked = jsonObject.getBoolean("favorited");
        tweet.likeCount = jsonObject.getInt("favorite_count");

        JSONObject entities  = jsonObject.getJSONObject("entities");

        if (entities.has("media")) {
            // grab the media_url_https from many nested arrays/objs
            tweet.mediaUrl = entities.getJSONArray("media").getJSONObject(0)
                    .getString("media_url_https");
        }

        return tweet;
    }
}
