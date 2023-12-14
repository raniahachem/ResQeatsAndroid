package tn.esprit.resqeatsandroid.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tn.esprit.resqeatsandroid.R
import tn.esprit.resqeatsandroid.model.CartItem

class CartItemAdapter(private val listener: OnItemClickListener) :
    ListAdapter<CartItem, CartItemAdapter.CartItemViewHolder>(CartItemDiffCallback()) {

    interface OnItemClickListener {
        fun onDeleteClicked(cartItem: CartItem)
        fun onQuantityChanged(cartItem: CartItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.each_cart, parent, false)
        return CartItemViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val currentCartItem = getItem(position)
        holder.bind(currentCartItem)
    }

    inner class CartItemViewHolder(itemView: View, private val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(cartItem: CartItem) {
            with(itemView) {
                Glide.with(context)
                    .load(cartItem.productImage)
                    .into(findViewById<ImageView>(R.id.productImage))
                findViewById<TextView>(R.id.productName).text = cartItem.productName
                findViewById<TextView>(R.id.productCategory).text = cartItem.productCategory
                findViewById<TextView>(R.id.productPrice).text = cartItem.productPrice.toString()
                findViewById<TextView>(R.id.eachCartItemQuantity).text = cartItem.quantity.toString()

                findViewById<ImageButton>(R.id.eachCartItemDeleteBtn).setOnClickListener {
                    listener.onDeleteClicked(cartItem)
                }

                findViewById<ImageButton>(R.id.eachCartItemAddQuantityBtn).setOnClickListener {
                    cartItem.quantity++
                    findViewById<TextView>(R.id.eachCartItemQuantity).text =
                        cartItem.quantity.toString()
                    listener.onQuantityChanged(cartItem)
                }

                findViewById<ImageButton>(R.id.eachCartItemMinusQuantityBtn).setOnClickListener {
                    if (cartItem.quantity > 1) {
                        cartItem.quantity--
                        findViewById<TextView>(R.id.eachCartItemQuantity).text =
                            cartItem.quantity.toString()
                        listener.onQuantityChanged(cartItem)
                    }
                }
            }
        }
    }

    private class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}
