package br.com.titoschmidt.chucknorrisio.models;

import android.support.annotation.NonNull;
import android.widget.TextView;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;
import br.com.titoschmidt.chucknorrisio.R;

public class CategoryItem extends Item<ViewHolder> {

    private final String nomeCategoria;
    private  final int bgColor;

    public CategoryItem(String nomeCategoria, int bgColor) {
        this.nomeCategoria = nomeCategoria;
        this.bgColor = bgColor;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    @Override
    public void bind(@NonNull ViewHolder viewHolder, int position) {
        TextView txtCategory = viewHolder.itemView.findViewById(R.id.txt_category);
        txtCategory.setText(nomeCategoria);
        viewHolder.itemView.setBackgroundColor(bgColor);
    }

    @Override
    public int getLayout() {
        return R.layout.card_category;
    }
}
