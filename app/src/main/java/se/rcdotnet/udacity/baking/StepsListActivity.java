package se.rcdotnet.udacity.baking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepsListActivity extends AppCompatActivity implements BakingStepAdapter.BakingStepAdapterClickHandler {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Recipe mRecipe;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_steps_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (getIntent().hasExtra("Recipe")){
            mRecipe = (Recipe) getIntent().getParcelableExtra("Recipe");
            getSupportActionBar().setTitle(getTitle()+" "+mRecipe.getName());
        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        RecyclerView recyclerView = findViewById(R.id.item_list);
        BakingStepAdapter mAdapter = new BakingStepAdapter(this, mRecipe,this);
        LinearLayoutManager mManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mManager);
        recyclerView.addItemDecoration(new Decor());
    }
    private final BakingStepAdapter.BakingStepAdapterClickHandler mOnClickListener = new BakingStepAdapter.BakingStepAdapterClickHandler() {
        @Override
        public void onClick(int pos) {
            Step item = mRecipe.getSteps().get(pos);
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(StepDetailFragment.ARG_ITEM, item);
                StepDetailFragment fragment = new StepDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {

                Intent intent = new Intent(mContext,StepDetailActivity.class);
                intent.putExtra(StepDetailFragment.ARG_ITEM, item);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onClick(int position) {
        mOnClickListener.onClick(position);
    }

    public class Decor extends RecyclerView.ItemDecoration {
        int mBottom = 10;
        int mLeft = 0;
        public Decor(){}
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.top = 10;
            outRect.bottom = 10;
            outRect.left = 2;
            outRect.right = 2;
        }
    }
}
