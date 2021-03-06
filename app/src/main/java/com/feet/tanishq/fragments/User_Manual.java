package com.feet.tanishq.fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.feet.tanishq.R;
import com.feet.tanishq.Tanishq_Screen;
import com.feet.tanishq.YouTuber;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.UserDetails;
import com.google.android.gms.analytics.HitBuilders;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link User_Manual#newInstance} factory method to
 * create an instance of this fragment.
 */
public class User_Manual extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public User_Manual() {
        // Required empty public constructor
    }

    public static User_Manual newInstance(){
        User_Manual user_manual_fragment=new User_Manual();
        return user_manual_fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment User_Manual.
     */
    // TODO: Rename and change types and number of parameters
    public static User_Manual newInstance(String param1, String param2) {
        User_Manual fragment = new User_Manual();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Tanishq_Screen.tracker.setScreenName("User Manual Screen");
        Tanishq_Screen.tracker.send(new HitBuilders.ScreenViewBuilder().build());

    }


    TextView tv_userman,tv_userman_header;
//    VideoView vv_tanishq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user__manual, container, false);
        tv_userman=(TextView) view.findViewById(R.id.tv_userman);
        tv_userman_header=(TextView) view.findViewById(R.id.tv_userman_header);

        tv_userman.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_userman_header.setTypeface(AsifUtils.getRaleWay_Bold(getContext()));



        tv_userman_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchYoutubeVideo();
            }
        });

        return view;
    }

    public void watchYoutubeVideo(){
        UserDetails details=new UserDetails(getContext());
        String url=details.getDemoUrl();
        try {
            Intent i = new Intent(getActivity(), YouTuber.class);
            startActivity(i);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            startActivity(intent);
        }
    }

}
