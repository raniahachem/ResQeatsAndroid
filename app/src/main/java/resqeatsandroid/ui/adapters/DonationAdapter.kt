package resqeatsandroid.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import resqeatsandroid.model.Donation
import tn.esprit.resqeatsandroid.R

class DonationAdapter(
    private val donations: List<Donation>,
    private val onDeleteClickListener: OnDeleteClickListener,
    private val onEditClickListener: OnEditClickListener,
    val onItemClick: (Donation) -> Unit
) : RecyclerView.Adapter<DonationAdapter.DonationViewHolder>() {

    // Define the callback interface for edit click
    interface OnDeleteClickListener {
        fun onDeleteClick(donation: Donation)
    }

    interface OnEditClickListener {
        fun onEditClick(donation: Donation)
    }

    class DonationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        val deleteButton: ImageButton = itemView.findViewById(R.id.eachDonationDeleteBtn)
        val editButton: ImageButton = itemView.findViewById(R.id.editDonationButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_donation, parent, false)
        return DonationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        val currentDonation = donations[position]
        "Title: ${currentDonation.title}".also { holder.titleTextView.text = it }
        "Description: ${currentDonation.description}".also { holder.descriptionTextView.text = it }
        "Quantity: ${currentDonation.quantity}".also { holder.quantityTextView.text = it }
        "Date: ${currentDonation.date}".also { holder.dateTextView.text = it }
        "Status: ${currentDonation.status}".also { holder.statusTextView.text = it }

        // Set up click listener for delete ImageButton
        holder.deleteButton.setOnClickListener {
            onDeleteClickListener.onDeleteClick(currentDonation)
        }

        // Set up click listener for edit ImageButton
        holder.editButton.setOnClickListener {
            onEditClickListener.onEditClick(currentDonation)
        }

        // Set up click listener for the entire item view
        holder.itemView.setOnClickListener {
            onItemClick(currentDonation)
        }
    }

    override fun getItemCount(): Int {
        return donations.size
    }
}
