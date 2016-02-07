package com.sd2799.jobowakee;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by fexofenadine180mg on 11/13/15.
 */
public class RegisterFormActivity extends LoginRegisterActivity {

    private Button mRegistrationSubmitButton;
    private Button mToLoginButton;
    private EditText mEmailField;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mPasswordConfirmField;
    private String mEmail;
    private String mUsername;
    private String mPassword;
    private String mPasswordConfirm;

    private ArrayList<String> mErrors;

    // TODO: maybe turn into fragments

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        activateForm();
    }

    private void activateForm() {
        mEmailField = (EditText) findViewById(R.id.register_email);
        mEmailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ...
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEmail = mEmailField.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // ...
            }
        });
        mUsernameField = (EditText) findViewById(R.id.register_username);
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
        mPasswordField = (EditText) findViewById(R.id.register_password);
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
        mPasswordConfirmField = (EditText) findViewById(R.id.register_password_conf);
        mPasswordConfirmField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ...
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPasswordConfirm = mPasswordConfirmField.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // ...
            }
        });
        mRegistrationSubmitButton = (Button) findViewById(R.id.register_submit);
        mRegistrationSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegistration();

                if (mErrors.size() <= 0) {
                    if (registerUser(mEmail, mUsername, mPassword)) {
                        // set user to logged in
                        editor.putBoolean(LoginRegisterActivity.IS_LOGGED_IN, true);
                        editor.commit();
                        // create a new user class to easily transfer user data
                        User user = setUserData(mUsername);
                        if (user != null) {
                            Intent i = new Intent(RegisterFormActivity.this, EmployerProfileActivity.class);
                            i.putExtra(MainActivity.USER_DATA, user);
                            i.putExtra(EmployerProfileActivity.TO_EMPLOYER_FORM, true);
                            startActivity(i);
                            (RegisterFormActivity.this).finish();
                        }
                    }
                } else {
                    for (int i = 0; i < mErrors.size(); i++) {
                        Toast.makeText(RegisterFormActivity.this, mErrors.get(i), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        mToLoginButton = (Button) findViewById(R.id.to_login);
        mToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterFormActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void validateRegistration() {
        mErrors = new ArrayList<String>();

        if (checkUserExists(mUsername)) {
            mErrors.add("That user already exists");
        }

        if (mEmail == null || mUsername == null || mPassword == null || mPasswordConfirm == null) {
            mErrors.add("You must enter all fields");
        }

        if (! mPassword.equals(mPasswordConfirm)) {
            mErrors.add("Those passwords don't match");
        }

//            if (! validateEmail()) {
//                mRegisterMessage = "You must enter a valid email address";
//            }
    }
}
