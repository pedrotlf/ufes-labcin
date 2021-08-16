package br.com.ufes.pedrotlf.pad.surgery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.databinding.FragmentSurgeryHomeBinding

class SurgeryHomeFragment : BaseFragment(){

    private var _binding: FragmentSurgeryHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSurgeryHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentSurgeryHomeFooterButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}