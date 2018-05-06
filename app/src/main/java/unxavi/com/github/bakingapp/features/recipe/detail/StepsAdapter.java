package unxavi.com.github.bakingapp.features.recipe.detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import unxavi.com.github.bakingapp.R;
import unxavi.com.github.bakingapp.model.Step;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private List<Step> data;
    private StepsAdapterListener listener;

    public interface StepsAdapterListener {
        void onStepClick(Step step);
    }

    StepsAdapter(List<Step> data, StepsAdapterListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.step = data.get(position);
        holder.mContentView.setText(holder.step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Step step;
        TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mContentView = view.findViewById(R.id.content);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onStepClick(step);
        }
    }

}
