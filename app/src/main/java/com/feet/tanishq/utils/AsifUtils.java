package com.feet.tanishq.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;


/**
 * @author Asif Sb
 */
@SuppressLint("NewApi")
public class AsifUtils {
	static float density = 1;
	private static ProgressDialog mProgressDialog;
	private static Dialog mDialog;
	private int driverId = 0;

	public static int getScreenWidth(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		return display.getWidth();
	}

	public static Typeface getRaleWay_Bold(Context context){
		Typeface tf_raleway_bold=Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Bold.otf");
;		return tf_raleway_bold;
	}
	public static Typeface getRaleWay_Medium(Context context){
		Typeface tf_raleway_medium=Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Medium.otf");
;		return tf_raleway_medium;
	}
	public static Typeface getRaleWay_SemiBold(Context context){
		Typeface tf_raleway_semibold=Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-SemiBold.otf");
;		return tf_raleway_semibold;
	}
	public static Typeface getRaleWay_Thin(Context context){
		Typeface tf_raleway_thin=Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Thin.otf");
;		return tf_raleway_thin;
	}


	public static InputFilter filter = new InputFilter() {

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

			String blockCharacterSet = " ";

			if (source != null && blockCharacterSet.contains(("" + source))) {
				return "";
			}
			return null;
		}

	};

	public static void showSimpleProgressDialog(Context context, String title, String msg, boolean isCancelable) {
		try {
			if (mProgressDialog == null) {
				mProgressDialog = ProgressDialog.show(context, title, msg);
				mProgressDialog.setCancelable(isCancelable);
			}

			if (!mProgressDialog.isShowing()) {
				mProgressDialog.show();
			}

		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();
		} catch (RuntimeException re) {
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public static void showSimpleProgressDialog(Context context) {
		showSimpleProgressDialog(context, null, "Loading...", false);
	}

	// public static void showCustomeProgressDialog(Context context, String msg,
	// boolean isCancelable) {
	// try {
	//
	// if (msg == null || msg.equalsIgnoreCase("")) {
	// msg = "Loading...";
	// }
	// if (mDialog == null) {
	// mDialog = new Dialog(context);
	// mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// mDialog.setCancelable(isCancelable);
	// mDialog.setContentView(R.layout.custom_progress_dialog);
	// }
	// TextView txtView = (TextView) mDialog.findViewById(R.id.textView);
	// txtView.setText(msg);
	// if (!mDialog.isShowing())
	// mDialog.show();
	//
	// } catch (IllegalArgumentException ie) {
	// ie.printStackTrace();
	// } catch (RuntimeException re) {
	// re.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public static void removeSimpleProgressDialog() {
		try {
			if (mProgressDialog != null) {
				if (mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
					mProgressDialog = null;
				}
			}
		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();

		} catch (RuntimeException re) {
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// public static void removeCustomeProgressDialog() {
	// try {
	// if (mDialog != null) {
	// if (mDialog.isShowing()) {
	// mDialog.dismiss();
	// mDialog = null;
	// }
	// }
	// } catch (IllegalArgumentException ie) {
	// ie.printStackTrace();
	//
	// } catch (RuntimeException re) {
	// re.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	public static boolean validateResponse(Context context,String response){
		boolean validate=false;
		try {
			int status=new JSONObject(response).getInt("status");
			Log.d("response", "validateResponse: "+response);
			Toast.makeText(context,new JSONObject(response).getString("message"),Toast.LENGTH_SHORT).show();
			switch (status){
				case 200:
				//success message
					validate=true;
					break;
				case 400:
				//failure message
					break;

				case 404:
				//method not found(aka page)

					break;

				case 406:
				//method not allowed
					break;

				default:

					break;

			}

			Log.d("response", "validateResponse: "+status+" val="+validate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return validate;
	}

	public static void showNetworkErrorMessage(final Context context) {
		Builder dlg = new Builder(context);
		dlg.setCancelable(false);
		dlg.setTitle("Error");
		dlg.setMessage("Network error has occured. Please check the network status of your phone and retry");
		dlg.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		dlg.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((Activity) context).finish();
				System.exit(0);
			}
		});
		dlg.show();
	}

	public static void showOkDialog(String title, String msg, Activity act) {
		Builder dialog = new Builder(act);
		if (title != null) {

			TextView dialogTitle = new TextView(act);
			dialogTitle.setText(title);
			dialogTitle.setPadding(10, 10, 10, 10);
			dialogTitle.setGravity(Gravity.CENTER);
			dialogTitle.setTextColor(Color.WHITE);
			dialogTitle.setTextSize(20);
			dialog.setCustomTitle(dialogTitle);

		}
		if (msg != null) {
			dialog.setMessage(msg);
		}
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		AlertDialog dlg = dialog.show();
		TextView messageText = (TextView) dlg.findViewById(android.R.id.message);
		messageText.setGravity(Gravity.CENTER);

	}

	public static float getDisplayMetricsDensity(Context context) {
		density = context.getResources().getDisplayMetrics().density;

		return density;
	}

	public static int getPixel(Context context, int p) {
		if (density != 1) {
			return (int) (p * density + 0.5);
		}
		return p;
	}

	public static Animation FadeAnimation(float nFromFade, float nToFade) {
		Animation fadeAnimation = new AlphaAnimation(nToFade, nToFade);

		return fadeAnimation;
	}

	public static Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
				0.0f);

		return inFromRight;
	}

	public static Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT,
				0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);

		return inFromLeft;
	}

	public static Animation inFromBottomAnimation() {
		Animation inFromBottom = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT,
				0.0f);

		return inFromBottom;
	}

	public static Animation outToLeftAnimation() {
		Animation outToLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
				-1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);

		return outToLeft;
	}

	public static Animation outToRightAnimation() {
		Animation outToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
				+1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);

		return outToRight;
	}

	public static Animation outToBottomAnimation() {
		Animation outToBottom = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
				0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, +1.0f);

		return outToBottom;
	}

	public static boolean isNetworkAvailable(Activity activity) {
		ConnectivityManager connectivity = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean eMailValidation(String emailstring) {
		if (null == emailstring || emailstring.length() == 0) {
			return false;
		}
		Pattern emailPattern = Pattern.compile(
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher emailMatcher = emailPattern.matcher(emailstring);
		return emailMatcher.matches();
	}

	public static boolean validateNumber(String S) {
		if (null == S || S.length() == 0) {
			return false;
		}
		String Regex = "[1-9]\\d{2}-[1-9]\\d{2}-\\d{4}";
		String PhoneDigits = S.replaceAll(Regex, "");
		return (PhoneDigits.length() != 10);
	}

	public static boolean validateLetters(String txt) {

		String regx = "^[\\p{L}\\d .'-_,]+$";
		Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(txt);
		return matcher.find();

	}

	//
	// public static SimpleDateFormat inputDateTime = new SimpleDateFormat(
	// "yyyy-MM-dd HH:mm:ss");
	// public static SimpleDateFormat inputDate = new SimpleDateFormat(
	// "yyyy-MM-dd");
	// public static SimpleDateFormat output = new SimpleDateFormat(
	// "MMMM dd, yyyy");
	// public static SimpleDateFormat outputDateFormate = new SimpleDateFormat(
	// "MMM dd, yyyy");
	// public static SimpleDateFormat simpleFormate = new SimpleDateFormat(
	// "yyyyMMdd");

	public static String urlBuilderForGetMethod(Map<String, String> g_map) {

		StringBuilder sbr = new StringBuilder();
		int i = 0;
		if (g_map.containsKey("url")) {
			sbr.append(g_map.get("url"));
			g_map.remove("url");
		}
		for (String key : g_map.keySet()) {
			if (i != 0) {
				sbr.append("&");
			}
			sbr.append(key + "=" + g_map.get(key));
			i++;
		}
		System.out.println("Builder url = " + sbr.toString());
		return sbr.toString();
	}

	public static int isValidate(String... fields) {
		if (fields == null) {
			return 0;
		}

		for (int i = 0; i < fields.length; i++) {
			if (TextUtils.isEmpty(fields[i])) {
				return i;
			}

		}
		return -1;
	}

	public static void showToast(String msg, Context ctx) {
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}

	public static String UppercaseFirstLetters(String str) {
		boolean prevWasWhiteSp = true;
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isLetter(chars[i])) {
				if (prevWasWhiteSp) {
					chars[i] = Character.toUpperCase(chars[i]);
				}
				prevWasWhiteSp = false;
			} else {
				prevWasWhiteSp = Character.isWhitespace(chars[i]);
			}
		}
		return new String(chars);
	}

		public static void buttonEffect(ImageButton button, final int alpha) {

		button.setOnTouchListener(new OnTouchListener() {

			@Override
			@SuppressWarnings("deprecation")
			public boolean onTouch(View v, MotionEvent event) {
				ImageButton btn = (ImageButton) v;
				switch (event.getAction()) {

					case MotionEvent.ACTION_DOWN: {

						if (Build.VERSION.SDK_INT > 15) {
							btn.setImageAlpha(alpha);

						} else {
							btn.setAlpha(alpha);
						}

						break;
					}
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:

						if (Build.VERSION.SDK_INT > 15) {

							btn.setImageAlpha(255);
						} else {
							btn.setAlpha(255);
						}

						break;
				}
				return false;
			}
		});

	}

	public static final SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	}

	public static String convertToUTF8(String s) {
		String out = null;
		try {
			out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
		} catch (java.io.UnsupportedEncodingException e) {
			return null;
		}
		return out;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}


}