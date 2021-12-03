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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import br.com.titoschmidt.netflixremake.model.Category;
import br.com.titoschmidt.netflixremake.model.Movie;

public class CategoryTask extends AsyncTask<String, Void, List<Category>> {

    /*

    Classe que converte os dados vindos da internet (inputStream) para String, depois para Json, depois para listas de objetos Java

    */

    /*

    Como as requisições são realizadas paralelamente, ou seja, eu uma thread separada para que a interface do usuário não fique travada,
    precisamos de alguma forma, pegar o retorno dos dados vindos da web e mandar para a Activity de volta.
    Isso é feito com o CategoryLoader. Ou seja, a Activity implementa essa interface CategoryLoader e obrigatóriamente o seu método (onResult)
    uma vez que, a classe CategoryTask  precisar notificar que terminou o processamento dos dados, ele informa ao CategoryLoader os dados
    novos e exibe na tela da Atividade.

     */

    // Se a CategoryTask for eliminada por alguma fonte
    //externa, WeakReference impede que a classe trave a app
    private final WeakReference<Context> context;
    ProgressDialog dialog;
    private CategoryLoader categoryLoader;

    public CategoryTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void setCategoryLoader(CategoryLoader categoryLoader) {
        this.categoryLoader = categoryLoader;
    }

    // main-thread
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.context.get();
        if (context != null) {
            dialog = ProgressDialog.show(context, "Carregando...", "", true);
        }
    }

    @Override
    protected List<Category> doInBackground(String... params) {
        String url = params[0];
        try {
            URL requestURL = new URL(url);
            HttpsURLConnection urlConnection = (HttpsURLConnection) requestURL.openConnection();
            // Tempo de espera de leitura - 2 segs
            urlConnection.setReadTimeout(2000);
            // Se a internet caiu, espera 2 segs para exibir msg de erro
            urlConnection.setConnectTimeout(2000);
            // pega a resposta do servidor
            int responseCode = urlConnection.getResponseCode();
            // se a resposta for maior que 400 então algo deu errado
            if (responseCode > 400) {
                throw new IOException("Erro na comunicação do servidor");
            }
            // converte os dados vindos da internet (inputStream) para String, depois para Json, depois para listas de objetos Java
            InputStream inputStream = urlConnection.getInputStream();

            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            String jsonAsString = toString(bufferedInputStream);

            List<Category> categories = getCategories(new JSONObject(jsonAsString));
            bufferedInputStream.close();
            return categories;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Category> getCategories(JSONObject json) throws JSONException {

        List<Category> categorias = new ArrayList<>();

        JSONArray categoryArray = json.getJSONArray("category");

        for (int i = 0; i < categoryArray.length(); i++) {
            JSONObject categoria = categoryArray.getJSONObject(i);
            String title = categoria.getString("title");

            JSONArray filmesArray = categoria.getJSONArray("movie");
            List<Movie> filmes = new ArrayList<>();
            for (int j = 0; j < filmesArray.length(); j++) {

                JSONObject filme = filmesArray.getJSONObject(j);
                int id = filme.getInt("id");
                String filmeURL = filme.getString("cover_url");
                Movie filmeObj = new Movie();
                filmeObj.setId(id);
                filmeObj.setCoverUrl(filmeURL);
                filmes.add(filmeObj);

            }
            Category categoriaObj = new Category();
            categoriaObj.setName(title);
            categoriaObj.setMovies(filmes);
            categorias.add(categoriaObj);
        }
        return categorias;
    }

    // main-threads
    @Override
    protected void onPostExecute(List<Category> categories) {
        super.onPostExecute(categories);
        dialog.dismiss();
        if (categoryLoader != null) {
            categoryLoader.onResult(categories);
        }
    }

    private String toString(InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = is.read(bytes)) > 0) {
            baos.write(bytes, 0, lidos);
        }
        return new String(baos.toByteArray());
    }

    public interface CategoryLoader {
        void onResult(List<Category> categorias);
    }
}
