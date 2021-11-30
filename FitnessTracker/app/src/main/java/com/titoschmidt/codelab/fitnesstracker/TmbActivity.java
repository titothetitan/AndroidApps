package com.titoschmidt.codelab.fitnesstracker;

import androidx.annotation.NonNull;
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
import android.widget.Spinner;
import android.widget.Toast;

public class TmbActivity extends AppCompatActivity {

    private EditText editPeso;
    private EditText editAltura;
    private EditText editIdade;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmb);

        editPeso = findViewById(R.id.edit_tmb_peso);
        editAltura = findViewById(R.id.edit_tmb_altura);
        editIdade = findViewById(R.id.edit_tmb_idade);
        spinner = findViewById(R.id.tmb_lifestyle);

        Button btnEnviar = findViewById(R.id.tmb_enviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validar()){
                    Toast.makeText(TmbActivity.this, R.string.campo_invalido, Toast.LENGTH_SHORT).show();
                } else {
                    String sPeso = editPeso.getText().toString();
                    String sAltura = editAltura.getText().toString();
                    String sIdade = editIdade.getText().toString();

                    int peso = Integer.parseInt(sPeso);
                    int altura = Integer.parseInt(sAltura);
                    int idade = Integer.parseInt(sIdade);

                    final double resultado = calcularTmb(altura, peso, idade);

                    double tmb = respostaTmb(resultado);
                    // Exibe uma mensagem dinâmica (resposta_tmb) numa pop-up
                    AlertDialog dialog = new AlertDialog.Builder(TmbActivity.this)
                            .setTitle("")
                            // string dinamica para aceitar outros valores de variavies, como tmb
                            .setMessage(getString(R.string.resposta_tmb, tmb))
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
                                            final long calcId = SqlHelper.getInstance(TmbActivity.this).addItem("tmb", resultado);
                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    if(calcId>0){
                                                        Toast.makeText(TmbActivity.this, R.string.registro_salvo, Toast.LENGTH_SHORT).show();
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
        if(item.getItemId() == R.id.menu_list){
            openListCalcActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openListCalcActivity(){
        Intent it = new Intent(TmbActivity.this, ListCalcActivity.class);
        it.putExtra("type", "tmb");
        startActivity(it);
    }

    private double respostaTmb(double tmb){

        int index = spinner.getSelectedItemPosition();

        switch (index) {
            case 0:
                return tmb * 1.2;
            case 1:
                return tmb * 1.375;
            case 2:
                return tmb * 1.55;
            case 3:
                return tmb * 1.725;
            case 4:
                return tmb * 1.9;
            default:
                return 0;
        }
    }

    private double calcularTmb(int altura, int peso, int idade){
        return 66 + (peso * 13.8) * (5 * altura) - (6.8 * altura);
    }

    private boolean validar(){
        if(!editPeso.getText().toString().isEmpty()
                && !editAltura.getText().toString().isEmpty()
                && !editIdade.getText().toString().isEmpty()
                && !editPeso.getText().toString().startsWith("0")
                && !editAltura.getText().toString().startsWith("0")
                && !editIdade.getText().toString().startsWith("0")
        ){
            return true;
        } else {
            return false;
        }
    }
}