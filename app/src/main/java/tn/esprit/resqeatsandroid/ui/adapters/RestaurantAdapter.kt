import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.resqeatsandroid.databinding.ItemRestaurantBinding
import tn.esprit.resqeatsandroid.model.HomeItem

class RestaurantAdapter : ListAdapter<HomeItem.RestaurantItem, RestaurantAdapter.ViewHolder>(RestaurantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurantItem = getItem(position)
        holder.bind(restaurantItem)
    }

    class ViewHolder(private val binding: ItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurantItem: HomeItem.RestaurantItem) {
            val restaurant = restaurantItem.restaurant
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

    private class RestaurantDiffCallback : DiffUtil.ItemCallback<HomeItem.RestaurantItem>() {
        override fun areItemsTheSame(oldItem: HomeItem.RestaurantItem, newItem: HomeItem.RestaurantItem): Boolean {
            return oldItem.restaurant._id == newItem.restaurant._id
        }

        override fun areContentsTheSame(oldItem: HomeItem.RestaurantItem, newItem: HomeItem.RestaurantItem): Boolean {
            return oldItem == newItem
        }
    }
}
