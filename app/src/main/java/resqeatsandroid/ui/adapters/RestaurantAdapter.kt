package resqeatsandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tn.esprit.resqeatsandroid.databinding.ItemRestaurantBinding
import tn.esprit.resqeatsandroid.model.Restaurant

class RestaurantAdapter : ListAdapter<Restaurant, RestaurantAdapter.ViewHolder>(
    RestaurantDiffCallback()
) {

    // Déclaration d'une variable pour stocker l'écouteur de clic
    private var onItemClickListener: ((Restaurant) -> Unit)? = null

    // Fonction pour définir l'écouteur de clic depuis l'extérieur de l'adaptateur
    fun setOnItemClickListener(listener: (Restaurant) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = getItem(position)
        holder.bind(restaurant)
    }

    inner class ViewHolder(private val binding: ItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: Restaurant) {
            with(binding) {
                restaurantName.text = restaurant.username
                restaurantCategory.text = restaurant.category
                Glide.with(root.context)
                    .load(restaurant.image)
                    .override(1000, 600)
                    .into(restaurantImage)

                // Ajout d'un clic sur l'élément
                root.setOnClickListener {
                    onItemClickListener?.invoke(restaurant)
                }
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
