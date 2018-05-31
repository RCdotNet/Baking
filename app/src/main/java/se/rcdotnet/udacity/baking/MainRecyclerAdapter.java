package se.rcdotnet.udacity.baking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainRecyclerViewHolder> {


    Recipe[] mRecipeArray;
    Context mContext;


    final private MainRecyclerAdapterClickHandler mClickHandler;

    public interface MainRecyclerAdapterClickHandler {
        void onClick(int position);
    }

    public MainRecyclerAdapter(Context context, Recipe[] recipes, MainRecyclerAdapterClickHandler listener) {
        mRecipeArray = recipes;
        mContext = context;
        mClickHandler = listener;
    }

    @Override
    public MainRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View retView = inflater.inflate(R.layout.main_recycler_item_layout, parent, false);
        return new MainRecyclerViewHolder(retView);
    }

    @Override
    public void onBindViewHolder(MainRecyclerViewHolder holder, int position) {
        if (mRecipeArray == null) return;
        holder.mName.setText(mRecipeArray[position].getName());
    }

    public void SwapData(Recipe[] recipes) {
        mRecipeArray = recipes;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if (mRecipeArray == null) return 0;
        else return mRecipeArray.length;
    }

    public class MainRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mName;
        public MainRecyclerViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.Name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());
        }

    }

}
