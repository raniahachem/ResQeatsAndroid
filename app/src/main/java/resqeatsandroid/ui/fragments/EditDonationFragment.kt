// EditDonationFragment.kt
package resqeatsandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import resqeatsandroid.model.Donation
import resqeatsandroid.network.RetrofitDonation.donationApi
import resqeatsandroid.viewmodel.DonationViewModel
import tn.esprit.resqeatsandroid.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditDonationFragment : Fragment() {
    private lateinit var updateButton: Button
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var statusEditText: EditText
    private val viewModel: DonationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_donation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateButton = view.findViewById(R.id.btnUpdateDonation)
        titleEditText = view.findViewById(R.id.etEditTitle)
        descriptionEditText = view.findViewById(R.id.etEditDescription)
        quantityEditText = view.findViewById(R.id.etEditQuantity)
        dateEditText = view.findViewById(R.id.etEditDate)
        statusEditText = view.findViewById(R.id.etEditStatus)

        // Retrieve the selected donation from the shared ViewModel
        val currentDonation: Donation? = viewModel.selectedDonation

        // Check if the currentDonation is not null before accessing its properties
        if (currentDonation != null) {
            // Pre-fill EditText fields with existing values
            titleEditText.setText(currentDonation.title)
            descriptionEditText.setText(currentDonation.description)
            quantityEditText.setText(currentDonation.quantity.toString())
            dateEditText.setText(currentDonation.date)
            statusEditText.setText(currentDonation.status)
        }

        // Handle click event for update button
        updateButton.setOnClickListener {
            // Check if the currentDonation is not null before proceeding
            if (currentDonation != null) {
                // Get updated values from EditText fields
                val updatedTitle = titleEditText.text.toString()
                val updatedDescription = descriptionEditText.text.toString()
                val updatedQuantity = quantityEditText.text.toString().toInt()
                val updatedDate = dateEditText.text.toString()
                val updatedStatus = statusEditText.text.toString()

                // Create updated donation object
                val updatedDonation = Donation(
                    id = currentDonation.id,
                    title = updatedTitle,
                    description = updatedDescription,
                    quantity = updatedQuantity,
                    date = updatedDate,
                    status = updatedStatus

                )

                // Call the API to update the donation
                val call: Call<Donation> = donationApi.updateDonation(
                    currentDonation.id,
                    updatedDonation
                )

                call.enqueue(object : Callback<Donation> {
                    override fun onResponse(
                        call: Call<Donation>,
                        response: Response<Donation>
                    ) {
                        // Handle the response as needed
                        if (response.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Donation updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Navigate back to the ListDonationsFragment
                            findNavController().navigateUp()
                        } else {
                            // Handle the case where the donation update failed
                            Toast.makeText(
                                requireContext(),
                                "Failed to update Donation. Check your network connection and try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Donation>, t: Throwable) {
                        // Handle error
                        t.printStackTrace()
                        // Show an error message to the user
                        Toast.makeText(
                            requireContext(),
                            "Failed to update Donation",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }
}
