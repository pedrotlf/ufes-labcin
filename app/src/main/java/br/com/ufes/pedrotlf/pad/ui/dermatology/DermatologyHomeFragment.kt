package br.com.ufes.pedrotlf.pad.ui.dermatology

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.databinding.FragmentDermatologyHomeBinding

class DermatologyHomeFragment : BaseFragment() {

    private var _binding: FragmentDermatologyHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDermatologyHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentDermatologyHomeFooterChangeFunctionButton.setOnClickListener {
                findNavController().popBackStack()
            }

            fragmentDermatologyHomePatientAddButton.setOnClickListener {
                val action = DermatologyHomeFragmentDirections.actionDermatologyHomeFragmentToNewPatientDataFragment()
                findNavController().navigate(action)
            }

            fragmentDermatologyHomeFooterPatientListButton.setOnClickListener {
                val action = DermatologyHomeFragmentDirections.actionDermatologyHomeFragmentToPatientsListFragment()
                findNavController().navigate(action)
            }
        }
    }
}