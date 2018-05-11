package unxavi.com.github.bakingapp.features.recipe.detail;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import unxavi.com.github.bakingapp.R;
import unxavi.com.github.bakingapp.model.Recipe;
import unxavi.com.github.bakingapp.model.Step;

/**
 * An activity representing a single Step detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StepListActivity}.
 */
public class StepDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar detailToolbar;

    private Parcelable recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideStatusBarIfLandscape();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);
        setSupportActionBar(detailToolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        hideToolbarIfLandscape();

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra(Recipe.RECIPE_KEY);
        Step step = intent.getParcelableExtra(Step.STEP_KEY);
        if (recipe != null && step != null) {
            // savedInstanceState is non-null when there is fragment state
            // saved from previous configurations of this activity
            // (e.g. when rotating the screen from portrait to landscape).
            // In this case, the fragment will automatically be re-added
            // to its container so we don't need to manually add it.
            // For more information, see the Fragments API guide at:
            //
            // http://developer.android.com/guide/components/fragments.html
            //
            if (savedInstanceState == null) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                Bundle arguments = new Bundle();

                arguments.putParcelable(Step.STEP_KEY, step);

                StepDetailFragment fragment = new StepDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_detail_container, fragment)
                        .commit();
            }
        } else {
            closeOnError();
        }
    }

    private void hideToolbarIfLandscape() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            detailToolbar.setVisibility(View.GONE);
        }
    }

    private void hideStatusBarIfLandscape() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent upIntent = new Intent(this, StepListActivity.class);
            upIntent.putExtra(Recipe.RECIPE_KEY, recipe);
            navigateUpTo(upIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeOnError() {
        // TODO: 5/6/18
    }


}
