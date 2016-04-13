package com.feet.tanishq.utils;

public class Const {
	
	public static final String URL_MAIN="https://www.tanishq.co.in/";
	
	public static final String URL="url";
	public static final String FRAG_All_COLL="all_collection";
	public static final String FRAG_WISH_LIST="wish_list";

	public static final String BASEAUTH_USERNAME="tanishqapiroot";
	public static final String BASEAUTH_PASSWORD="w0D0YdJtt";

    public static final String STORE_API=URL_MAIN+"storesapi/";
    public static final String USER_LOGIN=STORE_API+"userlogin?";
    public static final String OTP_VERIFY=STORE_API+"otpcodeverification?";
    public static final String LOGOUT=STORE_API+"signout?";
    public static final String ALL_COLLECTIONS=STORE_API+"tanishqcollections?";


	public class Params{
		public static final String ID="id";
		public static final String USERNAME="username";
		public static final String MOBILE="mobile";
		public static final String OTP_CODE="otpcode";
	}

    public class ServiceCode{
        public static final int USERLOGIN=1;
        public static final int OTP_VERIFY=2;
        public static final int LOGOUT=3;
        public static final int ALL_COLLECTIONS=4;
    }
}
