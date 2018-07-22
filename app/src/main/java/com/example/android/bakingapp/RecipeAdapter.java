package com.example.android.bakingapp;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joshua on 7/2/2018.
 */

class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    final private RecipeAdapterOnClickHandler mClickHandler;
    public final int mPosition;

    private List<String> mRecipeNameList;
    private List<String> mImageList;

    public interface RecipeAdapterOnClickHandler {
        void onClick(int position);
    }

    public RecipeAdapter(int position, RecipeAdapterOnClickHandler clickHandler) {
        mPosition = position;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.recipe_list_item;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        view.setFocusable(true);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int position) {


        Context context = holder.recipeNameView.getContext();

        //URL imageURL = NetworkUtils.buildImageUrl(mPoster.get(position),"w780");

        /*
        if(mImageList.get(position).isEmpty()) {
            Picasso.with(context)
                    .load(R.drawable.placeholder_recipe_image)
                    .placeholder(R.drawable.placeholder_recipe_image)
                    .error(R.drawable.no_recipe_image)
                    .into(holder.recipeImageView);
        }
        else {
            Picasso.with(context)
                    .load(mImageList.get(position))
                    .placeholder(R.drawable.placeholder_recipe_image)
                    .error(R.drawable.no_recipe_image)
                    .into(holder.recipeImageView);
        }
        */

        holder.recipeNameView.setText(mRecipeNameList.get(position));


    }

    @Override
    public int getItemCount() {
        if(mRecipeNameList==null) {
            return 0;
        }
        else {
            return mRecipeNameList.size();
        }
    }


    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //final TextView recipeNameView;
        //final ImageView recipeImageView;
        @BindView(R2.id.recipe_name) TextView recipeNameView;
        @BindView(R2.id.recipe_image) ImageView recipeImageView;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            //recipeNameView = itemView.findViewById(R.id.recipe_name);
            //recipeImageView = itemView.findViewById(R.id.recipe_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mClickHandler.onClick(clickedPosition);
        }

    }

    public void setData(List<String> recipeNameList, List<String> imageList) {
        mRecipeNameList = recipeNameList;
        mImageList = imageList;
        notifyDataSetChanged();
    }
}
