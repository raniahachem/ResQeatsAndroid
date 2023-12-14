package resqeatsandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import resqeatsandroid.model.Donation
import resqeatsandroid.network.RetrofitDonation.donationApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.resqeatsandroid.R

class AddDonationFragment : Fragment() {

    private lateinit var addButton: Button
    private lateinit var quantityEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var etatEditText: EditText
    private lateinit var backButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_donation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        addButton = view.findViewById(R.id.addButton)
        quantityEditText = view.findViewById(R.id.quantityEditText)
        dateEditText = view.findViewById(R.id.dateEditText)
        etatEditText = view.findViewById(R.id.etatEditText)
        backButton = view.findViewById(R.id.backButton)

        // Assuming you have a Retrofit instance set up
        addButton.setOnClickListener {
            val quantiteText = quantityEditText.text.toString()
            val date = dateEditText.text.toString()
            val etat = etatEditText.text.toString()

            // Check if fields are empty
            if (quantiteText.isEmpty() || date.isEmpty() || etat.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val quantite = quantiteText.toInt()
            val newDonation = Donation(id = "", quantite = quantite, date = date, etat = etat)

            val call: Call<Donation> = donationApi.createDonation(newDonation)

            call.enqueue(object : Callback<Donation> {
                override fun onResponse(call: Call<Donation>, response: Response<Donation>) {
                    val createdDonation = response.body()
                    // Handle the response as needed

                    // Navigate back to the ListDonationsFragment
                    findNavController().navigateUp()
                }

                override fun onFailure(call: Call<Donation>, t: Throwable) {
                    // Handle error
                    t.printStackTrace()
                    // Show an error message to the user
                    Toast.makeText(requireContext(), "Failed to create Donation", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Handle back button click event
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
