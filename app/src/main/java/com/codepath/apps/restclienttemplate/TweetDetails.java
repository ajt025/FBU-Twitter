package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class TweetDetails extends AppCompatActivity {

    Tweet tweet;
    private TwitterClient client;

    TextView tvUsername;
    TextView tvHandle;
    TextView tvBody;
    TextView tvTime;
    ImageView ivProfileImage;
    ImageButton ibLike;
    ImageButton ibRetweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        client = TwitterApp.getRestClient(TweetDetails.this);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvHandle = (TextView) findViewById(R.id.tvHandle);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvTime = (TextView) findViewById(R.id.tvTime);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        ibLike = (ImageButton) findViewById(R.id.ibLikeDetail);
        ibRetweet = (ImageButton) findViewById(R.id.ibRetweetDetail);

        // populate the views according to this data
        tvUsername.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvHandle.setText("@" + tweet.user.screenName);
        // tvTime.setText(ParseRelativeData.getRelativeTimeAgo(tweet.createdAt));
        // TODO set time, also figure out how to put non-truncated text in

        Glide.with(this)
                .load(tweet.user.profileImageUrl).into(ivProfileImage);

        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO use tags to switch between like and unlike drawable
            }
        });

        ibRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long tweetID = tweet.uid;

                client.retweet(tweetID, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Tweet tweet = Tweet.fromJson(response);
                            Intent data = new Intent();
                            data.putExtra("tweet", Parcels.wrap(tweet));
                            // bundle data for response
                            setResult(RESULT_OK, data);
                            // close the activity
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("TweetDetails", errorResponse.toString());
                    }
                });
            }
        });
    }
}
