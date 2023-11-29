package tn.esprit.resqeatsandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
                productCategory.text = product.category
                // Même idée pour les autres champs du produit

                // Mettez à jour la TextView du prix avec la valeur du modèle Product
                productPrice.text = "Price: $${product.price}"  // Utilisez product.price au lieu de productItem.price
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
