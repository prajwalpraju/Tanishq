package com.feet.tanishq;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;

/**
 * Created by asif on 05-04-2016.
 */
public abstract class CustomAppCompactActivity extends AppCompatActivity implements View.OnClickListener{

    private int mFragmentId = 0;
    private String mFragmentTag = null;

    protected abstract boolean isValidate();
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = null;
        if (mFragmentId > 0) {
            fragment = getSupportFragmentManager().findFragmentById(mFragmentId);
        } else if (mFragmentTag != null && !mFragmentTag.equalsIgnoreCase("")) {
            fragment = getSupportFragmentManager().findFragmentByTag(
                    mFragmentTag);
        }
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setFbTag(String tag) {
        mFragmentId = 0;
        mFragmentTag = tag;
    }

    public void startActivityForResult(Intent intent, int requestCode,
                                       int fragmentId) {
        mFragmentId = fragmentId;
        mFragmentTag = null;
        super.startActivityForResult(intent, requestCode);
    }

    public void startActivityForResult(Intent intent, int requestCode,
                                       String fragmentTag) {
        mFragmentTag = fragmentTag;
        mFragmentId = 0;
        super.startActivityForResult(intent, requestCode);
    }

    public void startActivityForResult(Intent intent, int requestCode,
                                       int fragmentId, Bundle options) {

        mFragmentId = fragmentId;
        mFragmentTag = null;
        super.startActivityForResult(intent, requestCode, options);
    }

    public void startActivityForResult(Intent intent, int requestCode,
                                       String fragmentTag, Bundle options) {

        mFragmentTag = fragmentTag;
        mFragmentId = 0;
        super.startActivityForResult(intent, requestCode, options);
    }

    public void startIntentSenderForResult(Intent intent, int requestCode,
                                           String fragmentTag, Bundle options) {

        mFragmentTag = fragmentTag;
        mFragmentId = 0;
        super.startActivityForResult(intent, requestCode, options);
    }


    public void addFragment(Fragment fragment,boolean addToBackStack,String tag){

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction ftrans=fragmentManager.beginTransaction();
        if (addToBackStack){
            ftrans.addToBackStack(tag);
        }
        ftrans.replace(R.id.fl_fragment, fragment, tag);
        ftrans.commitAllowingStateLoss();
    }

    public void removeAllFragment(Fragment replaceFragment,boolean addToBackStack, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        manager.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commit();
    }

    public void clearBackStackImmidiate() {

        FragmentManager manager = getSupportFragmentManager();

        manager.popBackStackImmediate(null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    public void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager
                    .getBackStackEntryAt(0);
            manager.popBackStack(first.getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//    }

    public void goToLoginScreen() {
        Intent i = new Intent(this, Login_Screen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();
    }
    public void clearAll() {
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
    }


    @Override
    public void onClick(View v) {

    }
}
