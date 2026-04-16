package com.example.ecommerceapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private val products: MutableList<Product>,
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_LIST = 1
        const val VIEW_TYPE_GRID = 2
    }

    var isGridMode = false

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduct: ImageView = itemView.findViewById(R.id.imgProduct)
        val txtName: TextView = itemView.findViewById(R.id.txtProductName)
        val txtCategory: TextView = itemView.findViewById(R.id.txtCategory)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
        val btnAddToCart: Button = itemView.findViewById(R.id.btnAddToCart)
    }

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduct: ImageView = itemView.findViewById(R.id.imgGridProduct)
        val txtName: TextView = itemView.findViewById(R.id.txtGridName)
        val txtPrice: TextView = itemView.findViewById(R.id.txtGridPrice)
        val btnCart: ImageButton = itemView.findViewById(R.id.btnGridCart)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGridMode) VIEW_TYPE_GRID else VIEW_TYPE_LIST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LIST) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_list, parent, false)
            ListViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_grid, parent, false)
            GridViewHolder(view)
        }
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = products[position]

        if (holder is ListViewHolder) {
            holder.imgProduct.setImageResource(product.imageRes)
            holder.txtName.text = product.name
            holder.txtCategory.text = product.category
            holder.ratingBar.rating = product.rating
            holder.txtPrice.text = "৳ ${product.price}"
            holder.btnAddToCart.text = if (product.inCart) "Added" else "Add to Cart"

            holder.btnAddToCart.setOnClickListener {
                onAddToCart(product)
                notifyItemChanged(position)
            }
        } else if (holder is GridViewHolder) {
            holder.imgProduct.setImageResource(product.imageRes)
            holder.txtName.text = product.name
            holder.txtPrice.text = "৳ ${product.price}"

            holder.btnCart.setOnClickListener {
                onAddToCart(product)
                notifyItemChanged(position)
            }
        }
    }

    fun updateData(newList: List<Product>) {
        val diffCallback = ProductDiffCallback(products, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        products.clear()
        products.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeItem(position: Int): Product {
        val removed = products[position]
        products.removeAt(position)
        notifyItemRemoved(position)
        return removed
    }

    fun restoreItem(product: Product, position: Int) {
        products.add(position, product)
        notifyItemInserted(position)
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val item = products.removeAt(fromPosition)
        products.add(toPosition, item)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun getProductAt(position: Int): Product = products[position]

    class ProductDiffCallback(
        private val oldList: List<Product>,
        private val newList: List<Product>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}