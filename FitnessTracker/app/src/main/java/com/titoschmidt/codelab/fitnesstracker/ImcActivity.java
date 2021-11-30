package com.titoschmidt.codelab.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.titoschmidt.codelab.fitnesstracker.util.Conversor;

public class ImcActivity extends AppCompatActivity {

    private EditText editPeso;
    private EditText editAltura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);

        editPeso = findViewById(R.id.edit_imc_peso);
        editAltura = findViewById(R.id.edit_imc_altura);

        Button btnEnviar = findViewById(R.id.btn_enviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validar()){
                    Toast.makeText(ImcActivity.this, R.string.campo_invalido, Toast.LENGTH_SHORT).show();
                } else {
                    String sPeso = editPeso.getText().toString();
                    String sAltura = editAltura.getText().toString();

                    int peso = Integer.parseInt(sPeso);
                    int altura = Integer.parseInt(sAltura );

                    final double resultado = calcularImc(altura, peso);

                    int idRespostaImc = respostaImc(resultado);
                    // Exibe uma mensagem dinâmica (resposta_imc) numa pop-up
                    AlertDialog dialog = new AlertDialog.Builder(ImcActivity.this)
                            .setTitle(getString(R.string.resposta_imc, resultado))
                            .setMessage(idRespostaImc)
                            // Button OK
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            // Button Salvar
                            .setNegativeButton(R.string.salvar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Thread utilizada para a pop-up não travar a interface gráfica
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Salva o item na database
                                            final long calcId = SqlHelper.getInstance(ImcActivity.this).addItem("imc", resultado);
                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    if(calcId>0){
                                                        Toast.makeText(ImcActivity.this, R.string.registro_salvo, Toast.LENGTH_SHORT).show();
                                                        openListCalcActivity();
                                                    }
                                                }
                                            });
                                        }
                                    }).start();
                                }
                            })
                            .create();
                    dialog.show();
                    // Esconde o teclado enquanto a pop-up estiver sendo exibida
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editPeso.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(editAltura.getWindowToken(), 0);

                    //Log.d("TESTE", "resultado: " + resultado);
                }
            }
        });
    }
    // Exibe o ícone do menu no canto superior direito da tela
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    // Verifica pelo ID qual Menu foi clicado, no caso há somente um Menu (menu_list)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_list:
                openListCalcActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Metódo utilizado após salvar o registro ou abrir o Menu contendo os registros
    // como passei o parâmetro type "imc", a ListCalcActivity vai será toda setada para Imc
    private void openListCalcActivity(){
        Intent it = new Intent(ImcActivity.this, ListCalcActivity.class);
        it.putExtra("type", "imc");
        startActivity(it);
    }

    @StringRes
    private int respostaImc(double imc){
        if(imc<15){
            return R.string.imc_severamente_abaixo;
        } else if(imc<16){
            return R.string.imc_muito_abaixo;
        } else if(imc<18.5){
            return R.string.imc_abaixo;
        } else if(imc<25){
            return R.string.imc_normal;
        } else if(imc<30){
            return R.string.imc_acima;
        } else if(imc<35){
            return R.string.imc_muito_acima;
        } else if(imc<40){
            return R.string.imc_severamente_acima;
        } else {
            return R.string.imc_extremamente_acima;
        }
    }

    private double calcularImc(int altura, int peso){
        return peso / ( ((double) altura / 100) * ((double) altura / 100) );
    }

    private boolean validar(){
        if(! editPeso.getText().toString().isEmpty()
          && !editAltura.getText().toString().isEmpty()
          && !editPeso.getText().toString().startsWith("0")
          && !editAltura.getText().toString().startsWith("0")
        ){
            return true;
        } else {
            return false;
        }
    }
}