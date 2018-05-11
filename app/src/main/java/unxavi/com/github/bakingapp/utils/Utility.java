package unxavi.com.github.bakingapp.utils;

import android.text.TextUtils;

import java.util.Locale;

import unxavi.com.github.bakingapp.model.Ingredient;
import unxavi.com.github.bakingapp.model.Recipe;

public class Utility {

    public static String formatIngredients(Recipe recipe){
        String ingredients = "";
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (!TextUtils.isEmpty(ingredients)) {
                ingredients += '\n';
            }
            ingredients += String.format(Locale.getDefault(), "\u25CF %.2f %s %s", ingredient.getQuantity(), ingredient.getMeasure().toLowerCase(), ingredient.getIngredient());
        }
        return ingredients;
    }
}
