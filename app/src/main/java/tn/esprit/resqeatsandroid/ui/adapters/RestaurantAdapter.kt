package tn.esprit.resqeatsandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.resqeatsandroid.databinding.ItemRestaurantBinding
import tn.esprit.resqeatsandroid.model.Restaurant

class RestaurantAdapter : ListAdapter<Restaurant, RestaurantAdapter.ViewHolder>(RestaurantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = getItem(position)
        holder.bind(restaurant)
    }

    class ViewHolder(private val binding: ItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: Restaurant) {
            with(binding) {
                restaurantName.text = restaurant.username
                restaurantCategory.text = restaurant.category
                // Assurez-vous que vous avez une propriété 'image' dans votre modèle Restaurant
                // restaurantImage.setImageResource(restaurant.image)

                // Si vous avez une URL d'image, vous pouvez utiliser une bibliothèque comme Glide pour charger l'image
                // Glide.with(root.context).load(restaurant.image).into(restaurantImage)
            }
        }
    }

    private class RestaurantDiffCallback : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem == newItem
        }
    }
}
