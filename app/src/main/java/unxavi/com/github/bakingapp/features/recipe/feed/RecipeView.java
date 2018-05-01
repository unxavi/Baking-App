package unxavi.com.github.bakingapp.features.recipe.feed;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

import unxavi.com.github.bakingapp.model.Recipe;

public interface RecipeView extends MvpView {

    void showLoading();

    void showData(List<Recipe> recipes);

    void showInternetError();

    void showServerError();
}
