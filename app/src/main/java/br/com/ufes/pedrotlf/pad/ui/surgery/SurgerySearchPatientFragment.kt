package br.com.ufes.pedrotlf.pad.ui.surgery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.data.Resource
import br.com.ufes.pedrotlf.pad.databinding.FragmentSurgerySearchPatientBinding

class SurgerySearchPatientFragment: BaseFragment() {

    private var _binding: FragmentSurgerySearchPatientBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModels<SurgerySearchPatientViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSurgerySearchPatientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentSurgerySearchPatientButton.setOnClickListener {
                val susNumber = fragmentSurgerySearchPatientSusNumber.text.toString()
                if (susNumber.isNotBlank()) searchViewModel.searchPatient(susNumber)
            }
        }

        searchViewModel.patientRequest.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    dismissLoading()
                    it.data
                    //send data
                }
                is Resource.Failure -> {
                    dismissLoading()
                    Toast.makeText(context, "O paciente não foi encontrado", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}