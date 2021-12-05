package br.com.titoschmidt.netflixremake.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.titoschmidt.netflixremake.model.Movie;
import br.com.titoschmidt.netflixremake.model.MovieDetail;

public class MovieDetailTask extends AsyncTask<String, Void, MovieDetail> {

    private final WeakReference <Context> context;

    ProgressDialog dialog;

    private MovieDetailLoader movieDetailLoader;

    public MovieDetailTask(Context context) {

        this.context = new WeakReference<>(context);
    }

    public void setMovieDetailLoader(MovieDetailLoader movieDetailLoader){
        this.movieDetailLoader = movieDetailLoader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.context.get();
        if(context != null){
            dialog = ProgressDialog.show(context,"Carregando detalhes...","", true);
        }
    }

    @Override
    protected MovieDetail doInBackground(String... params) {
        String urlMovieDetail = params[0];

        HttpURLConnection urlConnection;

        try {
            URL url = new URL(urlMovieDetail);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(2000);
            urlConnection.setConnectTimeout(2000);
            int statusCode = urlConnection.getResponseCode();
            if(statusCode > 400) {
                throw new IOException("Erro na comunicação do servidor");
            }

            InputStream inputStream = urlConnection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            String jsonAsString = toString(bufferedInputStream);
            MovieDetail movieDetail = getMovieDetail(new JSONObject(jsonAsString));
            bufferedInputStream.close();
            return movieDetail;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    // os similares são os filmes da mesma categoria do filme selecionado
    private MovieDetail getMovieDetail(JSONObject json) throws JSONException {
        int id = json.getInt("id");
        String title = json.getString("title");
        String desc = json.getString("desc");
        String cast = json.getString("cast");
        String coverUrl = json.getString("cover_url");

        List<Movie> movies = new ArrayList();
        JSONArray movieArray = json.getJSONArray("movie");
        for(int i=0;i<movieArray.length(); i++){
            JSONObject movie = movieArray.getJSONObject(i);
            String cover_url = movie.getString("cover_url");
            int idSimilar = movie.getInt("id");
            Movie similar = new Movie();
            similar.setId(idSimilar);
            similar.setCoverUrl(cover_url);
            movies.add(similar);
        }
        Movie movie = new Movie();
        movie.setId(id);
        movie.setCoverUrl(coverUrl);
        movie.setTitle(title);
        movie.setDescription(desc);
        movie.setCast(cast);
        return null;
        //return new MovieDetail(movie, movies);
    }

    private String toString(InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while((lidos = is.read(bytes)) >0){
            baos.write(bytes, 0 , lidos);
        }
        return new String(baos.toByteArray());
    }

    @Override
    protected void onPostExecute(MovieDetail movieDetail) {
        super.onPostExecute(movieDetail);
        dialog.dismiss();
        if(movieDetailLoader != null){
            movieDetailLoader.onResult(movieDetail);
        }
    }

    public interface MovieDetailLoader {
        void onResult(MovieDetail movieDetail);
    }
}
