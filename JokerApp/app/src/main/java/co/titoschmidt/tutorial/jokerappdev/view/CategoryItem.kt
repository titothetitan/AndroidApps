package co.titoschmidt.tutorial.jokerappdev.view

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import co.titoschmidt.tutorial.jokerappdev.model.Category
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import titoschmidt.tutorial.jokerappdev.R

/*
 * 08/12/2021
 * @author titoesa1988@gmail.com (Tito Schmidt).
 * 
 */
class CategoryItem(val category: Category) : Item<CategoryItem.CategoryViewHolder>(){

    class CategoryViewHolder(view: View) : GroupieViewHolder(view)
    // itemView é o LinearLayout em item_category.xml
    override fun createViewHolder(itemView: View) = CategoryViewHolder(itemView)

    override fun bind(viewHolder: CategoryViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.txt_category).text = category.name
        viewHolder.itemView.findViewById<LinearLayout>(R.id.container_category).setBackgroundColor(category.bgColor.toInt())
    }

    override fun getLayout(): Int {
        return R.layout.item_category
    }
}