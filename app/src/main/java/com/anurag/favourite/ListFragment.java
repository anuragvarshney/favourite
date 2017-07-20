package com.anurag.favourite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.anurag.favourite.model.Project;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {

    @BindView(R.id.lst_projectList)
    RecyclerView vwProjectList;
    @BindView(R.id.vw_error)
    LinearLayout vwError;

    private ProjectListAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<Project> mProjectList;

    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isfavourite = getArguments().getBoolean(MainActivity.IS_FAVOURITE);
        mProjectList = new ArrayList<>();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ProjectListAdapter(mProjectList,getActivity(),isfavourite);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        vwProjectList.setLayoutManager(mLinearLayoutManager);
        vwProjectList.setAdapter(mAdapter);
        return view;
    }

    public void setList(ArrayList<Project> projectList) {
        if (projectList != null && projectList.size() > 0) {
            vwProjectList.setVisibility(View.VISIBLE);
            vwError.setVisibility(View.GONE);
            mProjectList.clear();
            mProjectList.addAll(projectList);
            mAdapter.notifyDataSetChanged();
        } else {
            setError();
        }
    }

    public void setError() {
        vwProjectList.setVisibility(View.GONE);
        vwError.setVisibility(View.VISIBLE);
    }
}
