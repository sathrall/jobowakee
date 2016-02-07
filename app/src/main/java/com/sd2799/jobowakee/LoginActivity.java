package com.sd2799.jobowakee;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by student on 11/4/2015.
 */
public class LoginActivity extends LoginRegisterActivity {

    private Button mLoginSubmitButton;
    private Button mToRegistrationButton;
    private TextView mForgotPasswordLink;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private String mUsername;
    private String mPassword;

    private ArrayList<String> mErrors;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameField = (EditText) findViewById(R.id.username_field);
        mUsernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ...
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUsername = mUsernameField.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // ...
            }
        });
        mPasswordField = (EditText) findViewById(R.id.password_field);
        mPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ...
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPassword = mPasswordField.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // ...
            }
        });
        mForgotPasswordLink = (TextView) findViewById(R.id.forgot_password);
        mForgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Well that's just too bad!", Toast.LENGTH_SHORT).show();
            }
        });
        mLoginSubmitButton = (Button) findViewById(R.id.login_submit);
        mLoginSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();

                if (mErrors.size() <= 0) {
                    // set user to logged in
                    editor.putBoolean(IS_LOGGED_IN, true);
                    editor.commit();
                    // create a new user class to easily transfer user data
                    User user = setUserData(mUsername);
                    if (user != null) {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra(MainActivity.USER_DATA, user);
                        if (! hasProfile(user.getId())) {
                            i.putExtra(EmployerProfileActivity.TO_EMPLOYER_FORM, true);
                        }
                        startActivity(i);
                        (LoginActivity.this).finish();
                    }
                } else {
                    for (int i = 0; i < mErrors.size(); i++) {
                        Toast.makeText(LoginActivity.this, mErrors.get(i), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        mToRegistrationButton = (Button) findViewById(R.id.to_registration);
        mToRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterFormActivity.class);
                startActivity(intent);
            }
        });
    }

    private void validate() {
        mErrors = new ArrayList<String>();

        if (mUsername == null || mPassword == null) {
            mErrors.add("You must enter both fields");
        }

        if (! canLogin(mUsername, mPassword)) {
            mErrors.add("That username or password is incorrect");
        }
    }

}
