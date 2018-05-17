package unxavi.com.github.bakingapp.features.recipe.feed;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import unxavi.com.github.bakingapp.model.Recipe;
import unxavi.com.github.bakingapp.rest.ServiceGenerator;
import unxavi.com.github.bakingapp.rest.service.ApiService;
import unxavi.com.github.bakingapp.tests.SimpleIdlingResource;

class RecipePresenter extends MvpBasePresenter<RecipeView> {

    private static final String TAG = "RecipePresenter";
    private Call<List<Recipe>> recipesCall;
    private SimpleIdlingResource idlingResource;

    public void getRecipes(@Nullable SimpleIdlingResource idlingResource) {
        /**
         * The IdlingResource is null in production as set by the @Nullable annotation which means
         * the value is allowed to be null.
         *
         * If the idle state is true, Espresso can perform the next action.
         * If the idle state is false, Espresso will wait until it is true before
         * performing the next action.
         */
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        showViewLoading();
        ApiService service = ServiceGenerator.createService(ApiService.class);
        recipesCall = service.getRecipes();
        recipesCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();
                if (response.isSuccessful()) {
                    showViewData(recipes);
                } else {
                    // if the response is not successful show that there
                    // was a server error to the user and should try it later
                    showViewServerError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                // if the response has a failure could be for many reasons, could be the lack
                // of connection and might need to retry later or it could fail due to some
                // mismatch of the response and Java models
                if (t instanceof IOException) {
                    showViewInternetError();
                } else {
                    // mismatch of the response and Java models probably
                    showViewServerError();
                    Log.e(TAG, "onFailure: ", t);
                }
            }
        });

    }

    private void showViewData(final List<Recipe> recipes) {
        ifViewAttached(new ViewAction<RecipeView>() {
            @Override
            public void run(@NonNull RecipeView view) {
                view.showData(recipes);
            }
        });
    }

    private void showViewLoading() {
        setIdleResourceDone();
        ifViewAttached(new ViewAction<RecipeView>() {
            @Override
            public void run(@NonNull RecipeView view) {
                view.showLoading();
            }
        });
    }

    private void showViewInternetError() {
        setIdleResourceDone();
        ifViewAttached(new ViewAction<RecipeView>() {
            @Override
            public void run(@NonNull RecipeView view) {
                view.showInternetError();
            }
        });
    }

    private void showViewServerError() {
        setIdleResourceDone();
        ifViewAttached(new ViewAction<RecipeView>() {
            @Override
            public void run(@NonNull RecipeView view) {
                view.showServerError();
            }
        });
    }

    private void setIdleResourceDone(){
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }
    }

    /**
     * If the presenter gets destroy, cancel the network request if any
     */
    @Override
    public void destroy() {
        super.destroy();
        if (recipesCall != null) {
            recipesCall.cancel();
            recipesCall = null;
        }
    }

}
