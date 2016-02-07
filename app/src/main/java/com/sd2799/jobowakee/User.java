package com.sd2799.jobowakee;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * Created by fexofenadine180mg on 11/11/15.
 */
public class User implements Parcelable {

    private UUID id;
    private String email;
    private String username;
    private String password;

    public User() {
        id = UUID.randomUUID();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setEmail(String e) {
        email = e;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String u) {
        username = u;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String p) {
        password = p;
    }

    public String getPassword() {
        return password;
    }

    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id.toString());
        out.writeString(email);
        out.writeString(username);
        out.writeString(password);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        id = UUID.fromString(in.readString());
        email = in.readString();
        username = in.readString();
        password = in.readString();
    }

}
