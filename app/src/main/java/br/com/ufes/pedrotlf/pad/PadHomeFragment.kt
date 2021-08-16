package br.com.ufes.pedrotlf.pad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.ufes.pedrotlf.pad.databinding.FragmentPadHomeBinding

class PadHomeFragment: BaseFragment() {

    private var _binding: FragmentPadHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPadHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentHomeButtonCirurgia.setOnClickListener {
                val action = PadHomeFragmentDirections.actionPadHomeFragmentToSurgeryHomeFragment()
                findNavController().navigate(action)
            }

            fragmentHomeButtonDermatologia.setOnClickListener {
                val action = PadHomeFragmentDirections.actionPadHomeFragmentToDermatologyHomeFragment()
                findNavController().navigate(action)
            }
        }
    }
}