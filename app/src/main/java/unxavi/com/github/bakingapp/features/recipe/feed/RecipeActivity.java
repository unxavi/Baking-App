package unxavi.com.github.bakingapp.features.recipe.feed;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import unxavi.com.github.bakingapp.R;
import unxavi.com.github.bakingapp.model.Recipe;

public class RecipeActivity extends MvpActivity<RecipeView, RecipePresenter> implements RecipeView, RecipeAdapter.RecipeAdapterOnClickHandler {

    @BindView(R.id.recipes_rv)
    RecyclerView recipesRv;

    private RecipeAdapter recipeAdapter;

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
        presenter.getRecipes();
    }

    @Override
    public void showLoading() {

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

    }

    @Override
    public void showServerError() {

    }

    @Override
    public void onRecipeClick(Recipe recipe) {

    }
}
