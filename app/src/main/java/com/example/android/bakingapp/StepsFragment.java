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

    private Toast mToast;

    public StepsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.no_recipe_image));
        initializePlayer(Uri.parse(mStepsVideoURL));

        mStepsShortDescriptionView.setText(mStepsShortDescription);
        mStepsDescriptionView.setText(mStepsDescription);

        mToast.makeText(getContext(), StepsActivity.mStepsShortDescription, Toast.LENGTH_SHORT).show();

        return rootView;
    }

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        mPlayerView = findViewById(R.id.steps_video_player);
        mStepsShortDescriptionView = findViewById(R.id.steps_short_description);
        mStepsDescriptionView = findViewById(R.id.steps_description);

        Intent intentFromMainActivity = getIntent();

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            mRecipeName = intentFromMainActivity.getStringExtra(MainActivity.EXTRA_RECIPE_NAME_KEY);
            mStepsID = intentFromMainActivity.getStringExtra(DetailActivity.EXTRA_STEPS_ID_KEY);
            mStepsShortDescription = intentFromMainActivity.getStringExtra(DetailActivity.EXTRA_STEPS_SHORT_DESCRIPTION_KEY);
            mStepsDescription = intentFromMainActivity.getStringExtra(DetailActivity.EXTRA_STEPS_DESCRIPTION_KEY);
            mStepsVideoURL = intentFromMainActivity.getStringExtra(DetailActivity.EXTRA_STEPS_VIDEO_URL_KEY);
            mStepsThumbnailURL = intentFromMainActivity.getStringExtra(DetailActivity.EXTRA_STEPS_THUMBNAIL_URL_KEY);

            setTitle(mRecipeName);

            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(),R.drawable.no_recipe_image));
            initializePlayer(Uri.parse(mStepsVideoURL));

            mStepsShortDescriptionView.setText(mStepsShortDescription);
            mStepsDescriptionView.setText(mStepsDescription);

        }

    }*/

    private void initializePlayer(Uri mediaUri) {
        if(mExoplayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();
            mExoplayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoplayer);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, dataSourceFactory, extractorsFactory, null, null);
            mExoplayer.prepare(mediaSource);
            mExoplayer.setPlayWhenReady(true);

            //mToast.makeText(this, mStepsVideoURL, Toast.LENGTH_SHORT).show();
        }
    }

    private void releasePlayer() {
        mExoplayer.stop();
        mExoplayer.release();
        mExoplayer = null;
    }

}
