package com.anurag.favourite.controller;

import com.anurag.favourite.model.Project;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by ${Anurag} on 18/7/17.
 */

public class ProjectNetworkService {
    private static ProjectNetworkService sProjectNetworkService;
    private ProjectApi projectApi;

    private ProjectNetworkService() {
        if (sProjectNetworkService != null) {
            throw new RuntimeException("Reflection Not Allowed");
        }
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://54.254.198.83:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();
        projectApi = retrofit.create(ProjectApi.class);

    }

    public static ProjectNetworkService getInstance() {
        if (sProjectNetworkService == null) {
            synchronized (ProjectNetworkService.class) {
                sProjectNetworkService = new ProjectNetworkService();
            }
        }
        return sProjectNetworkService;
    }

    public ProjectApi getFavouriteApi() {
        return projectApi;
    }


    public interface ProjectApi {
        @GET("favourite.json")
        Observable<ArrayList<Project>> getProjectList();
    }
}
