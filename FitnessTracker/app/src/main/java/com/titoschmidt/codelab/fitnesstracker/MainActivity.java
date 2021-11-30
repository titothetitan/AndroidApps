package com.titoschmidt.codelab.fitnesstracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private RecyclerView  rvMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		List<MainItem> mainItens = new ArrayList<>();
		mainItens.add(new MainItem(1, R.drawable.balanca_1, Color.WHITE, R.string.label_IMC,R.color.colorAccent));
		mainItens.add(new MainItem(2, R.drawable.peso_1, Color.WHITE, R.string.label_TMB, R.color.colorAccent));
		mainItens.add(new MainItem(3, R.drawable.doctor_1, Color.WHITE, R.string.label_conselhos, R.color.colorAccent));
		mainItens.add(new MainItem(4, R.drawable.water_1, Color.WHITE, R.string.label_hidratacao, R.color.colorAccent));
		mainItens.add(new MainItem(5, R.drawable.dinner_1, Color.WHITE, R.string.label_dieta, R.color.colorAccent));
		mainItens.add(new MainItem(6, R.drawable.exercicios_1, Color.WHITE, R.string.label_exercicios, R.color.colorAccent));

		rvMain = findViewById(R.id.main_rv);

		/* Define o comportamento de exibição do layout da RecyclerView
		   - linear (vertical | horizontal)
		   - grid
		   - mosaic
		 */
		rvMain.setLayoutManager(new GridLayoutManager(this,2));

		MainAdapter adapter = new MainAdapter(mainItens);

		adapter.setListener(new OnItemClickListener() {
			@Override
			public void onClick(int id) {
				Intent it;
				switch (id){
					// Abre a activity IMC
					case 1:
						it = new Intent(MainActivity.this, ImcActivity.class);
						it.putExtra("type", "imc");
						startActivity(it);
						break;
					// Abre a activity TMB
					case 2:
						it = new Intent(MainActivity.this, TmbActivity.class);
						it.putExtra("type", "tmb");
						startActivity(it);
						break;
					case 3:
						it = new Intent(MainActivity.this, Conselhos.class);
						startActivity(it);
						break;
				}
			}
		});
		rvMain.setAdapter(adapter);
	}
	// Faz a conexão entre a célula (main_item) e o ReciclerView
	private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>{
		// Lista que armazena os itens da RecyclerView
		private List<MainItem> mainItens;
		private OnItemClickListener listener;

		// Construtor
		public MainAdapter(List<MainItem> mainItens){
			this.mainItens = mainItens;
		}

		public void setListener(OnItemClickListener listener) {
			this.listener = listener;
		}

		@NonNull
		@Override
		public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			return new MainViewHolder(getLayoutInflater().inflate(R.layout.main_item, parent, false));
		}
		// Método que vai atualizar o conteúdo mostrado na RecyclerView durante a rolagem
		@Override
		public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
			MainItem mainItemCurrent = mainItens.get(position);
			holder.bind(mainItemCurrent);
		}
		// Método que retorna a quantidade de itens da RecyclerView
		@Override
		public int getItemCount() {
			return mainItens.size();
		}
			// View da célula (main_item) que está dentro da RecyclerView
			private class MainViewHolder extends RecyclerView.ViewHolder{

				public MainViewHolder(@NonNull View itemView) {
					super(itemView);
				}

				public void bind(final MainItem item) {
					TextView txtNome = itemView.findViewById(R.id.item_txt_nome);
					ImageView imgIcone = itemView.findViewById(R.id.item_icone);
					LinearLayout btnImc = (LinearLayout) itemView.findViewById(R.id.btn_imc);
					// O listener nativo invoca o listener criado por mim pois o nativo recebe uma View e o meu recebe um ID
					// para a magia funcionar foi criado a classe interface OnItemClickListener
					btnImc.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							listener.onClick(item.getId());
						}
					});

					txtNome.setText(item.getTextStringId());
					imgIcone.setImageResource(item.getDrawable());
					imgIcone.setColorFilter(item.getDrawableColor());
					btnImc.setBackgroundResource(item.getColor());
				}
			}
	}
}

