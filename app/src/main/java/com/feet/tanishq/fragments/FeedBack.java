package com.feet.tanishq.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.feet.tanishq.R;
import com.feet.tanishq.Tanishq_Screen;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.UserDetails;
import com.feet.tanishq.utils.VolleyHttpRequest;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedBack.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FeedBack extends Fragment implements AsyncTaskCompleteListener,Response.ErrorListener {


    public FeedBack() {
        // Required empty public constructor
    }


    public static FeedBack newInstance(){
        FeedBack help_fragment=new FeedBack();
        return help_fragment;
    }

    TextView tv_feed,tv_subject,tv_message,tv_email,tv_name;
    Button bt_send,bt_cancel;
    EditText et_message,et_subject,et_email,et_name;
    RequestQueue requestQueue;
    AdapterCallback adapterCallback;

    @Override
    public void onResume() {
        super.onResume();
        AdapterCallback adapterCallback = (AdapterCallback) this.getActivity();
//        adapterCallback.setFilterToVisable();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tanishq_Screen.tracker.setScreenName("Feedback Screen");
        Tanishq_Screen.tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_feedback, container, false);

        tv_feed=(TextView) view.findViewById(R.id.tv_feed);
        tv_subject=(TextView) view.findViewById(R.id.tv_subject);
        tv_message=(TextView) view.findViewById(R.id.tv_message);
        tv_email=(TextView) view.findViewById(R.id.tv_email);
        tv_name=(TextView) view.findViewById(R.id.tv_name);

        this.adapterCallback= (AdapterCallback) getContext();
        requestQueue= Volley.newRequestQueue(getContext());

        bt_send=(Button) view.findViewById(R.id.bt_send);
        bt_cancel=(Button) view.findViewById(R.id.bt_cancel);

        et_message=(EditText) view.findViewById(R.id.et_message);
        et_subject=(EditText) view.findViewById(R.id.et_subject);
        et_email=(EditText) view.findViewById(R.id.et_email);
        et_name=(EditText) view.findViewById(R.id.et_name);

        tv_feed.setTypeface(AsifUtils.getRaleWay_Bold(getContext()));
        tv_subject.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_message.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_email.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_name.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));

        bt_send.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        bt_cancel.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));

        et_message.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        et_email.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        et_name.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));




        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFeedBack()) {
                    Tanishq_Screen.reportEventToGoogle("Feedback","Form fill","Send");
//                    Log.e("screenClick", "onClick: ---------------> Feedback,Form fill,Send" );
                    callFeedBackApi();
                }
            }
        });


        return view;
    }


    public void callFeedBackApi(){
        if (!AsifUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        AsifUtils.start(getContext());

        UserDetails user=new UserDetails(getContext());
        HashMap<String,String> map=new HashMap<String,String>();
        map.put(Const.URL,Const.FEEDBACK);
        map.put(Const.Params.ID, user.getUserId());
        map.put(Const.Params.SUBJECT, et_subject.getText().toString().trim());
        map.put(Const.Params.EMAILID, et_email.getText().toString().trim());
        map.put(Const.Params.USERNAME, et_name.getText().toString().trim());
        map.put(Const.Params.FEEDBACK, et_message.getText().toString().trim());

        requestQueue.add(new VolleyHttpRequest(Request.Method.POST,map,Const.ServiceCode.FEEDBACK,this,this));
    }


    public boolean validateFeedBack(){
        boolean isval=false;
        String sub=et_subject.getText().toString().trim();
        String msg=et_message.getText().toString().trim();
        String email=et_email.getText().toString().trim();
        String name=et_email.getText().toString().trim();
        if (name.length()==0&&name.isEmpty()){
            Toast.makeText(getContext(),"Please enter user name",Toast.LENGTH_SHORT).show();
        }else if(!AsifUtils.isValidEmail(email)){
            Toast.makeText(getContext(),"Please enter valid email id",Toast.LENGTH_SHORT).show();
        }else if (sub.length()==0&&sub.isEmpty()){
            Toast.makeText(getContext(),"Please enter subject",Toast.LENGTH_SHORT).show();
        }else if(msg.length()==0&&msg.isEmpty()){
            Toast.makeText(getContext(),"Please enter message",Toast.LENGTH_SHORT).show();
        }else{
            isval=true;
        }
        return isval;
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode){
            case Const.ServiceCode.FEEDBACK:
                Log.e("xres", "responce wish list "+response );
               if (AsifUtils.validateResponse(getContext(),response)){
                   try {
                       Toast.makeText(getContext(),new JSONObject(response).getString("message"),Toast.LENGTH_SHORT).show();
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   adapterCallback.onMethodCallback(3);
               }

                break;
        }
        AsifUtils.stop();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        AsifUtils.stop();
        AsifUtils.validateResponse(getContext(), error.getMessage());
    }

    // TODO: Rename method, update argument and hook method into UI event


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
