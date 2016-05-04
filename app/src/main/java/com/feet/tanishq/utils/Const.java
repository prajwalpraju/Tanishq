package com.feet.tanishq.utils;

public class Const {
	
	public static final String URL_MAIN="https://www.tanishq.co.in/";
	
	public static final String URL="url";
	public static final String FRAG_All_COLL="all_collection";
	public static final String FRAG_WISH_LIST="wish_list";
	public static final String FRAG_COMPARE_LIST="compare";
	public static final String FRAG_SUB_COLL="sub_collection";
	public static final String FRAG_FILTER="filter_product";
	public static final String FRAG_PAGERFILTER="pager_filter_product";

	public static final String BASEAUTH_USERNAME="tanishqapiroot";
	public static final String BASEAUTH_PASSWORD="w0D0YdJtt";

    public static final String STORE_API=URL_MAIN+"storesapi/";
    public static final String USER_LOGIN=STORE_API+"userlogin?";
    public static final String OTP_VERIFY=STORE_API+"otpcodeverification?";
    public static final String LOGOUT=STORE_API+"signout?";
    public static final String ALL_COLLECTIONS=STORE_API+"tanishqcollections?";
	public static final String COLLECTION_CATEGORY=STORE_API+"tanishqcollectioncategories?";
	public static final String PRODUCT_LIST=STORE_API+"tanishqproductslist2?";


	public class Params{
		public static final String ID="id";
		public static final String USERNAME="username";
		public static final String MOBILE="mobile";
		public static final String OTP_CODE="otpcode";
		public static final String COLLECTIONID="collectionid";
		public static final String JEWELLERY="jewellery";
		public static final String MATERIAL="material";
		public static final String OCCASSION="occassion";
		public static final String PAGENO="pageno";
	}

    public class ServiceCode{
        public static final int USERLOGIN=1;
        public static final int OTP_VERIFY=2;
        public static final int LOGOUT=3;
        public static final int ALL_COLLECTIONS=4;
        public static final int COLLECTION_CATEGORY=5;
        public static final int PRODUCT_LIST=6;
    }
}
