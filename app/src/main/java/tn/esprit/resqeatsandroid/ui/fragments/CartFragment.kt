package tn.esprit.resqeatsandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tn.esprit.resqeatsandroid.database.AppDatabase
import tn.esprit.resqeatsandroid.databinding.FragmentCartBinding
import tn.esprit.resqeatsandroid.ui.activities.HomeActivity
import tn.esprit.resqeatsandroid.ui.adapters.CartItemAdapter

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val database: AppDatabase by lazy {
        (requireActivity() as HomeActivity).database
    }
    private val cartItemAdapter = CartItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView with the CartItemAdapter
        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartItemAdapter
        }

        // Call the function to display cart items
        displayCartItems()
    }

    private fun displayCartItems() {
        CoroutineScope(Dispatchers.IO).launch {
            val cartItems = database.cartItemDao().getAllCartItems()

            // Update UI or adapter with the list of cart items
            withContext(Dispatchers.Main) {
                cartItemAdapter.submitList(cartItems)
            }
        }
    }
}
