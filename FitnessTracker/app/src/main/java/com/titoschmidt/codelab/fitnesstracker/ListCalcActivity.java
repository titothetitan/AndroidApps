package com.titoschmidt.codelab.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.titoschmidt.codelab.fitnesstracker.util.Conversor;

import java.util.ArrayList;
import java.util.List;

public class ListCalcActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<Register> registros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calc);

        recyclerView = findViewById(R.id.recicler_view_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent it = getIntent();
                // type "imc" ou "tmb" vem de ImcActivity.openListCalcActivity()

                String param = it.getStringExtra("type"); // imc ou tmb
                registros = SqlHelper.getInstance(ListCalcActivity.this).listar(param);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainAdapter adapter = new MainAdapter(registros);
                        recyclerView.setAdapter(adapter);
                        adapter.setListener(new OnItemClickListener() {
                            @Override
                            public void onClick(int id) {
                                Register registro = SqlHelper.getInstance(ListCalcActivity.this).buscar(id);
                                //Toast.makeText(ListCalcActivity.this, "Registro"+registro.getId()+registro.getCreatedDate()+registro.getType(), Toast.LENGTH_SHORT).show();
                                mostrarOpcoes(registro);
                            }
                        });
                        Log.d("tt", registros.toString());
                    }
                });
            }
        }).start();
    }

    public void editar(final int id){
        //View editDialog = getLayoutInflater().inflate(R.layout.main_edit_dialog, null);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Informe o novo valor");
        input.setGravity(Gravity.CENTER);
        AlertDialog dialog = new AlertDialog.Builder(ListCalcActivity.this)
                .setView(input)
                .setTitle("Editar")
                .setMessage("")
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final boolean atualizou = SqlHelper.getInstance(ListCalcActivity.this).atualizar(id, Double.parseDouble(String.valueOf(input.getText())));
                        if(atualizou){
                            Toast.makeText(ListCalcActivity.this, "Registro editado com sucesso!", Toast.LENGTH_SHORT).show();
                            // Reinicia a Activity
                            finish();
                            startActivity(getIntent());
                        } else {
                            Toast.makeText(ListCalcActivity.this, "Ocorreu um erro ao editar o registro.", Toast.LENGTH_SHORT).show();
                        }
                        dialog.cancel();

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        dialog.show();
    }

    public void excluir(final int id){
        AlertDialog dialog = new AlertDialog.Builder(ListCalcActivity.this)
                .setTitle("Excluir")
                .setMessage("Tem certeza que quer excluir este registro?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final boolean excluiu = SqlHelper.getInstance(ListCalcActivity.this).excluir(id);
                        if(excluiu){
                            Toast.makeText(ListCalcActivity.this, "Registro excluído com sucesso!", Toast.LENGTH_SHORT).show();
                            // Reinicia a Activity
                            finish();
                            startActivity(getIntent());
                        } else {
                            Toast.makeText(ListCalcActivity.this, "Ocorreu um erro ao excluir o registro.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        dialog.show();
    }

    public void mostrarOpcoes(final Register registro){
        //Toast.makeText(ListCalcActivity.this, "O ID do registro é "+idRegistro, Toast.LENGTH_SHORT).show();
        AlertDialog dialog = new AlertDialog.Builder(ListCalcActivity.this)
                .setTitle("Opções")
                .setMessage("O que você deseja fazer?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editar(registro.getId());
                    }
                }).setNeutralButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluir(registro.getId());
                    }
                })
                .create();
        dialog.show();
    }
    // Faz a conexão entre os registros e o ReciclerView
    private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>{
        // Lista de registros
        private List<Register> registros;
        // Ouvinte do click
        private OnItemClickListener listener;

        // Construtor
        public MainAdapter(List<Register> registros){
            this.registros = registros;
        }
        // Seta o ouvinte do click
        public void setListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MainViewHolder(getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
            Register registroAtual =  registros.get(position);
            holder.bind(registroAtual);
        }

        @Override
        public int getItemCount() {
            return registros.size();
        }

        // View da célula que está dentro da RecyclerView
        private class MainViewHolder extends RecyclerView.ViewHolder{

            public MainViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void bind(final Register registro){
                //((TextView) itemView).setText(registro.getType()+" "+registro.getResponse()+" "+registro.getCreatedDate()); *alt
                TextView tv = (TextView) itemView;

                int id = registro.getId();
                String type = registro.getType();
                Double response = registro.getResponse();
                String createdDate = registro.getCreatedDate();
                String modifiedDate = registro.getModifiedDate();

                tv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        listener.onClick(registro.getId());
                        return true;
                    }
                });

                switch (type){
                    case "imc":
                        tv.setText("Seu IMC: [" + Conversor.round(response,2) + "] Em: "+createdDate);
                        break;
                    case "tmb":
                        tv.setText("Seu TMB: [" + Conversor.round(response,2) + "] Em: "+createdDate);
                }
            }
        }
    }
}