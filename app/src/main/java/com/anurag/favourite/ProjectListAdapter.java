package com.anurag.favourite;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.anurag.favourite.model.Project;
import com.anurag.favourite.utils.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ${Anurag} on 18/7/17.
 */

class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder> {

    private ArrayList<Project> mProjectList;
    private Context mContext;
    private OnItemClickListener mListener;
    private boolean isFavourite = false;
    private Drawable projectDrawable;
    private Drawable internDrawable;
    private Drawable offerDrawable;

    ProjectListAdapter(ArrayList<Project> projectList,Context context, boolean isFavourite) {
        mProjectList = projectList;
        mContext = context;
        mListener = (OnItemClickListener) context;
        this.isFavourite = isFavourite;
        projectDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_project);
        internDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_internship);
        offerDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_offer);
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProjectViewHolder holder, int position) {
        Project project = mProjectList.get(position);
        if (project != null) {
            holder.txtTitle.setText(project.getTitle());
            holder.txtDescription.setText(project.getDesc());
            holder.txtViewCount.setText(getApproxValue(project.getViewcount()));
            if (project.isFavourite()) {
                holder.chkFavourite.setChecked(true);

            } else {
                holder.chkFavourite.setChecked(false);
            }
            if (project.getType().equalsIgnoreCase("project")) {
                holder.txtTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, projectDrawable, null);
            } else if (project.getType().equalsIgnoreCase("internship")) {
                holder.txtTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, internDrawable, null);
            } else if (project.getType().equalsIgnoreCase("offer")) {
                holder.txtTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, offerDrawable, null);
            }

            Glide.with(mContext)
                    .load(project.getImageUrl())
                    .apply(RequestOptions
                            .circleCropTransform()
                            .placeholder(R.drawable.ic_profile)
                            .error(R.drawable.ic_profile))
                    .into(holder.imgProjectIcon);
        }
    }

    private String getApproxValue(double count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format(Locale.getDefault(), "%.1f %c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp - 1));
    }

    @Override
    public int getItemCount() {
        return mProjectList.size();
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_description)
        TextView txtDescription;
        @BindView(R.id.txt_viewCount)
        TextView txtViewCount;
        @BindView(R.id.chk_favourite)
        CheckBox chkFavourite;
        @BindView(R.id.img_projectIcon)
        ImageView imgProjectIcon;

        ProjectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (!isFavourite) {
                chkFavourite.setVisibility(View.GONE);
            }

        }

        @OnClick(R.id.chk_favourite)
        public void onClick() {
            if (mProjectList.get(getAdapterPosition()).isFavourite()) {
                mProjectList.get(getAdapterPosition()).setFavourite(false);
                chkFavourite.setChecked(false);
                mListener.OnItemClicked(getAdapterPosition(), false);
            } else {
                mProjectList.get(getAdapterPosition()).setFavourite(true);
                chkFavourite.setChecked(true);
                mListener.OnItemClicked(getAdapterPosition(), true);
            }
        }
    }
}
