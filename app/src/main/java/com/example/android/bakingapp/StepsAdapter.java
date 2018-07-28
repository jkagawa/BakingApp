package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joshua on 7/2/2018.
 */

class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    final private StepsAdapterOnClickHandler mClickHandler;
    public final int mPosition;

    private List<String> mStepsIDList;
    private List<String> mStepsShortDescriptionList;
    private List<String> mStepsDescriptionList;
    private List<String> mStepsVideoURLList;
    private List<String> mStepsThumbnailURLList;

    public interface StepsAdapterOnClickHandler {
        void onClick(int position);
    }

    public StepsAdapter(int position, StepsAdapterOnClickHandler clickHandler) {
        mPosition = position;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.steps_list_item;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        view.setFocusable(true);

        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StepsViewHolder holder, int position) {

        Context context = holder.stepsNumberView.getContext();

        int stepNumber = Integer.parseInt(mStepsIDList.get(position));
        holder.stepsNumberView.setText(context.getResources().getString(R.string.step_number, String.valueOf(stepNumber)));

        holder.stepsShortDescriptionView.setText(mStepsShortDescriptionList.get(position));

        URL url = null;
        try {
            url = new URL(mStepsThumbnailURLList.get(position));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if(url!=null) {
            Picasso.with(context)
                    .load(url.toString())
                    .error(R.drawable.no_recipe_image)
                    .into(holder.stepsImageView);
        }

    }

    @Override
    public int getItemCount() {
        if(mStepsIDList==null) {
            return 0;
        }
        else {
            return mStepsIDList.size();
        }
    }


    class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //final TextView stepsNumberView;
        //final TextView stepsShortDescriptionView;
        @BindView(R.id.detail_step_image) ImageView stepsImageView;
        @BindView(R2.id.detail_step_number) TextView stepsNumberView;
        @BindView(R2.id.detail_step_short_description) TextView stepsShortDescriptionView;

        public StepsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            //stepsNumberView = itemView.findViewById(R.id.detail_step_number);
            //stepsShortDescriptionView = itemView.findViewById(R.id.detail_step_short_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mClickHandler.onClick(clickedPosition);
        }

    }

    public void setData(List<String> stepsIDList, List<String> stepsShortDescriptionList, List<String> stepsDescriptionList, List<String> stepsVideoURLList, List<String> stepsThumbnailURLList) {
        stepsIDList.remove(0);
        stepsShortDescriptionList.remove(0);
        stepsDescriptionList.remove(0);
        stepsVideoURLList.remove(0);
        stepsThumbnailURLList.remove(0);

        mStepsIDList = stepsIDList;
        mStepsShortDescriptionList = stepsShortDescriptionList;
        mStepsDescriptionList = stepsDescriptionList;
        mStepsVideoURLList = stepsVideoURLList;
        mStepsThumbnailURLList = stepsThumbnailURLList;
        notifyDataSetChanged();
    }
}
