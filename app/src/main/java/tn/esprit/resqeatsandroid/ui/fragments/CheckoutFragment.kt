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

class CheckoutFragment : Fragment() {
    private val paymentViewModel: PaymentViewModel by activityViewModels()

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
                    showOrderSuccessAlert(response.body()?._id)
                    cartViewModel.clearCart()
                } else {
                    Toast.makeText(requireContext(), "Failed to place order", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: "Unknown error"
                Log.e("HTTP Error", errorMessage)
                Toast.makeText(requireContext(), "HTTP Error placing order: $errorMessage", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("Error placing order", e.message ?: "Unknown error")
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

}
