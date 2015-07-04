package com.example.n33r.loginfacebook;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    TextView welcome;
    Profile profile;
    LoginButton button;
    CallbackManager callbackManager;
    ProfileTracker mProfileTracker;
    FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken token = loginResult.getAccessToken();
            profile = Profile.getCurrentProfile();
            welcome.setText(constructWelcomeMessage(profile));

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.d("VIVZ", "" + currentProfile);
                welcome.setText("myname: "+constructWelcomeMessage(currentProfile));
            }
        };
        mProfileTracker.startTracking();
        Log.d("create","done");
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
        welcome=(TextView)view.findViewById(R.id.runway);
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
            Log.d("profile","done");
            stringBuffer.append("Welcome " + profile.getName());
            Log.d("idcheck",""+profile.getLinkUri());
        }
        return stringBuffer.toString();
    }
}
