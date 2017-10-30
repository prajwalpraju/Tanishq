package com.feet.tanishq.utils;

public class Const {

	public static final int VERSION_CODE = 7;
	public static final String VERSION_NAME = "1.5";




	public static final String URL_MAIN="https://www.tanishq.co.in/";
	public static final String BASEAUTH_USERNAME="tanishqapiroot";
	public static final String BASEAUTH_PASSWORD="w0D0YdJtt";

	public static final String URL="url";
	public static final String FRAG_All_COLL="all_collection";

	public static final String FRAG_MAIN_COLL="main_collection";
	public static final String FRAG_WISH_LIST="wish_list";
	public static final String FRAG_COMPARE_LIST="compare";
	public static final String FRAG_SUB_COLL="sub_collection";
	public static final String FRAG_FILTER="filter_product";
	public static final String FRAG_SEARCH="filter_Search";
	public static final String FRAG_FILTER_FROM_CLICK="frag_filter_from_click";
	public static final String FRAG_HELP="help";
	public static final String FRAG_FEEDBACK="feedback";
	public static final String FRAG_MAIL="mail";
	public static final String FRAG_USERMAN="user_manual";
	public static final String FRAG_PAGERFILTER="pager_filter_product";


	    public static final String MAIN_STORE_API=URL_MAIN+"storesapi/";


//	public static final String STORE_API="https://www.tanishq.co.in/storesapi-v2/";
	public static final String STORE_API="https://www.tanishq.co.in/storesapi-v3/";
//	public static final String STORE_API="https://www.tanishq.co.in/storesapi-beta/";


	public static final String USER_LOGIN=STORE_API+"userlogin";
	public static final String OTP_VERIFY=STORE_API+"otpcodeverification?";
	public static final String LOGOUT=STORE_API+"signout?";
	public static final String FEATURED=STORE_API+"featured?";
	public static final String CATEGORYLIST_URL=STORE_API+"tanishqgetcategorylist";
	public static final String PRODUCT_LIST=STORE_API+"tanishqgetproducts";
	public static final String CATALOGLIST_URL=STORE_API+"tanishqgetcataloguelist";
	public static final String COLLECTIONLIST_URL =STORE_API+"tanishqgetcollectionlist";
	public static final String MAINCOLLECTIONLIST_URL =STORE_API+"tanishqdashboardcontents";
	public static final String FILTER_URL = STORE_API+"tanishqgetfilters";
	public static final String SEARCH_URL = STORE_API+"tanishqsearchresults";
	public static final String FEEDBACK=STORE_API+"tanishquserfeedback?";
	public static final String STORELIST_API_URL =STORE_API+"tanishqgetavailablestorelist";
	public static final String SENDMAILTOSTORELIST=STORE_API+"tanishqsendmailtostorelist";

	public static final String MAIL_INFO=STORE_API+"tanishqsavecustomerinfo?";


	public static final String GCM_URL="https://www.tanishq.co.in/storesapi-beta/storedeviceid";


	public class Resolution{
		public static final double MDPI=1;
		public static final double HDPI= 1.5;
		public static final double XHDPI=2;
		public static final double XXHPDI=3;
		public static final double XXXHDPI=4;

		public static final String MDPI_TXT="mdpi";
		public static final String HDPI_TXT="hdpi";
		public static final String XHDPI_TXT="xhdpi";
		public static final String XXHPDI_TXT="xxhdpi";
		public static final String XXXHDPI_TXT="xxxhdpi";
	}


	public class Params{
		public static final String ID="id";
		public static final String STOREMOBILENO="storemobileno";
		public static final String STORENAME="storename";
		public static final String CATEGORYID="categoryid";
//		public static final String COLLECTIONID="collectionid";
		public static final String ITEM_ID="itemid";
		public static final String SKU="sku";
		public static final String FILTERPARAMETER="filterparameter";
		public static final String USERNAME="username";
		public static final String MOBILE="mobile";
		public static final String DEVICE_ID="deviceid";
		public static final String DEVICETYPE="devicetype";
		public static final String ANDROID="1";
		public static final String OTP_CODE="otpcode";
		public static final String COLLECTION="collection";
		public static final String KARATAGE="karatage";
		public static final String PRODUCTSTYPE ="productstype";
		public static final String STORELIST ="storelist";
		public static final String GRAMMAGE="grammage";
		public static final String COLLECTIONID="collectionid";
		public static final String CATALOGUEID="catalogueid";
		public static final String JEWELLERY="jewellery";
		public static final String MATERIAL="material";
		public static final String OCCASSION="occassion";
		public static final String PRICEBAR="pricebar";
		public static final String NEXT_PAGE="next_page";
		public static final String SEARCHTERM="searchterm";
		public static final String FEEDBACK="feedback";
		public static final String SUBJECT="subject";
		public static final String EMAILID="emailid";
		public static final String WISHLIST="wishlist";
	}

	public class ServiceCode{
		public static final int USERLOGIN=1;
		public static final int OTP_VERIFY=2;
		public static final int LOGOUT=15;
		public static final int STORE_LIST=16;
		public static final int CATEGORYLIST=4;
		public static final int COLLECTIONLIST=12;
		public static final int FILTER=13;
		public static final int STORELIST=14;
		public static final int MAIN_COLLECTIONS=11;
		public static final int COLLECTION_CATEGORY=5;
		public static final int PRODUCT_LIST=6;
		public static final int PRODUCT_LIST_FROM_CLICK=5;
		public static final int COATALOGUE=7;
		public static final int FEEDBACK=8;
		public static final int MAIL_INFO=9;
		public static final int FEATURED=10;
		public static final int GCM_TOKEN=17;
	}
}
