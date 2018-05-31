package se.rcdotnet.udacity.baking;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BakingStepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Recipe mRecipe;
    Context mContext;
    static final int VIEW_INGREDIENTS = 0;
    static final int VIEW_STEP = 1;


    final private BakingStepAdapterClickHandler mClickHandler;

    public interface BakingStepAdapterClickHandler {
        void onClick(int position);
    }

    public BakingStepAdapter(Context context, Recipe recipe, BakingStepAdapterClickHandler listener) {
       mRecipe = recipe;
        mContext = context;
        mClickHandler = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View retView;
        switch (viewType){
            case VIEW_STEP:
                retView = inflater.inflate(R.layout.baking_step_item_layout, parent, false);
                return new BakingStepViewHolder(retView);
            case VIEW_INGREDIENTS:
                retView = inflater.inflate(R.layout.baking_ingrediens_layout, parent, false);
                return new BakingIngrediensViewHolder(retView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return VIEW_INGREDIENTS;
        else
            return VIEW_STEP;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mRecipe.getSteps().size() == 0) return;
        if (holder.getItemViewType() == VIEW_INGREDIENTS) {
            StringBuilder builder = new StringBuilder();
            builder.append("<ul>");
            for (Ingredient i : mRecipe.getIngredients()
                 ) {
                builder.append("<li>");
                builder.append(String.valueOf(i.getQuantity())).append(" ")
                        .append(i.getMeasure()).append(" ").append(i.getIngredient());
            }
            builder.append("</ul>");
            BakingIngrediensViewHolder holder1 = (BakingIngrediensViewHolder) holder;
            holder1.mIngrediensTitle.setText(mContext.getString(R.string.ingrediensTitleText));
            holder1.mIngredients.setText(Html.fromHtml(builder.toString()));
        }
        else{
            BakingStepViewHolder holder2 = (BakingStepViewHolder) holder;
            holder2.mStepName.setText(mRecipe.getSteps().get(position-1).getShortdescription());
        }

        // TODO bind the view
    }

    public void SwapData(Recipe recipe) {
        mRecipe = recipe;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRecipe == null || mRecipe.getSteps().size() == 0) return 0;
        else return mRecipe.getSteps().size() + 1;     // Using +1 to render the special irst card representing the ingrediens
    }

    public class BakingStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // TODO define variables for the viewholder's views
        TextView mStepName;


        public BakingStepViewHolder(View itemView) {
            super(itemView);
            // TODO find the views, and store the references
            mStepName = itemView.findViewById(R.id.Step_name_TextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if (mClickHandler != null)
                mClickHandler.onClick(adapterPosition - 1);
        }
    }
    public class BakingIngrediensViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // TODO define variables for the viewholder's views
        CardView mCard;
        TextView mIngrediensTitle;
        TextView mIngredients;

        public BakingIngrediensViewHolder(View itemView) {
            super(itemView);
            // TODO find the views, and store the references
            mCard = itemView.findViewById(R.id.cardIngrediens);
            mIngrediensTitle = itemView.findViewById(R.id.ingrediensTitleTextView);
            mIngredients = itemView.findViewById(R.id.IngredientsTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() != null && (int) v.getTag() == 1){
                        ViewGroup.LayoutParams p = mCard.getLayoutParams();
                        p.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, mContext.getResources().getDisplayMetrics());
                        mCard.setLayoutParams(p);
                        mIngrediensTitle.setText(mContext.getString(R.string.ingrediensTitleText));
                        v.setTag(0);
                    }
                    else
                    {
                        ViewGroup.LayoutParams p = mCard.getLayoutParams();
                        p.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        mCard.setLayoutParams(p);
                        mIngrediensTitle.setText(mContext.getString(R.string.ingrediensTitleTextCollapse));
                        v.setTag(1);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if (mClickHandler != null)
                mClickHandler.onClick(adapterPosition - 1);
        }
    }

}
