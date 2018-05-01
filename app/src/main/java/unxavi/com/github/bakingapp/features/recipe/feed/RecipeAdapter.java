package unxavi.com.github.bakingapp.features.recipe.feed;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import unxavi.com.github.bakingapp.R;
import unxavi.com.github.bakingapp.model.Recipe;

/**
 * {@link RecipeAdapter} exposes a list of data
 * from a {@link List<unxavi.com.github.bakingapp.model.Recipe>} to a {@link android.support.v7.widget.RecyclerView}.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    /* List of recipes to use with the adapter */
    private final List<Recipe> data;

    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our RecipeAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onClick method whenever
     * an item is clicked in the list.
     */
    final private RecipeAdapterOnClickHandler listener;

    /**
     * The interface that receives onRecipeClick messages and handle it.
     */
    public interface RecipeAdapterOnClickHandler {
        void onRecipeClick(Recipe recipe);
    }

    /**
     * Creates a RecipeAdapter.
     *
     * @param data     List of recipes data to show on the adapter
     * @param listener The on-click handler for this adapter. This single handler is called
     *                 when an item is clicked.
     */
    RecipeAdapter(List<Recipe> data, RecipeAdapterOnClickHandler listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_recipe, parent, false);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = data.get(position);
        holder.recipeTV.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews.
     * this ViewHolder represents a movie view
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView recipeTV;

        ViewHolder(View itemView) {
            super(itemView);
            recipeTV = itemView.findViewById(R.id.recipe_tv);
            itemView.setOnClickListener(this);
        }


        /**
         * This gets called by the child views during a click. We fetch the  recipe
         * that has been selected, and then call the onRecipeClick handler registered with this a
         * adapter, passing that recipe
         *
         * @param view the View that was clicked
         */
        @Override
        public void onClick(View view) {
            Recipe recipe = data.get(getAdapterPosition());
            listener.onRecipeClick(recipe);
        }
    }
}
