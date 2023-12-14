package resqeatsandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import android.widget.Button
import android.widget.EditText
import resqeatsandroid.model.Donation
import tn.esprit.resqeatsandroid.R
import resqeatsandroid.viewmodel.DonationViewModel

class EditDonationFragment : Fragment() {

    private lateinit var donationViewModel: DonationViewModel
    private lateinit var etEditQuantity: EditText
    private lateinit var etEditDate: EditText
    private lateinit var etEditEtat: EditText
    private lateinit var btnUpdateDonation: Button
    private var donationId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_donation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etEditQuantity = view.findViewById(R.id.etEditQuantity)
        etEditDate = view.findViewById(R.id.etEditDate)
        etEditEtat = view.findViewById(R.id.etEditEtat)
        btnUpdateDonation = view.findViewById(R.id.btnUpdateDonation)

        //donationId = EditDonationFragmentArgs.fromBundle(requireArguments()).donationId

        // Initialize ViewModel
        donationViewModel = ViewModelProvider(this).get(DonationViewModel::class.java)

        // Observe the donation data
        donationViewModel.getDonationById(donationId).observe(viewLifecycleOwner, Observer { donation ->
            donation?.let {
                // Populate UI with donation data
                etEditQuantity.setText(donation.quantite.toString())
                etEditDate.setText(donation.date)
                etEditEtat.setText(donation.etat)
            }
        })

        // Set up click listener for the "Update" button
        btnUpdateDonation.setOnClickListener {
            // Call the function to update the donation
            updateDonation()
        }
    }

    private fun updateDonation() {
        // Get the values from UI fields
        val updatedQuantite = etEditQuantity.text.toString().toInt()
        val updatedDate = etEditDate.text.toString()
        val updatedEtat = etEditEtat.text.toString()

        // Create a new Donation object with updated information
        val updatedDonation = Donation(donationId, updatedQuantite, updatedDate, updatedEtat)

        // Make a PUT request to update the donation on the server
        //donationViewModel.updateDonation(updatedDonation)
    }
}
