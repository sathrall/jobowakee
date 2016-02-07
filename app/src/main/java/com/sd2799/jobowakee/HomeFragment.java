package com.sd2799.jobowakee;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by fexofenadine180mg on 11/11/15.
 */
public class HomeFragment extends Fragment {

    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable("user_data");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, parent, false);

        TextView label = (TextView) v.findViewById(R.id.label);
        label.setText("Welcome, " + user.getUsername() + "!");

        return v;
    }

}
