package com.example.n33r.loginfacebook;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;

import com.facebook.internal.Utility;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    TextView welcome;
    Profile profile;
    ImageView image;
    String imageurl;

    LoginButton button;
    CallbackManager callbackManager;
    ProfileTracker mProfileTracker;

    FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken token = loginResult.getAccessToken();
            profile = Profile.getCurrentProfile();
            if (profile != null) {
                welcome.setText(constructWelcomeMessage(profile));
            }


        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };


    public MainActivityFragment() {

    }

    public void Getfriendlist(Profile profile) {
        if (profile != null) {
            GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/friends", null, HttpMethod.GET, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    JSONObject job1 =graphResponse.getJSONObject();
                    try {
                        JSONArray arr1 = job1.getJSONArray("data");
                        JSONObject ob2 = arr1.getJSONObject(0);
                        String name = ob2.getString("name").toString();
                        Log.d("omg", " " + name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            graphRequest.executeAsync();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mProfileTracker.stopTracking();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                welcome.setText(constructWelcomeMessage(currentProfile));
                Picasso.with(getActivity().getApplicationContext()).load(ProfilePic(currentProfile)).into(image);
                Getfriendlist(currentProfile);

            }
        };


        mProfileTracker.startTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button = (LoginButton) view.findViewById(R.id.login_button);
        welcome = (TextView) view.findViewById(R.id.runway);
        image = (ImageView) view.findViewById(R.id.imageView);

        button.setFragment(this);
        button.setReadPermissions("user_friends");
        button.registerCallback(callbackManager, callback);
        Log.d("OnView", "done");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    private String constructWelcomeMessage(Profile profile) {
        StringBuffer stringBuffer = new StringBuffer();
        if (profile != null) {
            Log.d("profile", "done");
            stringBuffer.append("Welcome " + profile.getName());
            Log.d("try", "" + profile.getProfilePictureUri(10, 20));


        }
        return stringBuffer.toString();
    }

    private String ProfilePic(Profile profile) {
        if (profile != null) {
            imageurl = "" + profile.getProfilePictureUri(220, 220);
            Log.d("id", profile.getId().toString());

        }
        return imageurl;
    }


}
