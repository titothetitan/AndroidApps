package br.com.titoschmidt.chucknorrisio.datasource;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import br.com.titoschmidt.chucknorrisio.models.Joke;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JokeRemoteDataSource {

    public interface JokeCallback {
        void onSuccess(Joke joke);

        void onError(String msgErro);

        void onComplete();
    }

    public void findJokeBy(JokeCallback callback, String category){
        //Cria uma instância concreta do Retrofit e passa a API com todos os métodos disponíveis
        HTTPClient.retrofit().create(ChuckNorrisAPI.class)
                .findRandomBy(category)
                .enqueue(new Callback<Joke>() {
                    @Override
                    public void onResponse(Call<Joke> call, Response<Joke> response) {
                        if(response.isSuccessful())
                            callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<Joke> call, Throwable t) {
                        // Mostra uma msg de erro
                        callback.onError(t.getMessage());
                        // Esconde a progress bar
                        callback.onComplete();
                    }
                });


        // Método antigo sem uso de Retrofit
        //new JokeTask(callback, category).execute();
    }

    private static class JokeTask extends AsyncTask<Void, Void, Joke>{

        private String msgErro;
        private JokeCallback callback;
        private String category;

        public JokeTask(JokeCallback callback, String category) {
            this.callback = callback;
            this.category = category;
        }

        @Override
        protected Joke doInBackground(Void... voids) {
            String endpoint = String.format("%s?category=%s", Endpoint.GET_JOKE, category);
            Joke joke = null;
            HttpsURLConnection urlConnection;
            try {
                URL url = new URL(endpoint);
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(2000);
                urlConnection.setReadTimeout(2000);

                int responseCode = urlConnection.getResponseCode();

                if(responseCode > 400){
                    throw new IOException("Erro na comunicação do servidor");
                }

                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                JsonReader jsonReader = new JsonReader(new InputStreamReader(is));

                jsonReader.beginObject();

                String iconUrl = null;
                String value = null;

                while(jsonReader.hasNext()){
                    JsonToken token = jsonReader.peek();

                    if(token == JsonToken.NAME) {
                        String name = jsonReader.nextName();
                        if (name.equals("category")) {
                            jsonReader.skipValue();
                        } else if (name.equals("icon_url")) {
                            iconUrl = jsonReader.nextString();
                        } else if (name.equals("value")) {
                            value = jsonReader.nextString();
                        } else {
                            // Pula os demais valores que não preciso
                            jsonReader.skipValue();
                        }
                    }
                }

                joke = new Joke(iconUrl, value);
                jsonReader.endObject();

            } catch (MalformedURLException e) {
                msgErro = e.getMessage();
            } catch (IOException e) {
                msgErro = e.getMessage();
            }
            Log.d("con", "erro:"+msgErro);
            return joke;
        }

        @Override
        protected void onPostExecute(Joke joke) {
            if(msgErro != null){
                callback.onError(msgErro);
            } else {
                callback.onSuccess(joke);
            }
            callback.onComplete();
        }
    }
}
