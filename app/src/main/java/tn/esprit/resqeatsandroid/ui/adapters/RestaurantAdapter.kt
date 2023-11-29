package tn.esprit.resqeatsandroid.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.resqeatsandroid.R
import tn.esprit.resqeatsandroid.model.Restaurant


class RestaurantAdapter(private val restaurants: List<Restaurant>) :
    RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = restaurants.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Mettez à jour les vues de l'élément de la liste ici
        // Par exemple, utilisez findViewById pour accéder aux vues et mettez à jour les valeurs avec les données du restaurant
    }
}
