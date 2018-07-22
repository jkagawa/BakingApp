package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joshua on 7/2/2018.
 */

class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    //final private IngredientsAdapterOnClickHandler mClickHandler;
    public final int mPosition;

    private List<String> mIngredientQuantityList;
    private List<String> mIngredientMeasureList;
    private List<String> mIngredientNameList;

    public interface IngredientsAdapterOnClickHandler {
        void onClick(int position);
    }

    public IngredientsAdapter(int position) {
        mPosition = position;
        //mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.ingredients_list_item;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        view.setFocusable(true);

        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final IngredientsViewHolder holder, int position) {

        Context context = holder.ingredientNameView.getContext();

        holder.ingredientNameView.setText(mIngredientNameList.get(position));

        holder.ingredientAmountView.setText(context.getResources().getString(R.string.ingredient_amount, mIngredientQuantityList.get(position), mIngredientMeasureList.get(position)));


    }

    @Override
    public int getItemCount() {
        if(mIngredientNameList==null) {
            return 0;
        }
        else {
            return mIngredientNameList.size();
        }
    }


    class IngredientsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //final TextView ingredientNameView;
        //final TextView ingredientAmountView;
        @BindView(R2.id.detail_ingredient_name) TextView ingredientNameView;
        @BindView(R2.id.detail_ingredient_amount) TextView ingredientAmountView;

        public IngredientsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            //ingredientNameView = itemView.findViewById(R.id.detail_ingredient_name);
            //ingredientAmountView = itemView.findViewById(R.id.detail_ingredient_amount);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            //mClickHandler.onClick(clickedPosition);
        }

    }

    public void setData(List<String> ingredientQuantityList, List<String> ingredientMeasureList, List<String> ingredientNameList) {
        mIngredientQuantityList = ingredientQuantityList;
        mIngredientMeasureList = ingredientMeasureList;
        mIngredientNameList = ingredientNameList;
        notifyDataSetChanged();
    }
}
