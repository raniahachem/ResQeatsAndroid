package tn.esprit.resqeatsandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tn.esprit.resqeatsandroid.databinding.ItemProductSupplierBinding
import tn.esprit.resqeatsandroid.model.HomeItem

class ProductSupplierAdapter : ListAdapter<HomeItem.ProductItem, ProductSupplierAdapter.ViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductSupplierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productItem = getItem(position)
        holder.bind(productItem)
    }

    class ViewHolder(private val binding: ItemProductSupplierBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(productItem: HomeItem.ProductItem) {
            val product = productItem.product
            with(binding) {
                productName.text = product.title
                val priceWithCurrency = "${product.price} TND"
                productPrice.text = priceWithCurrency
                productCategory.text = product.category
                Glide.with(root.context)
                    .load(product.image)
                    .override(600, 600)
                    .into(productImage)
            }
        }
    }

    private class ProductDiffCallback : DiffUtil.ItemCallback<HomeItem.ProductItem>() {
        override fun areItemsTheSame(oldItem: HomeItem.ProductItem, newItem: HomeItem.ProductItem): Boolean {
            return oldItem.product._id == newItem.product._id
        }

        override fun areContentsTheSame(oldItem: HomeItem.ProductItem, newItem: HomeItem.ProductItem): Boolean {
            return oldItem == newItem
        }
    }
    fun updateList(newList: List<HomeItem.ProductItem>) {
        submitList(newList)
    }

}
