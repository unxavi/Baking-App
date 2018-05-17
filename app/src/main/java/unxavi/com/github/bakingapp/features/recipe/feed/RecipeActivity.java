package unxavi.com.github.bakingapp.features.recipe.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.widget.RecyclerView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import unxavi.com.github.bakingapp.R;
import unxavi.com.github.bakingapp.features.recipe.detail.StepListActivity;
import unxavi.com.github.bakingapp.model.Recipe;
import unxavi.com.github.bakingapp.tests.SimpleIdlingResource;

public class RecipeActivity extends MvpActivity<RecipeView, RecipePresenter> implements
        RecipeView,
        RecipeAdapter.RecipeAdapterOnClickHandler {

    @BindView(R.id.recipes_rv)
    RecyclerView recipesRv;

    private RecipeAdapter recipeAdapter;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource idlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
    }

    @NonNull
    @Override
    public RecipePresenter createPresenter() {
        return new RecipePresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.getRecipes(idlingResource);
    }

    @Override
    public void showLoading() {
        // TODO: 5/6/18
    }

    @Override
    public void showData(List<Recipe> recipes) {
        if (recipeAdapter == null) {
            recipeAdapter = new RecipeAdapter(recipes, this);
            recipesRv.setHasFixedSize(true);
            recipesRv.setAdapter(recipeAdapter);
        }
    }

    @Override
    public void showInternetError() {
        // TODO: 5/6/18
    }

    @Override
    public void showServerError() {
        // TODO: 5/6/18
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, StepListActivity.class);
        intent.putExtra(Recipe.RECIPE_KEY, recipe);
        startActivity(intent);
    }
}
