package br.com.titoschmidt.chucknorrisio.datasource;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRemoteDataSource {
    //callback devolve uma lista de categorias ao presenter
    public interface ListCategoriesCallback{

        void onSuccess(List<String> response);

        void onError(String msgErro);

        void onComplete();
    }

    public void findAll(ListCategoriesCallback callback) {
        //Cria uma instância concreta do Retrofit e passa a API com todos os métodos disponíveis
        HTTPClient.retrofit().create(ChuckNorrisAPI.class)
                .findAll()
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        if(response.isSuccessful())
                            callback.onSuccess(response.body());

                        callback.onComplete();
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        // Mostra uma msg de erro
                        callback.onError(t.getMessage());
                        // Esconde a progress bar
                        callback.onComplete();
                    }
                });

        // Método antigo sem uso de Retrofit
        //new CategoryTask(callback).execute();
    }

    private static class CategoryTask extends AsyncTask<Void, Void, List<String>>{

        private ListCategoriesCallback callback;

        private String msgErro;

        public CategoryTask(ListCategoriesCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> response = new ArrayList<>();
            HttpsURLConnection urlConnection;

            try {
                URL url = new URL(Endpoint.GET_CATEGORIES); // https://api.chucknorris.io/jokes/categories
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setReadTimeout(2000);
                urlConnection.setConnectTimeout(2000);

                int responseCode = urlConnection.getResponseCode();

                if(responseCode > 400){
                    throw new IOException("Erro na comunicação do servidor");
                }

                InputStream is = new BufferedInputStream(urlConnection.getInputStream());

                JsonReader jsonReader = new JsonReader(new InputStreamReader(is));

                jsonReader.beginArray();

                while(jsonReader.hasNext()){
                    response.add(jsonReader.nextString());
                }

                jsonReader.endArray();

            } catch (MalformedURLException e) {
                msgErro = e.getMessage();
            } catch (IOException e) {
                msgErro = e.getMessage();
            }
            //Log.d("url", msgErro);
            return response;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            if(msgErro != null){
                callback.onError(msgErro);
                Log.i("test", msgErro);
            } else {
                callback.onSuccess(strings);
                Log.i("test", strings.toString());
            }
            callback.onComplete();
            //super.onPostExecute(strings);
        }
    }
}
