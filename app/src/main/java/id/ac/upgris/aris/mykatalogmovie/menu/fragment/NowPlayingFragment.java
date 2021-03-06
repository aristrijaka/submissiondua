package id.ac.upgris.aris.mykatalogmovie.menu.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import id.ac.upgris.aris.mykatalogmovie.R;
import id.ac.upgris.aris.mykatalogmovie.adapter.MovieAdapter;
import id.ac.upgris.aris.mykatalogmovie.helper.Config;
import id.ac.upgris.aris.mykatalogmovie.model.MovieModel;
import id.ac.upgris.aris.mykatalogmovie.model.ResultsItem;
import id.ac.upgris.aris.mykatalogmovie.rest.ApiService;
import id.ac.upgris.aris.mykatalogmovie.rest.Client;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NowPlayingFragment extends Fragment {

    private ArrayList<ResultsItem> listResults;
    private SearchView svNowPlayingMovie;
    private RecyclerView rvNowPlayingMovie;
    private MovieAdapter movieAdapter;
    private EditText edtSearch;

    public NowPlayingFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);
        initView(view);
        listResults = new ArrayList<>();
        Locale current = getResources().getConfiguration().locale;
        if (current.toString().equalsIgnoreCase("en_US")) {
            getMovieNowPlayingEN();
        } else {
            getMovieNowPlayingID();
        }


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                movieAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        return view;
    }

    private void getMovieNowPlayingEN() {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), getString(R.string.Tittle_loading), getString(R.string.Message_loading), false, false);
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getMovieNowPlaying("en_US").enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.isSuccessful()) {
                    listResults = response.body().getResults();
                    movieAdapter = new MovieAdapter(getActivity(), listResults);
                    rvNowPlayingMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rvNowPlayingMovie.setAdapter(movieAdapter);
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getActivity(), "" + Config.ERROR_NETWORK, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMovieNowPlayingID() {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), getString(R.string.Tittle_loading), getString(R.string.Message_loading), false, false);
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getMovieNowPlaying("id").enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.isSuccessful()) {
                    listResults = response.body().getResults();
                    movieAdapter = new MovieAdapter(getActivity(), listResults);
                    rvNowPlayingMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rvNowPlayingMovie.setAdapter(movieAdapter);
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getActivity(), "" + Config.ERROR_NETWORK, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view) {
        svNowPlayingMovie = (SearchView) view.findViewById(R.id.sv_now_playing_movie);
        rvNowPlayingMovie = (RecyclerView) view.findViewById(R.id.rv_now_playing_movie);
        edtSearch = (EditText) view.findViewById(R.id.edt_search);
    }
}
