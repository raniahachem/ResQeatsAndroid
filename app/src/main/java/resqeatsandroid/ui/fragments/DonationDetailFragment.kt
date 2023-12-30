package resqeatsandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import resqeatsandroid.viewmodel.DonationViewModel
import tn.esprit.resqeatsandroid.R

class DonationDetailFragment : Fragment() {

    private val viewModel: DonationViewModel by activityViewModels()
    private lateinit var backButton: ImageButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_donation_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the selected donation from the ViewModel
        val selectedDonation = viewModel.selectedDonation

        backButton = view.findViewById(R.id.backButton)


        // Find TextViews by ID
        val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        val descriptionTextView = view.findViewById<TextView>(R.id.descriptionTextView)
        val quantityTextView = view.findViewById<TextView>(R.id.quantityTextView)
        val dateTextView = view.findViewById<TextView>(R.id.dateTextView)
        val statusTextView = view.findViewById<TextView>(R.id.statusTextView)

        // Populate the UI with donation details
        titleTextView.text = "Title: ${selectedDonation?.title}"
        descriptionTextView.text = "Description: ${selectedDonation?.description}"
        quantityTextView.text = "Quantity: ${selectedDonation?.quantity}"
        dateTextView.text = "Date: ${selectedDonation?.date}"
        statusTextView.text = "Status: ${selectedDonation?.status}"

        // Add any additional views or details as needed
        // Handle back button click event
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}