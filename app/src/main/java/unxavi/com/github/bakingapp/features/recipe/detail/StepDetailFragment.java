package unxavi.com.github.bakingapp.features.recipe.detail;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

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
public class StepDetailFragment extends Fragment {

    public static final String BAKING_APP_USER_AGENT = "BakingAPPUserAgent";
    @BindView(R.id.playerView)
    PlayerView playerView;

    @BindView(R.id.step_description)
    TextView stepDescription;

    Unbinder unbinder;

    private Step step;
    private SimpleExoPlayer simpleExoPlayer;

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

        return rootView;
    }

    private void renderUI() {
        stepDescription.setText(step.getDescription());
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
//            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
//                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                simpleExoPlayer.prepare(mediaSource);
                simpleExoPlayer.setPlayWhenReady(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        initPlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void releasePlayer() {
        simpleExoPlayer.stop();
        simpleExoPlayer.release();
        simpleExoPlayer = null;

    }
}
