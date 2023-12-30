package tn.esprit.resqeatsandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tn.esprit.resqeatsandroid.databinding.FragmentOrderSupplierBinding

class OrderSupplierFragment : Fragment() {
        private lateinit var binding: FragmentOrderSupplierBinding
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentOrderSupplierBinding.inflate(inflater, container, false)
            return binding.root
        }
}