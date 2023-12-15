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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import resqeatsandroid.model.Donation
import resqeatsandroid.network.RetrofitDonation.donationApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.resqeatsandroid.R
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class AddDonationFragment : Fragment() {

    private lateinit var addButton: Button
    private lateinit var quantityEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var etatEditText: EditText
    private lateinit var backButton: ImageButton

    // Replace these values with your Twilio account SID, auth token, and Twilio phone number
    private val twilioAccountSid = "ACa62721605d27320e2270fec6eb12370c"
    private val twilioAuthToken = "6ecc3063e4b6e384d2d65c84d72e2687"
    private val twilioPhoneNumber = "+12515170867"
    private val toPhoneNumber = "+21620947998"

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
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val quantite = quantiteText.toInt()
            val newDonation = Donation(id = "", quantite = quantite, date = date, etat = etat)

            // Create the donation using the Retrofit API
            val call: Call<Donation> = donationApi.createDonation(newDonation)

            call.enqueue(object : Callback<Donation> {
                override fun onResponse(call: Call<Donation>, response: Response<Donation>) {
                    val createdDonation = response.body()
                    // Handle the response as needed

                    // Check if the donation was created successfully
                    if (response.isSuccessful) {
                        // Send SMS using Twilio after creating a donation
                        sendTwilioSMS("New donation: Quantity - $quantite, Date - $date, Status - $etat")
                        Toast.makeText(
                            requireContext(),
                            "Donation created successfully ",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Navigate back to the ListDonationsFragment
                        findNavController().navigateUp()
                    } else {
                        // Handle the case where the donation creation failed
                        // You can log an error or display a message to the user
                        Toast.makeText(
                            requireContext(),
                            "Failed to create Donation. Check your network connection and try again.",
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
                        "Failed to create Donation",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }


        // Handle back button click event
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun sendTwilioSMS(message: String) {
        val twilioApiUrl =
            "https://api.twilio.com/2010-04-01/Accounts/$twilioAccountSid/Messages.json"
        val authString = "$twilioAccountSid:$twilioAuthToken"
        val authHeaderValue = "Basic " + android.util.Base64.encodeToString(
            authString.toByteArray(),
            android.util.Base64.NO_WRAP
        )
        val requestBody = "To=$toPhoneNumber&From=$twilioPhoneNumber&Body=$message"

        // Use coroutines for background work
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL(twilioApiUrl)
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Authorization", authHeaderValue)
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

                val os: OutputStream = connection.outputStream
                val input: ByteArray = requestBody.toByteArray(StandardCharsets.UTF_8)
                os.write(input, 0, input.size)

                val responseCode = connection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    activity?.runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "SMS sent successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    activity?.runOnUiThread {
                        Toast.makeText(requireContext(), "Failed to send SMS", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                connection.disconnect()

            } catch (e: Exception) {
                e.printStackTrace()
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}