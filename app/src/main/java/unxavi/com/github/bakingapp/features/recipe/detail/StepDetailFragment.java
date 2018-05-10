package unxavi.com.github.bakingapp.features.recipe.detail;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

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

    @BindView(R.id.videoView)
    VideoView videoView;

    @BindView(R.id.step_description)
    TextView stepDescription;

    Unbinder unbinder;

    private Step step;

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

        String description = step.getDescription();
        stepDescription.setText(description);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
