package tn.esprit.resqeatsandroid.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import tn.esprit.resqeatsandroid.R
import tn.esprit.resqeatsandroid.databinding.FragmentCheckoutBinding
import tn.esprit.resqeatsandroid.model.CartItem
import tn.esprit.resqeatsandroid.model.Order
import tn.esprit.resqeatsandroid.model.OrderItem
import tn.esprit.resqeatsandroid.model.PaymentRequest
import tn.esprit.resqeatsandroid.model.PaymentResponse
import tn.esprit.resqeatsandroid.model.Product
import tn.esprit.resqeatsandroid.network.RetrofitClient
import tn.esprit.resqeatsandroid.repository.CartRepository
import tn.esprit.resqeatsandroid.ui.activities.HomeActivity
import tn.esprit.resqeatsandroid.ui.fragments.CheckoutFragmentDirections
import tn.esprit.resqeatsandroid.viewmodel.CartViewModel
import tn.esprit.resqeatsandroid.viewmodel.CartViewModelFactory
import tn.esprit.resqeatsandroid.viewmodel.PaymentViewModel
import retrofit2.Response
import tn.esprit.resqeatsandroid.model.EmailRequest
import java.io.IOException

class CheckoutFragment : Fragment() {
    private val paymentViewModel: PaymentViewModel by activityViewModels()
    private var orderId: String? = null
    private lateinit var binding: FragmentCheckoutBinding
    private val cartViewModel: CartViewModel by activityViewModels {
        CartViewModelFactory(CartRepository((requireActivity() as HomeActivity).database.cartItemDao()))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val totalAmount = arguments?.getFloat("totalAmount") ?: 0.0f
        binding.checkoutTotalAmountTv.text = "Total Amount: $totalAmount TND"

        binding.payNowButton.setOnClickListener {
            initiatePayment(totalAmount.toDouble())
        }

        binding.payOnDeliveryButton.setOnClickListener {
            lifecycleScope.launch {
                val cartItems: List<CartItem>? = cartViewModel.cartItems.value
                val orderItems = cartItems?.map {
                    OrderItem(
                        product = Product(
                            title = it.productName,
                            category = it.productCategory,
                            description = "",
                            price = it.productPrice,
                            image = it.productImage,
                            quantity = it.quantity,
                            restaurant = null,
                            _id = null
                        ), quantity = it.quantity
                    )
                } ?: emptyList()

                val order = Order(
                    status = "pending",
                    items = orderItems,
                    totalAmount = totalAmount.toDouble(),
                    client = "65594efffb8b75c44f353fb7"
                )

                sendOrder(order)
            }
        }
    }

    private fun sendOrder(order: Order) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.create().createOrder(order)

                if (response.isSuccessful) {
                    val orderId = response.body()?._id

                    // Récupérer l'e-mail du client en utilisant l'API REST de manière asynchrone
                    try {
                        val emailResponse = RetrofitClient.create().getEmailById("65594efffb8b75c44f353fb7")
                        if (emailResponse.isSuccessful) {
                            val clientEmail = emailResponse.body()?.email

                            // Envoyer un e-mail avec le bon orderId
                            sendEmail(clientEmail, orderId)

                            // Afficher une alerte pour indiquer que la commande a été passée avec succès
                            showOrderSuccessAlert(orderId)

                            // Effacer le panier après avoir placé la commande
                            cartViewModel.clearCart()
                        } else {
                            // Gérez la réponse en erreur pour getEmailById
                            Log.e("Get Email", "Failed to get client email")
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        // Gérez les erreurs IO ici
                        Log.e("IO Exception", "Error executing getEmailById")
                    }
                } else {
                    // Gérez la réponse en erreur pour createOrder
                    Toast.makeText(requireContext(), "Failed to place order", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: "Unknown error"
                Log.e("HTTP Error", errorMessage)
                Toast.makeText(
                    requireContext(),
                    "HTTP Error placing order: $errorMessage",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Log.e("Error placing order", "Exception message: ${e.message}")
                Log.e("Error placing order", "Exception localized message: ${e.localizedMessage}")
                Log.e("Error placing order", "Exception cause: ${e.cause}")
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error placing order", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showOrderSuccessAlert(orderId: String?) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Order Placed Successfully")
        alertDialog.setMessage("Your order has been placed successfully. Order ID: $orderId")
        alertDialog.setPositiveButton("Got It") { _, _ ->
            findNavController().popBackStack()
        }
        alertDialog.setOnCancelListener {
            // Additional handling if needed
        }
        alertDialog.show()
    }

    private fun initiatePayment(amount: Double) {
        lifecycleScope.launch {
            try {
                // Convert the amount to String before creating PaymentRequest
                val paymentRequest = PaymentRequest(amount.toString())

                // Use withContext to switch to the IO dispatcher for network operations
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.create().initiatePayment(paymentRequest).execute()
                }

                if (response.isSuccessful) {
                    val paymentResponse = response.body()

                    if (paymentResponse != null && paymentResponse.result.success) {
                        val paymentFragment = PaymentFragment()
                        val bundle = Bundle().apply {
                            putString("paymentUrl", paymentResponse.result.link)
                        }
                        paymentFragment.arguments = bundle

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_content_main, paymentFragment)
                            .addToBackStack(null)
                            .commit()
                    } else {
                        Toast.makeText(requireContext(), "Invalid payment response", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to initiate payment", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("Payment Initiation Error", Log.getStackTraceString(e))

                // Ajoutez ces lignes pour imprimer le détail de l'exception
                Log.e("Payment Initiation Error", "Exception message: ${e.message}")
                Log.e("Payment Initiation Error", "Exception localized message: ${e.localizedMessage}")
                Log.e("Payment Initiation Error", "Exception cause: ${e.cause}")

                Toast.makeText(requireContext(), "Error initiating payment", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun sendEmail(clientEmail: String?, orderId: String?) {
        try {
            val emailSubject = "Thank You for Your Order!"
            val emailMessage = "Thank you for placing your order with our application. This is your order ID: $orderId. Please make sure to show it to your restaurant to verify the order."

            val emailRequest = EmailRequest(
                to = clientEmail ?: "",
                subject = emailSubject,
                text = emailMessage
            )

            val emailResponse = RetrofitClient.create().sendEmail(emailRequest)

            if (emailResponse.isSuccessful) {
                // Gérer le succès de l'envoi de l'e-mail
                Log.d("Send Email", "Email sent successfully")
            } else {
                // Gérez la réponse en erreur pour sendEmail
                Log.e("Send Email", "Failed to send email. Response code: ${emailResponse.code()}")
                Log.e("Send Email", "Response body: ${emailResponse.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            // Gérer les erreurs ici
            e.printStackTrace()
            Log.e("Send Email", "Error sending email. Exception message: ${e.message}")
        }
    }


}