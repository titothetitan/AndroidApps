package br.com.titoschmidt.catsapp.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import br.com.titoschmidt.catsapp.R
import br.com.titoschmidt.catsapp.model.Cat
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.main_item.*

/*
 * 16/12/2021
 * @author titoesa1988@gmail.com (Tito Schmidt).
 * 
 */

class MainItem (val cat: Cat) : Item<MainItem.MainViewHolder>(){

    class MainViewHolder(view: View) : GroupieViewHolder(view)

    override fun createViewHolder(itemView: View) = MainViewHolder(itemView)

    override fun bind(viewHolder: MainViewHolder, position: Int) {
        val imageView = viewHolder.itemView.findViewById<ImageView>(R.id.image_view_cat)
        Picasso.get().load(cat.imgUrl).into(imageView)
    }

    override fun getLayout(): Int {
        return R.layout.main_item
    }
}