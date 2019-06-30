package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    // tweet char limit
    private static final int MAX_CHAR = 280;

    Button sendTweet;
    EditText etTweet;
    TextView tvCharCount;

    private TwitterClient client;
    private TextWatcher etWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(ComposeActivity.this);
        sendTweet = findViewById(R.id.btnPost);
        etTweet = findViewById(R.id.etTweet);
        tvCharCount = findViewById(R.id.tvCharCount);

        tvCharCount.setText("0 / " + MAX_CHAR);

        sendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etTweet.getText().toString();

                // check if tweet is of required length
                if (message.length() <= MAX_CHAR) {
                    client.sendTweet(message, new JsonHttpResponseHandler() {
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
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                } else {
                    Toast.makeText(v.getContext(), "Tweet is too long!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // define our text watch for char counting
        etWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int currTextLength = s.length();

                // This sets a textview to the current length
                tvCharCount.setText(String.valueOf(currTextLength) + " / " + MAX_CHAR);

                if (currTextLength > MAX_CHAR) {
                    tvCharCount.setTextColor(Color.RED);
                } else {
                    tvCharCount.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
                }
            }

            public void afterTextChanged(Editable s) {}
        };

        // assign the created textwatcher to our editText
        etTweet.addTextChangedListener(etWatcher);
    }
}
