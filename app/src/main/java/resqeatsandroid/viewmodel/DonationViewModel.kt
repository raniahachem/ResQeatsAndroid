package resqeatsandroid.viewmodel

import androidx.lifecycle.ViewModel
import resqeatsandroid.model.Donation

class DonationViewModel : ViewModel() {
    var selectedDonation: Donation? = null
}