package br.com.titoschmidt.chucknorrisio.presentation;

import java.util.ArrayList;
import java.util.List;

import br.com.titoschmidt.chucknorrisio.Cores;
import br.com.titoschmidt.chucknorrisio.MainActivity;
import br.com.titoschmidt.chucknorrisio.datasource.CategoryRemoteDataSource;
import br.com.titoschmidt.chucknorrisio.models.CategoryItem;

public class CategoryPresenter implements CategoryRemoteDataSource.ListCategoriesCallback {

    private final MainActivity mainActivity;
    private final CategoryRemoteDataSource dataSource;

    public CategoryPresenter(MainActivity mainActivity, CategoryRemoteDataSource dataSource) {
        this.mainActivity = mainActivity;
        this.dataSource = dataSource;
    }

    public void requestAll(){
        mainActivity.exibirProgressBar();
        this.dataSource.findAll(this);
    }
    @Override
    public void onSuccess(List<String> response){
        List<CategoryItem> categoryItens = new ArrayList();
        for(String val: response){
            categoryItens.add(new CategoryItem(val, Cores.randomColor()));
        }
        mainActivity.showCategories(categoryItens);
    }
    @Override
    public void onError(String mensagem){
        mainActivity.exibirFalha(mensagem);
    }

    // Retornando sucesso ou falha chama-se o onComplete
    public void onComplete(){
        mainActivity.esconderProgressBar();
    }
    /*
    public void request(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    onSuccess(fakeResponse);
                } catch (Exception e) {
                    onError(e.getMessage());
                } finally {
                    onComplete();
                }
            }
        },3000);
    }*/
}
