package unxavi.com.github.bakingapp.model;

import com.google.gson.annotations.SerializedName;

public class Step {

    @SerializedName("id")
    private Integer id;

    @SerializedName("shortDescription")
    private String shortDescription;

    @SerializedName("description")
    private String description;

    @SerializedName("videoURL")
    private String videoURL;

    @SerializedName("thumbnailURL")
    private String thumbnailURL;

}
