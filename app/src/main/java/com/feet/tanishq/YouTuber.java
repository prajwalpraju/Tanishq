package com.feet.tanishq;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.feet.tanishq.utils.UserDetails;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YouTuber extends YouTubeBaseActivity {


    String api_key = "AIzaSyAmECP-YgMOrSvW5oCbQjcJLunNjtrHhLo";

//    String api_key = "AIzaSyDCfTt_0Zf17s5f5vonY-r94aqxx31cSSo";

    YouTubePlayerView yu_tube;
    UserDetails details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_you_tuber);
        yu_tube = (YouTubePlayerView) findViewById(R.id.yu_tube);
        yu_tube.initialize(api_key, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                if (!b) {
                    youTubePlayer.loadVideo(details.getVideo_idUrl());
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(YouTuber.this, youTubeInitializationResult.toString(), Toast.LENGTH_SHORT).show();
                Log.e("utube", "onInitializationFailure: " + details.getVideo_idUrl());
            }
        });
        details = new UserDetails(getApplicationContext());
    }


//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//    }

}



//youTubePlayerFragment.initialize(YoutubeConnector.KEY, new YouTubePlayer.OnInitializedListener() {
//@Override
//public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
//        youtubevideoplayer = youTubePlayer;
//
//        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
//        youTubePlayer.setPlaybackEventListener(playbackEventListener);
//
//
//        if (!b) {
//        youTubePlayer.cueVideo(videoId);