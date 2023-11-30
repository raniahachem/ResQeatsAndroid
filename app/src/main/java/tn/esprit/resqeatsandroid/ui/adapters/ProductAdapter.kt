package tn.esprit.resqeatsandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tn.esprit.resqeatsandroid.databinding.ItemProductBinding
import tn.esprit.resqeatsandroid.model.HomeItem

class ProductAdapter : ListAdapter<HomeItem.ProductItem, ProductAdapter.ViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productItem = getItem(position)
        holder.bind(productItem)
    }

    class ViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(productItem: HomeItem.ProductItem) {
            val product = productItem.product
            with(binding) {
                productName.text = product.title
                productPrice.text = product.price.toString()
                productCategory.text= product.category
                // Utilisez Glide avec override pour redimensionner l'image
                Glide.with(root.context)
                    .load(product.image)
                    .override(600, 600) // Ajustez ces valeurs en fonction de vos besoins
                    .into(productImage)
                // Add any other bindings you need
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
}
