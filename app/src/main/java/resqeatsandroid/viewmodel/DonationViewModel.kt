package resqeatsandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import resqeatsandroid.model.Donation
import resqeatsandroid.network.RetrofitDonation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DonationViewModel : ViewModel() {

    private val _donation = MutableLiveData<Donation>()
    val donation: LiveData<Donation>
        get() = _donation

    fun getDonationById(donationId: String): LiveData<Donation> {
        // Make a network request or use your data source to fetch the donation information
        val call: Call<Donation> = RetrofitDonation.donationApi.getDonationById(donationId)

        call.enqueue(object : Callback<Donation> {
            override fun onResponse(call: Call<Donation>, response: Response<Donation>) {
                if (response.isSuccessful) {
                    _donation.value = response.body()
                } else {
                    // Handle unsuccessful response
                    _donation.value = null
                }
            }

            override fun onFailure(call: Call<Donation>, t: Throwable) {
                // Handle network failure
                _donation.value = null
            }
        })

        return donation
    }
}
