package br.com.ufes.pedrotlf.pad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.ufes.pedrotlf.pad.databinding.FragmentPadHomeBinding

class PadHomeFragment: Fragment() {

    private var _binding: FragmentPadHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPadHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}