package com.feet.tanishq.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.feet.tanishq.R;
import com.feet.tanishq.utils.AsifUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedBack.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FeedBack extends Fragment {


    public FeedBack() {
        // Required empty public constructor
    }


    public static FeedBack newInstance(){
        FeedBack help_fragment=new FeedBack();
        return help_fragment;
    }

    TextView tv_feed,tv_subject,tv_message;
    Button bt_send,bt_cancel;
    EditText et_subject,et_message;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_feedback, container, false);

        tv_feed=(TextView) view.findViewById(R.id.tv_feed);
        tv_subject=(TextView) view.findViewById(R.id.tv_subject);
        tv_message=(TextView) view.findViewById(R.id.tv_message);

        bt_send=(Button) view.findViewById(R.id.bt_send);
        bt_cancel=(Button) view.findViewById(R.id.bt_cancel);

        et_subject=(EditText) view.findViewById(R.id.et_subject);
        et_message=(EditText) view.findViewById(R.id.et_message);

        tv_feed.setTypeface(AsifUtils.getRaleWay_Bold(getContext()));
        tv_subject.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_message.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));

        bt_send.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        bt_cancel.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));

        et_subject.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        et_message.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));




        return view;
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
