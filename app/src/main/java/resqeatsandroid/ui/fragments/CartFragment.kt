package tn.esprit.resqeatsandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import tn.esprit.resqeatsandroid.R
import tn.esprit.resqeatsandroid.databinding.FragmentCartBinding
import tn.esprit.resqeatsandroid.model.CartItem
import tn.esprit.resqeatsandroid.model.Order
import tn.esprit.resqeatsandroid.network.RetrofitClient
import resqeatsandroid.repository.CartRepository
import tn.esprit.resqeatsandroid.ui.activities.HomeActivity
import tn.esprit.resqeatsandroid.ui.adapters.CartItemAdapter
import tn.esprit.resqeatsandroid.viewmodel.CartViewModel
import tn.esprit.resqeatsandroid.viewmodel.CartViewModelFactory


class CartFragment : Fragment(), CartItemAdapter.OnItemClickListener {

    private lateinit var binding: FragmentCartBinding

    private val cartViewModel: CartViewModel by activityViewModels {
        CartViewModelFactory(CartRepository((requireActivity() as HomeActivity).database.cartItemDao()))
    }

    private val cartItemAdapter = CartItemAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartItemAdapter
        }
        // Définir le gestionnaire de clics pour le bouton Checkout
        binding.cartActivityCheckoutBtn.setOnClickListener {
            handleCheckoutButtonClick()
        }

        displayCartItems()
    }

    /*private fun displayCartItems() {
        cartViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartItemAdapter.submitList(cartItems)
            updateTotalPrice(cartItems)
        }
    }*/

    private fun displayCartItems() {
        cartViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            if (cartItems.isEmpty()) {
                binding.emptyCartMessage.visibility = View.VISIBLE
                binding.cartActivityCheckoutBtn.isEnabled = false
            } else {
                binding.emptyCartMessage.visibility = View.GONE
                binding.cartActivityCheckoutBtn.isEnabled = true
            }

            cartItemAdapter.submitList(cartItems)
            updateTotalPrice(cartItems)
        }
    }

    private fun handleCheckoutButtonClick() {
        if (cartViewModel.cartItems.value.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Your cart is empty", Toast.LENGTH_SHORT).show()
        } else {
            val totalAmount = binding.cartActivityTotalPriceTv.text.toString().toDouble()
            val bundle = Bundle().apply {
                putFloat("totalAmount", totalAmount.toFloat())
            }
            findNavController().navigate(
                R.id.action_fragment_cart_to_checkout_fragment,
                bundle
            )
        }
    }


    private fun updateTotalPrice(cartItems: List<CartItem>) {
        lifecycleScope.launch {
            val totalPrice = withContext(Dispatchers.IO) {
                cartViewModel.calculateTotalPrice()
            }
            binding.cartActivityTotalPriceTv.text = totalPrice.toString()
        }
    }

    override fun onDeleteClicked(cartItem: CartItem) {
        lifecycleScope.launch {
            try {
                val success = cartViewModel.deleteCartItem(cartItem)
                if (success) {
                    Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to delete item", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("CartFragment", "Error deleting cart item", e)
                Toast.makeText(requireContext(), "Error deleting item", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onQuantityChanged(cartItem: CartItem) {
        lifecycleScope.launch {
            try {
                val success = cartViewModel.updateCartItem(cartItem)
                if (success) {
                    Toast.makeText(requireContext(), "Quantity updated", Toast.LENGTH_SHORT).show()
                    updateTotalPrice(cartItemAdapter.currentList)
                } else {
                    Toast.makeText(requireContext(), "Failed to update quantity", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("CartFragment", "Error updating cart item quantity", e)
                Toast.makeText(requireContext(), "Error updating quantity", Toast.LENGTH_SHORT).show()
            }
        }
    }



    /*private fun handleCheckoutButtonClick() {
        val totalAmount = binding.cartActivityTotalPriceTv.text.toString().toDouble()

        // Utilisez un Bundle pour transmettre le montant total à CheckoutFragment
        val bundle = Bundle().apply {
            putFloat("totalAmount", totalAmount.toFloat())
        }

        // Naviguer vers le fragment de paiement avec le montant total
        findNavController().navigate(
            R.id.action_fragment_cart_to_checkout_fragment,
            bundle
        )
    }*/


    private fun sendOrder(order: Order) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.create().createOrder(order)
                }

                if (response.isSuccessful) {
                    // La commande a été créée avec succès
                    showOrderSuccessView()
                } else {
                    // La requête a échoué
                    Toast.makeText(
                        requireContext(),
                        "Failed to create order: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: HttpException) {
                // Erreur HTTP
                Log.e("CartFragment", "HTTP Error creating order", e)
                Toast.makeText(requireContext(), "HTTP Error creating order", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                // Autre erreur
                Log.e("CartFragment", "Error creating order", e)
                Toast.makeText(requireContext(), "Error creating order", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showOrderSuccessView() {
        // Vous pouvez afficher une vue ou effectuer une action spécifique
        // lorsqu'une commande est créée avec succès.
        // Par exemple, afficher une nouvelle activité ou un message de réussite.
        // Ici, je montre la vue de confirmation que vous avez dans votre layout.
        binding.cartActivityCardView.visibility = View.VISIBLE
    }

}
