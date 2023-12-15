package resqeatsandroid.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import resqeatsandroid.model.Donation
import tn.esprit.resqeatsandroid.R

class DonationAdapter(
    private val donations: List<Donation>,
    private val onDeleteClickListener: OnDeleteClickListener,
    private val onEditClickListener: OnEditClickListener // Added this line
) : RecyclerView.Adapter<DonationAdapter.DonationViewHolder>() {

    // Define the callback interface for edit click
    interface OnEditClickListener {
        fun onEditClick(donation: Donation)
    }

    // Define the callback interface for delete click
    interface OnDeleteClickListener {
        fun onDeleteClick(donation: Donation)
    }

    class DonationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val etatTextView: TextView = itemView.findViewById(R.id.etatTextView)
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

        holder.quantityTextView.text = "Quantity: ${currentDonation.quantite}"
        holder.dateTextView.text = "Date: ${currentDonation.date}"
        holder.etatTextView.text = "Etat: ${currentDonation.etat}"

        // Set up click listener for delete ImageButton
        holder.deleteButton.setOnClickListener {
            onDeleteClickListener.onDeleteClick(currentDonation)
        }

        // Set up click listener for edit ImageButton
        holder.editButton.setOnClickListener {
            onEditClickListener.onEditClick(currentDonation)
        }
    }

    override fun getItemCount(): Int {
        return donations.size
    }
}