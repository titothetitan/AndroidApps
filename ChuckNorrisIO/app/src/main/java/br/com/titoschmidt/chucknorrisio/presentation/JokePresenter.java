package br.com.titoschmidt.chucknorrisio.presentation;

import br.com.titoschmidt.chucknorrisio.JokeActivity;
import br.com.titoschmidt.chucknorrisio.datasource.JokeRemoteDataSource;
import br.com.titoschmidt.chucknorrisio.models.Joke;

public class JokePresenter implements JokeRemoteDataSource.JokeCallback {

    private final JokeActivity jokeActivity;
    private final JokeRemoteDataSource dataSource;

    public JokePresenter(JokeActivity jokeActivity, JokeRemoteDataSource dataSource) {
        this.jokeActivity = jokeActivity;
        this.dataSource = dataSource;
    }

    public void findJokeBy(String category){
        jokeActivity.mostraProgressBar();
        this.dataSource.findJokeBy(this, category);
    }

    @Override
    public void onSuccess(Joke joke){
        jokeActivity.mostraJoke(joke);
    }
    @Override
    public void onError(String msgErro){
        jokeActivity.mostraFalha(msgErro);
    }

    public void onComplete(){
        jokeActivity.escondeProgressBar();
    }
}
