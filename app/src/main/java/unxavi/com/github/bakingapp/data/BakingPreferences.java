package unxavi.com.github.bakingapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

public class BakingPreferences {

    /*
     * key to store in share preferences the ingredients text of the last recipe the user open
     */
    private static final String LAST_INGREDIENTS_PREFERENCE = "last_ingredients_preference";
    private static final String LAST_RECIPE_TITLE_OPEN_PREFERENCE = "last_recipe_title_open_preference";


    /**
     * Helper to get the default shared preferences
     *
     * @param context used to access SharedPreferences
     * @return SharedPreferences default
     */
    private static SharedPreferences getSharedPreferences(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Return the last recipe ingredients
     *
     * @param context used to access SharedPreferences
     * @return a string with the ingredients
     */
    public static String getLastIngredients(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getString(LAST_INGREDIENTS_PREFERENCE, "");
    }

    /**
     * Sets on sharedPreferences the ingredients for the last recipe opened
     *
     * @param context        used to access SharedPreferences
     * @param ingredientsString string with the ingredients to save
     */
    public static void setLastIngredientsPreference(Context context, String ingredientsString) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_INGREDIENTS_PREFERENCE, ingredientsString);
        editor.apply();
    }

    /**
     * Return the last recipe opened name
     *
     * @param context used to access SharedPreferences
     * @return a string with the name
     */
    public static String getLastRecipeTitleOpenPreference(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getString(LAST_RECIPE_TITLE_OPEN_PREFERENCE, "");
    }

    /**
     * Sets on sharedPreferences the name for the last recipe opened
     *
     * @param context        used to access SharedPreferences
     * @param recipeTitle string with the ingredients to save
     */
    public static void setLastRecipeTitleOpenPreference(Context context, String recipeTitle) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_RECIPE_TITLE_OPEN_PREFERENCE, recipeTitle);
        editor.apply();
    }
}
