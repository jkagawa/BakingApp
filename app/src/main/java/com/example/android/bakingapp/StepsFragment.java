package com.example.android.bakingapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joshua on 7/6/2018.
 */

public class StepsFragment extends Fragment {

    private String mRecipeName;
    private String mStepsID;
    private String mStepsShortDescription;
    private String mStepsDescription;
    private String mStepsVideoURL;
    private String mStepsThumbnailURL;

    @BindView(R2.id.steps_video_player) SimpleExoPlayerView mPlayerView;
    @BindView(R2.id.steps_short_description) TextView mStepsShortDescriptionView;
    @BindView(R2.id.steps_description) TextView mStepsDescriptionView;

    private SimpleExoPlayer mExoplayer;

    private long playerPosition;
    private boolean getPlayerWhenReady = true;

    private String PLAYER_POSITION = "player_position";
    private String PLAYER_STATE = "player_state";

    private Toast mToast;

    public StepsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);

        ButterKnife.bind(this, rootView);

        mPlayerView = rootView.findViewById(R.id.steps_video_player);
        mStepsShortDescriptionView = rootView.findViewById(R.id.steps_short_description);
        mStepsDescriptionView = rootView.findViewById(R.id.steps_description);

        if(DetailActivity.mTwoPane) {
            mStepsVideoURL = DetailActivity.mStepsVideoURLList.get(DetailActivity.mPosition);
            mStepsShortDescription = DetailActivity.mStepsShortDescriptionList.get(DetailActivity.mPosition);
            mStepsDescription = DetailActivity.mStepsDescriptionList.get(DetailActivity.mPosition);
        }
        else {
            mStepsVideoURL = StepsActivity.mStepsVideoURL;
            mStepsShortDescription = StepsActivity.mStepsShortDescription;;
            mStepsDescription = StepsActivity.mStepsDescription;;
        }


        getPlayerWhenReady = true;

        if(savedInstanceState!=null) {
            playerPosition = savedInstanceState.getLong(PLAYER_POSITION);
            getPlayerWhenReady = savedInstanceState.getBoolean(PLAYER_STATE);
        }

        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.no_recipe_image));
        initializePlayer(Uri.parse(mStepsVideoURL), savedInstanceState);

        mStepsShortDescriptionView.setText(mStepsShortDescription);
        mStepsDescriptionView.setText(mStepsDescription);

        return rootView;
    }

    private void initializePlayer(Uri mediaUri, Bundle savedInstanceState) {
        if(mExoplayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();
            mExoplayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            if(playerPosition > 0) {
                mExoplayer.seekTo(playerPosition);
            }
            mExoplayer.setPlayWhenReady(getPlayerWhenReady);
            mPlayerView.setPlayer(mExoplayer);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, dataSourceFactory, extractorsFactory, null, null);
            mExoplayer.prepare(mediaSource);
        }
    }

    private void releasePlayer() {
        mExoplayer.stop();
        mExoplayer.release();
        mExoplayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        playerPosition = mExoplayer.getCurrentPosition();
        getPlayerWhenReady = mExoplayer.getPlayWhenReady();
        outState.putLong(PLAYER_POSITION, playerPosition);
        outState.putBoolean(PLAYER_STATE, getPlayerWhenReady);
    }

}
