package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetails extends AppCompatActivity {

    Tweet tweet;

    TextView tvUsername;
    TextView tvHandle;
    TextView tvBody;
    TextView tvTime;
    ImageView ivProfileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvHandle = (TextView) findViewById(R.id.tvHandle);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvTime = (TextView) findViewById(R.id.tvTime);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        // populate the views according to this data
        tvUsername.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        // tvTime.setText(ParseRelativeData.getRelativeTimeAgo(tweet.createdAt));
        // TODO set time and handle, also figure out how to put non-truncated text in

        Glide.with(this)
                .load(tweet.user.profileImageUrl).into(ivProfileImage);
    }
}
