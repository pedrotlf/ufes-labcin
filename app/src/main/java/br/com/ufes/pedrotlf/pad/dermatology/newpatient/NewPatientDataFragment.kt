package br.com.ufes.pedrotlf.pad.dermatology.newpatient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.databinding.FragmentDermatologyNewPatientDataBinding

class NewPatientDataFragment: BaseFragment() {

    private var _binding: FragmentDermatologyNewPatientDataBinding? = null
    private val binding get() = _binding!!
    private val newPatientViewModel: NewPatientViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDermatologyNewPatientDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentDermatologyNewPatientDataSusNumber.doOnTextChanged { text, _, _, _ ->
                newPatientViewModel.susNumber.value = text?.toString()
            }
            fragmentDermatologyNewPatientDataAge.doOnTextChanged { text, _, _, _ ->
                newPatientViewModel.age.value = text?.toString()?.toIntOrNull()
            }
            fragmentDermatologyNewPatientDataServiceCity.doOnTextChanged { text, _, _, _ ->
                newPatientViewModel.serviceCity.value = text?.toString()
            }
            fragmentDermatologyNewPatientDataLivingCity.doOnTextChanged { text, _, _, _ ->
                newPatientViewModel.livingCity.value = text?.toString()
            }

            fragmentDermatologyNewPatientDataFooterNextButton.setOnClickListener {
                val action = NewPatientDataFragmentDirections.actionNewPatientDataFragmentToNewPatientWoundFragment()
                findNavController().navigate(action)
            }
        }
    }
}