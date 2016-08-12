package com.feet.tanishq.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.feet.tanishq.R;
import com.feet.tanishq.Tanishq_Screen;
import com.feet.tanishq.YouTuber;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.utils.AsifUtils;
import com.google.android.gms.analytics.HitBuilders;

public class Help_Fragment extends Fragment {


    public Help_Fragment() {
        // Required empty public constructor
    }

    public static Help_Fragment newInstance(){
        Help_Fragment help_fragment=new Help_Fragment();
        return help_fragment;
    }

    TextView tv_help;
    Button bt_feed,bt_userman;
    AdapterCallback adapterCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tanishq_Screen.tracker.setScreenName("Help Screen");
        Tanishq_Screen.tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_help, container, false);
        tv_help=(TextView) view.findViewById(R.id.tv_help);
        bt_feed=(Button) view.findViewById(R.id.bt_feed);
        bt_userman=(Button) view.findViewById(R.id.bt_userman);
        bt_feed.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        bt_userman.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_help.setTypeface(AsifUtils.getRaleWay_Bold(getContext()));
        this.adapterCallback= (AdapterCallback) getContext();
        bt_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallback.onMethodCallback(1);
            }
        });

        bt_userman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), YouTuber.class);
                startActivity(intent);
//                adapterCallback.onMethodCallback(2);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

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
}
