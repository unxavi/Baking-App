package unxavi.com.github.bakingapp.features.recipe.detail;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import unxavi.com.github.bakingapp.R;
import unxavi.com.github.bakingapp.model.Step;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {

    public static final String BAKING_APP_USER_AGENT = "BakingAPPUserAgent";
    public static final String VIDEO_POSITION_KEY = "VIDEO_POSITION";
    private static final String TAG = "StepDetailFragment";

    @BindView(R.id.playerView)
    PlayerView playerView;

    @Nullable
    @BindView(R.id.step_description)
    TextView stepDescription;

    @Nullable
    @BindView(R.id.imageView)
    ImageView imageView;


    Unbinder unbinder;

    private Step step;
    private SimpleExoPlayer simpleExoPlayer;
    private long videoPosition;
    private MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder stateBuilder;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(Step.STEP_KEY)) {
            // Load the step content specified by the fragment
            // arguments.
            step = getArguments().getParcelable(Step.STEP_KEY);
            Activity activity = this.getActivity();
            Toolbar toolbar = null;
            if (activity != null) {
                toolbar = activity.findViewById(R.id.toolbar);
            }
            if (toolbar != null) {
                toolbar.setTitle(step.getShortDescription());
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        renderUI();

        // Initialize the Media Session.
        initializeMediaSession();

        return rootView;
    }

    private void initializeMediaSession() {
        if (getContext() != null) {
            // Create a MediaSessionCompat.
            mediaSessionCompat = new MediaSessionCompat(getContext(), TAG);

            // Enable callbacks from MediaButtons and TransportControls.
            mediaSessionCompat.setFlags(
                    MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                            MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

            // Do not let MediaButtons restart the player when the app is not visible.
            mediaSessionCompat.setMediaButtonReceiver(null);

            // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
            stateBuilder = new PlaybackStateCompat.Builder()
                    .setActions(
                            PlaybackStateCompat.ACTION_PLAY |
                                    PlaybackStateCompat.ACTION_PAUSE |
                                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                    PlaybackStateCompat.ACTION_PLAY_PAUSE);

            mediaSessionCompat.setPlaybackState(stateBuilder.build());


            // MySessionCallback has methods that handle callbacks from a media controller.
            mediaSessionCompat.setCallback(new MySessionCallback());

            // Start the Media Session since the activity is active.
            mediaSessionCompat.setActive(true);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(VIDEO_POSITION_KEY, videoPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            videoPosition = savedInstanceState.getLong(VIDEO_POSITION_KEY);
        }
    }

    private void renderUI() {
        if (stepDescription != null) {
            stepDescription.setText(step.getDescription());
        }
        if (imageView != null && !TextUtils.isEmpty(step.getThumbnailURL())) {
            Picasso.get().load(step.getThumbnailURL()).placeholder(R.drawable.ic_recipe_silverwear).error(R.drawable.ic_recipe_silverwear).into(imageView);
        }
    }

    private void initPlayer() {
        if (TextUtils.isEmpty(step.getVideoURL())) {
            playerView.setVisibility(View.GONE);
        } else {
            playerView.setVisibility(View.VISIBLE);
            Uri mediaUri = Uri.parse(step.getVideoURL());
            if (simpleExoPlayer == null && getContext() != null) {
                DefaultTrackSelector defaultTrackSelector = new DefaultTrackSelector();
                DefaultLoadControl defaultLoadControl = new DefaultLoadControl();
                RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
                simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, defaultTrackSelector, defaultLoadControl);
                playerView.setPlayer(simpleExoPlayer);
                String userAgent = Util.getUserAgent(getContext(), BAKING_APP_USER_AGENT);
                ExtractorMediaSource.Factory factory = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(getContext(), userAgent));
                ExtractorMediaSource mediaSource = factory.createMediaSource(mediaUri);
                simpleExoPlayer.addListener(this);
                simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
                simpleExoPlayer.prepare(mediaSource);
                simpleExoPlayer.setPlayWhenReady(true);
                simpleExoPlayer.seekTo(videoPosition);
            }
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     *
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (simpleExoPlayer != null) {
            if ((playbackState == Player.STATE_READY) && playWhenReady) {
                stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                        simpleExoPlayer.getCurrentPosition(), 1f);
            } else if ((playbackState == Player.STATE_READY)) {
                stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                        simpleExoPlayer.getCurrentPosition(), 1f);
            }
            mediaSessionCompat.setPlaybackState(stateBuilder.build());
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onPause() {
        super.onPause();
        mediaSessionCompat.setActive(false);
        if (simpleExoPlayer != null) {
            videoPosition = simpleExoPlayer.getContentPosition();
        }
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        mediaSessionCompat.setActive(true);
        initPlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            if (simpleExoPlayer != null) {
                simpleExoPlayer.setPlayWhenReady(true);
            }
        }

        @Override
        public void onPause() {
            if (simpleExoPlayer != null) {
                simpleExoPlayer.setPlayWhenReady(false);
            }
        }

        @Override
        public void onSkipToPrevious() {
            if (simpleExoPlayer != null) {
                simpleExoPlayer.seekTo(0);
            }
        }
    }
}
