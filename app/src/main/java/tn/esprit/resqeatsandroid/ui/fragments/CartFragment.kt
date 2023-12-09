package tn.esprit.resqeatsandroid.ui.fragments
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tn.esprit.resqeatsandroid.databinding.FragmentCartBinding
import tn.esprit.resqeatsandroid.model.CartItem
import tn.esprit.resqeatsandroid.network.RetrofitClient
import tn.esprit.resqeatsandroid.repository.CartRepository
import tn.esprit.resqeatsandroid.ui.activities.HomeActivity
import tn.esprit.resqeatsandroid.ui.adapters.CartItemAdapter
import tn.esprit.resqeatsandroid.viewmodel.CartViewModel
import tn.esprit.resqeatsandroid.viewmodel.CartViewModelFactory
import tn.esprit.resqeatsandroid.viewmodel.ProductViewModel

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

        displayCartItems()
    }

    /*private fun displayCartItems() {
        cartViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartItemAdapter.submitList(cartItems)
            lifecycleScope.launch {
                val totalPrice = withContext(Dispatchers.IO) {
                    cartViewModel.calculateTotalPrice()
                }
                binding.cartActivityTotalPriceTv.text = totalPrice.toString()
            }
        }
    }*/

    private fun displayCartItems() {
        // Observer for LiveData ensures that changes in the database are reflected in the UI
        cartViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartItemAdapter.submitList(cartItems)
            lifecycleScope.launch {
                val totalPrice = withContext(Dispatchers.IO) {
                    cartViewModel.calculateTotalPrice()
                }
                binding.cartActivityTotalPriceTv.text = totalPrice.toString()
            }
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



}
