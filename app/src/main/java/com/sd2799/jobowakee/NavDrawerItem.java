package com.sd2799.jobowakee;

/**
 * Created by fexofenadine180mg on 11/10/15.
 */
public class NavDrawerItem {

    private boolean showNotify;
    private String title;

    public NavDrawerItem() {
        //
    }

    public NavDrawerItem(boolean s, String t) {
        showNotify = s;
        title = t;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean s) {
        showNotify = s;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String t) {
        title = t;
    }

}
