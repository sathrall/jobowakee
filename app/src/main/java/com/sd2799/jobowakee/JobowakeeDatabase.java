package com.sd2799.jobowakee;

import android.provider.BaseColumns;

import java.util.UUID;

/**
 * Created by fexofenadine180mg on 11/11/15.
 */
public final class JobowakeeDatabase {

    public static final class Users implements BaseColumns {

        private Users() {
            /* intentionally left blank */
        }

        public static final String USERS_TABLE_NAME = "table_users";
        public static final String USER_ID = "user_id";
        public static final String EMAIL = "email";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String DEFAULT_SORT_ORDER = "username ASC";

    }

    public static final class Employers implements BaseColumns {

        private Employers() { /* intentionally left blank */ }

        public static final String EMPLOYERS_TABLE_NAME = "table_employers";
        public static final String USER_ID = "user_id";
        public static final String EMAIL = "email";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String COMPANY_NAME = "company_name";
        public static final String COMPANY_ADDRESS = "company_address";
        public static final String COMPANY_PHONE = "company_phone";
        public static final String DEFAULT_SORT_ORDER = "company_name ASC";

    }

}
