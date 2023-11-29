package tn.esprit.resqeatsandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tn.esprit.resqeatsandroid.databinding.ItemRestaurantBinding
import tn.esprit.resqeatsandroid.model.Restaurant

class RestaurantAdapter(private val restaurants: List<Restaurant>) :
    RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = restaurants.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurants[position]
        with(holder.binding) {
            // Mettez à jour les vues de l'élément de la liste ici
            restaurantName.text = restaurant.username
            restaurantCategory.text = restaurant.category
            // Assurez-vous que vous avez une propriété 'image' dans votre modèle Restaurant
            // restaurantImage.setImageResource(restaurant.image)

            // Si vous avez une URL d'image, vous pouvez utiliser une bibliothèque comme Glide pour charger l'image
            // Glide.with(root.context).load(restaurant.image).into(restaurantImage)
        }
    }
}
