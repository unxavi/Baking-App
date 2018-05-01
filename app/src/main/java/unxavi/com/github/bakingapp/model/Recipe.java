package unxavi.com.github.bakingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("ingredients")
    private List<Ingredient> ingredients = null;

    @SerializedName("steps")
    private List<Step> steps = null;

    @SerializedName("servings")
    private Long servings;

    @SerializedName("image")
    private String image;

}
