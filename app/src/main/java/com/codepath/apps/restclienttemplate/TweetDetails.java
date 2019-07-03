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
    ImageView ivMedia;

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
        ivMedia = (ImageView) findViewById(R.id.ivMedia);

        // populate the views according to this data
        tvUsername.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvHandle.setText("@" + tweet.user.screenName);
        // tvTime.setText(ParseRelativeData.getRelativeTimeAgo(tweet.createdAt));
        // TODO set time, also figure out how to put non-truncated text in

        // determine background resource for favorite button
        if (tweet.liked) {
            ibLike.setImageResource(R.drawable.ic_vector_heart);
            ibLike.setTag(R.drawable.ic_vector_heart);
        } else {
            ibLike.setImageResource(R.drawable.ic_vector_heart_stroke);
            ibLike.setTag(R.drawable.ic_vector_heart_stroke);
        }

        // set profile image
        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .into(ivProfileImage);

        // set media if media exists
        if (tweet.mediaUrl != null) {
            Glide.with(this)
                    .load(tweet.mediaUrl)
                    .into(ivMedia);
        }

        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( (int) ibLike.getTag() == R.drawable.ic_vector_heart_stroke) {
                    client.likeTweet(tweet.uid, new JsonHttpResponseHandler());
                    ibLike.setImageResource(R.drawable.ic_vector_heart);
                    ibLike.setTag(R.drawable.ic_vector_heart);
                } else {
                    client.unlikeTweet(tweet.uid, new JsonHttpResponseHandler());
                    ibLike.setImageResource(R.drawable.ic_vector_heart_stroke);
                    ibLike.setTag(R.drawable.ic_vector_heart_stroke);
                }
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
