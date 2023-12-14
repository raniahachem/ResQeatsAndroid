package resqeatsandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import resqeatsandroid.model.Donation
import resqeatsandroid.network.RetrofitDonation.donationApi
import resqeatsandroid.ui.adapters.DonationAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.resqeatsandroid.R

class ListDonationsFragment : Fragment(), DonationAdapter.OnDeleteClickListener, DonationAdapter.OnEditClickListener {

    private lateinit var donationsRecyclerView: RecyclerView
    private lateinit var btnAddDonation: View

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

    private fun fetchDonationsAndUpdateUI() {
        // Assuming you have a Retrofit instance set up
        val call: Call<List<Donation>> = donationApi.getAllDonations()

        call.enqueue(object : Callback<List<Donation>> {
            override fun onResponse(call: Call<List<Donation>>, response: Response<List<Donation>>) {
                val donations = response.body()
                donations?.let {
                    // Pass the ListDonationsFragment as an OnEditClickListener
                    val adapter = DonationAdapter(donations, this@ListDonationsFragment, this@ListDonationsFragment)
                    donationsRecyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Donation>>, t: Throwable) {
                // Handle error
            }
        })
    }

    override fun onEditClick(donation: Donation) {
        // Fetch the Donation object based on donationId from the server
        fetchDonationByIdAndUpdateUI(donation.id)
    }

    private fun fetchDonationByIdAndUpdateUI(donationId: String) {
        // Fetch the Donation by ID using the Retrofit service
        val call: Call<Donation> = donationApi.getDonationById(donationId)

        call.enqueue(object : Callback<Donation> {
            override fun onResponse(call: Call<Donation>, response: Response<Donation>) {
                val updatedDonation = response.body()
                updatedDonation?.let {
                    // Handle the fetched Donation object
                    navigateToEditDonation(updatedDonation)
                }
            }

            override fun onFailure(call: Call<Donation>, t: Throwable) {
                // Handle error
            }
        })
    }

    private fun navigateToEditDonation(updatedDonation: Donation) {
        // Navigate to the EditDonationFragment, passing the fetched Donation information
        val action = ListDonationsFragmentDirections.actionListDonationsFragmentToEditDonationFragment()
        findNavController().navigate(action)
    }

    // Implement the onDeleteClick function
    override fun onDeleteClick(donation: Donation) {
        val call: Call<Void> = donationApi.deleteDonation(donation.id)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                // Handle the response as needed
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
