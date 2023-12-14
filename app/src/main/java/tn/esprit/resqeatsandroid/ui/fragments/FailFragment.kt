package tn.esprit.resqeatsandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tn.esprit.resqeatsandroid.databinding.FragmentFailBinding

class FailFragment : Fragment() {
    private lateinit var binding: FragmentFailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFailBinding.inflate(inflater, container, false)
        return binding.root
    }
}
