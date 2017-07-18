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
import com.anurag.favourite.utils.OnItemClickListener;

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

    public ListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnItemClickListener mClickListener = (OnItemClickListener) getActivity();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        boolean isfavourite = getArguments().getBoolean(MainActivity.IS_FAVOURITE);
        mAdapter = new ProjectListAdapter(new ArrayList<Project>(), getActivity(),mClickListener,isfavourite);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void setList(ArrayList<Project> projectList) {
        if (projectList!=null && projectList.size()>0) {
            vwProjectList.setVisibility(View.VISIBLE);
            vwError.setVisibility(View.GONE);
            if (vwProjectList != null && mAdapter != null) {
                vwProjectList.setLayoutManager(mLinearLayoutManager);
                vwProjectList.setAdapter(mAdapter);
                mAdapter.setList(projectList);
            }
        }
        else {
         setError();
        }
    }

    public void setError(){
        vwProjectList.setVisibility(View.GONE);
        vwError.setVisibility(View.VISIBLE);
    }
}
