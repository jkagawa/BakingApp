package com.example.android.bakingapp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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

public class StepsActivity extends AppCompatActivity {

    public static String mRecipeName;
    public static String mStepsID;
    public static String mStepsShortDescription;
    public static String mStepsDescription;
    public static String mStepsVideoURL;
    public static String mStepsThumbnailURL;

    private SimpleExoPlayer mExoplayer;
    private SimpleExoPlayerView mPlayerView;

    private Toast mToast;

    StepsFragment stepsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        if(stepsFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .remove(stepsFragment)
                    .commit();
        }

        stepsFragment = new StepsFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.steps_fragment, stepsFragment)
                .commit();

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
        }

    }

    private void releasePlayer() {
        mExoplayer.stop();
        mExoplayer.release();
        mExoplayer = null;
    }

}
