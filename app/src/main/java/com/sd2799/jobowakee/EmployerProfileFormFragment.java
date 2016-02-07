package com.sd2799.jobowakee;

import android.app.Activity;
import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by fexofenadine180mg on 11/15/15.
 */
public class EmployerProfileFormFragment extends Fragment {

    private JobowakeeDatabaseHelper mDatabase = null;
    private SQLiteDatabase mDB = null;

    private String mWelcomeMessage;
    private TextView mWelcomeText;

    private EditText mPersonalEmailField;
    private EditText mPersonalFirstNameField;
    private EditText mPersonalLastNameField;
    private EditText mCompanyNameField;
    private EditText mCompanyAddressField;
    private EditText mCompanyPhoneField;

    private Button mSubmitButton;

    private String mPersonalEmail;
    private String mPersonalFirstName;
    private String mPersonalLastName;
    private String mCompanyName;
    private String mCompanyAddress;
    private String mCompanyPhone;

    private ArrayList<String> mErrors;

    private User user;

    OnProfileSubmissionListener listener;

    public interface OnProfileSubmissionListener {
        public void onProfileSubmission(User user);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // check to see if OnProfileSubmissionListener
        // is implemented by host activity
        try {
            listener = (OnProfileSubmissionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnProfileSubmissionListener"
            );
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable(MainActivity.USER_DATA);

        mDatabase = new JobowakeeDatabaseHelper(getActivity());
        mDB = mDatabase.getWritableDatabase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mDB != null) {
            mDB.close();
        }

        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_employer_profile_form, parent, false);

        mWelcomeMessage = "Welcome, " + user.getUsername() + "!";

        mWelcomeText = (TextView) v.findViewById(R.id.welcome);
        mWelcomeText.setText(mWelcomeMessage);

        mPersonalEmailField = (EditText) v.findViewById(R.id.personal_email);
        mPersonalEmailField.setText(user.getEmail());
        mPersonalEmailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPersonalEmail = mPersonalEmailField.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        mPersonalFirstNameField = (EditText) v.findViewById(R.id.personal_first_name);
        mPersonalFirstNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPersonalFirstName = mPersonalFirstNameField.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        mPersonalLastNameField = (EditText) v.findViewById(R.id.personal_last_name);
        mPersonalLastNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPersonalLastName = mPersonalLastNameField.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        mCompanyNameField = (EditText) v.findViewById(R.id.company_name);
        mCompanyNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCompanyName = mCompanyNameField.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        mCompanyAddressField = (EditText) v.findViewById(R.id.company_address);
        mCompanyAddressField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCompanyAddress = mCompanyAddressField.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        mCompanyPhoneField = (EditText) v.findViewById(R.id.company_phone);
        mCompanyPhoneField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCompanyPhone = mCompanyPhoneField.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        mSubmitButton = (Button) v.findViewById(R.id.submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();

                if (mErrors.size() <= 0) {
                    if (createEmployerProfile(mPersonalEmail, mPersonalFirstName,
                            mPersonalLastName, mCompanyName, mCompanyAddress, mCompanyPhone)) {
                        listener.onProfileSubmission(user);
                    }
                } else {
                    for (int i = 0; i < mErrors.size(); i++) {
                        Toast.makeText(getActivity(), mErrors.get(i), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        return v;
    }

    private void validate() {
        // TODO: verify address and phone number
        mErrors = new ArrayList<String>();

        if (mPersonalEmail == "" || mPersonalFirstName == "" || mPersonalLastName == ""
                || mCompanyName == "" || mCompanyAddress == "" || mCompanyPhone == "") {
            mErrors.add("You must fill out all fields");
        }

    }

    private boolean createEmployerProfile(String email, String firstName, String lastName,
                     String company, String address, String phone) {
        boolean success = false;

        // grab the user id
        String userId = user.getId().toString();

        // start the transaction
        mDB.beginTransaction();

        try {
            ContentValues employerRecordToAdd = new ContentValues();
            employerRecordToAdd.put(JobowakeeDatabase.Employers.USER_ID, userId);
            employerRecordToAdd.put(JobowakeeDatabase.Employers.EMAIL, email);
            employerRecordToAdd.put(JobowakeeDatabase.Employers.FIRST_NAME, firstName);
            employerRecordToAdd.put(JobowakeeDatabase.Employers.LAST_NAME, lastName);
            employerRecordToAdd.put(JobowakeeDatabase.Employers.COMPANY_NAME, company);
            employerRecordToAdd.put(JobowakeeDatabase.Employers.COMPANY_ADDRESS, address);
            employerRecordToAdd.put(JobowakeeDatabase.Employers.COMPANY_PHONE, phone);
            mDB.insertOrThrow(JobowakeeDatabase.Employers.EMPLOYERS_TABLE_NAME,
                    JobowakeeDatabase.Employers.USER_ID, employerRecordToAdd);
            mDB.setTransactionSuccessful();
            success = true;
        } catch (SQLException e) {
            Toast.makeText(getActivity(), "SQLException: " + e, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Other Exception: " + e, Toast.LENGTH_LONG).show();
        } finally {
            // always end the transaction
            mDB.endTransaction();
        }

        return success;
    }

}
