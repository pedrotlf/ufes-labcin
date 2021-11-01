package br.com.ufes.pedrotlf.pad.ui.surgery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.databinding.FragmentSurgeryPatientDetailsBinding

class SurgeryPatientDetailsFragment: BaseFragment() {

    private var _binding: FragmentSurgeryPatientDetailsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<SurgeryPatientDetailsFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSurgeryPatientDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentSurgeryPatientDetailsName.text = args.surgeryPatient.name
        }
    }
}