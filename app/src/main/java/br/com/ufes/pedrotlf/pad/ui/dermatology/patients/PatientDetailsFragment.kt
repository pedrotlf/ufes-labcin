package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.databinding.FragmentDermatologyPatientDetailsBinding
import br.com.ufes.pedrotlf.pad.getCitiesList
import br.com.ufes.pedrotlf.pad.setAutoCompleteOptions
import com.redmadrobot.inputmask.MaskedTextChangedListener

class PatientDetailsFragment: BaseFragment() {

    private var _binding: FragmentDermatologyPatientDetailsBinding? = null
    private val binding get() = _binding!!
    private val patientViewModel: PatientDetailsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDermatologyPatientDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            setPatientInfos()

            patientViewModel.patient?.lesions?.let {
                fragmentDermatologyPatientDetailsLesionsList.adapter = PatientLesionsAdapter(
                    this@PatientDetailsFragment,
                    it
                )
            } ?: kotlin.run { fragmentDermatologyPatientDetailsLesionsList.isVisible = false }

            fragmentDermatologyPatientDetailsUpdate.setOnClickListener {
                patientViewModel.updatePatient()
            }

            patientViewModel.patientUpdated.observe(viewLifecycleOwner){
                if(it != 0){
                    Toast.makeText(root.context, "Dados do paciente atualizados!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun FragmentDermatologyPatientDetailsBinding.setPatientInfos(){
        fragmentDermatologyPatientDetailsSusNumber.apply {
            setText(patientViewModel.susNumber)
            MaskedTextChangedListener.Companion.installOn(
                this,
                "[000]-[0000]-[0000]-[0000]",
                object : MaskedTextChangedListener.ValueListener {
                    override fun onTextChanged(maskFilled: Boolean, extractedValue: String, formattedValue: String) {
                        patientViewModel.susNumber = formattedValue
                    }
                }
            )
        }

        fragmentDermatologyPatientDetailsAge.apply {
            setText(patientViewModel.age?.toString())
            doOnTextChanged { text, _, _, _ ->
                patientViewModel.age = text?.toString()?.toIntOrNull()
            }
        }

        val citiesList = root.context.getCitiesList()
        fragmentDermatologyPatientDetailsServiceCity.apply {
            setText(patientViewModel.serviceCity)
            doOnTextChanged { text, _, _, _ ->
                patientViewModel.serviceCity = text?.toString()
            }
            setAutoCompleteOptions(citiesList)
        }
        fragmentDermatologyPatientDetailsLivingCity.apply {
            setText(patientViewModel.livingCity)
            doOnTextChanged { text, _, _, _ ->
                patientViewModel.livingCity = text?.toString()
            }
            setAutoCompleteOptions(citiesList)
        }
    }
}