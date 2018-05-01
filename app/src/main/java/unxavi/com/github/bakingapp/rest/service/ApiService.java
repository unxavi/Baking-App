package unxavi.com.github.bakingapp.rest.service;


import android.support.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import unxavi.com.github.bakingapp.model.Recipe;

public interface ApiService {

    /* API PATHS */
    String RECIPES_PATH = "baking.json";

    /**
     * Get the recipe list from the API
     *
     * @return response {@linkplain retrofit2.Call } with recipes from the API
     * with a list {@linkplain unxavi.com.github.bakingapp.model.Recipe } object
     */
    @NonNull
    @GET(RECIPES_PATH)
    Call<List<Recipe>> getRecipes();

}
