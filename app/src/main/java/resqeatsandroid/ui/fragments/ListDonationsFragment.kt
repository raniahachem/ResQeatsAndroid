package resqeatsandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import resqeatsandroid.model.Donation
import resqeatsandroid.network.RetrofitDonation.donationApi
import resqeatsandroid.ui.adapters.DonationAdapter
import resqeatsandroid.viewmodel.DonationViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.resqeatsandroid.R

class ListDonationsFragment : Fragment(), DonationAdapter.OnDeleteClickListener, DonationAdapter.OnEditClickListener {

    private lateinit var donationsRecyclerView: RecyclerView
    private lateinit var btnAddDonation: View
    private val viewModel: DonationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_donations, container, false)
        donationsRecyclerView = view.findViewById(R.id.donationsRecyclerView)
        btnAddDonation = view.findViewById(R.id.btnAddDonation)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        donationsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Fetch donations and update UI
        fetchDonationsAndUpdateUI()
        // Set up click listener for btnAddDonation
        btnAddDonation.setOnClickListener {
            // Use Navigation Component to navigate to AddDonationFragment
            findNavController().navigate(R.id.action_listDonationsFragment_to_addDonationFragment)
        }
    }


    private fun onDonationItemClick(donation: Donation) {
        // Set the selected donation in the shared ViewModel
        viewModel.selectedDonation = donation

        // Navigate to DonationDetailFragment
        findNavController().navigate(R.id.action_listDonationsFragment_to_donationDetailFragment)
    }

    private fun fetchDonationsAndUpdateUI() {
        // Assuming you have a Retrofit instance set up
        val call: Call<List<Donation>> = donationApi.getAllDonations()

        call.enqueue(object : Callback<List<Donation>> {
            override fun onResponse(call: Call<List<Donation>>, response: Response<List<Donation>>) {
                val donations = response.body()
                donations?.let {
                    // Sort the donations based on quantity
                    val sortedDonations = donations.sortedBy { it.quantity }

                    // Set up RecyclerView with onItemClick listener
                    val adapter = DonationAdapter(sortedDonations, this@ListDonationsFragment, this@ListDonationsFragment) { donation ->
                        onDonationItemClick(donation)
                    }
                    donationsRecyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Donation>>, t: Throwable) {
                // Handle error
            }
        })
    }

    // Implement the onEditClick function
    override fun onEditClick(donation: Donation) {
        // Set the selected donation in the shared ViewModel
        viewModel.selectedDonation = donation

        // Navigate to EditDonationFragment
        findNavController().navigate(R.id.action_listDonationsFragment_to_editDonationFragment)
    }

    // Implement the onDeleteClick function
    override fun onDeleteClick(donation: Donation) {
        val call: Call<Void> = donationApi.deleteDonation(donation.id)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                // Handle the response as needed
                Toast.makeText(
                    requireContext(),
                    "Donation deleted successfully",
                    Toast.LENGTH_SHORT
                ).show()
                // For example, update the UI by refreshing the donation list
                refreshDonationList()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle error
                t.printStackTrace()
                // Show an error message to the user
            }
        })
    }

    // Add a function to refresh the donation list after deletion
    private fun refreshDonationList() {
        fetchDonationsAndUpdateUI()
    }
}
