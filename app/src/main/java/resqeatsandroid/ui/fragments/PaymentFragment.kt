package tn.esprit.resqeatsandroid.ui.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import tn.esprit.resqeatsandroid.databinding.FragmentPaymentBinding

class PaymentFragment : Fragment() {
    private lateinit var binding: FragmentPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val paymentUrl = arguments?.getString("paymentUrl")

        if (!paymentUrl.isNullOrBlank()) {
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.webViewClient = WebViewClient()
            binding.webView.loadUrl(paymentUrl)
        } else {
            Toast.makeText(requireContext(), "Invalid payment URL", Toast.LENGTH_SHORT).show()
        }
    }
}
