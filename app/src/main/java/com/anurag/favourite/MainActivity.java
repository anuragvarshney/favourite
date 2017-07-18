package com.anurag.favourite;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.anurag.favourite.controller.ProjectNetworkService;
import com.anurag.favourite.model.Project;
import com.anurag.favourite.utils.OnItemClickListener;
import com.anurag.favourite.utils.ViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    public static final String IS_FAVOURITE = "isFavourite";

    @BindView(R.id.vwPager)
    ViewPager mViewPager;
    @BindView(R.id.tab_projectType)
    TabLayout tabLayout;
    @BindView(R.id.vw_rootLayout)
    LinearLayout vwRootLayout;

    private ProgressDialog mProgressDialog;
    private ArrayList<Project> mProjectList;
    private ListFragment mPojectListFragment;
    private ArrayList<Project> mFavouriteList;
    private ListFragment mFavouriteListFragment;
    private ProjectNetworkService mNetworkService;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initilizations();
        setUpViewPager();
        getProjectList();
    }

    private void initilizations() {
        mPojectListFragment = new ListFragment();
        Bundle projectBunble = new Bundle();
        projectBunble.putBoolean(IS_FAVOURITE, true);
        mPojectListFragment.setArguments(projectBunble);
        mFavouriteListFragment = new ListFragment();
        Bundle favouriteBundle = new Bundle();
        favouriteBundle.putBoolean(IS_FAVOURITE, false);
        mFavouriteListFragment.setArguments(favouriteBundle);
        mCompositeDisposable = new CompositeDisposable();
        mNetworkService = ProjectNetworkService.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.progress_message));
    }

    private void setUpViewPager() {
        ViewPagerAdapter mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(mPojectListFragment, "List");
        mPagerAdapter.addFragment(mFavouriteListFragment, "Favourite");
        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void getProjectList() {
        mProgressDialog.show();
        mCompositeDisposable.add(mNetworkService.getFavouriteApi().getProjectList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ArrayList<Project>>() {
                    @Override
                    public void onNext(@NonNull ArrayList<Project> projects) {
                        mProgressDialog.cancel();
                        mProjectList = projects;
                        mFavouriteList = new ArrayList<>();
                        mPojectListFragment.setList(mProjectList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mProgressDialog.cancel();
                        mPojectListFragment.setError();
                        Snackbar snackbar = Snackbar.make(vwRootLayout, R.string.network_error, Snackbar.LENGTH_INDEFINITE);
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getProjectList();
                            }
                        });
                        snackbar.show();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void OnItemClicked(int position, boolean isfavourite) {
        if (isfavourite) {
            mFavouriteList.add(mProjectList.get(position));
        } else {
            mFavouriteList.remove(mProjectList.get(position));
        }
        mFavouriteListFragment.setList(mFavouriteList);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCompositeDisposable.dispose();
    }
}
