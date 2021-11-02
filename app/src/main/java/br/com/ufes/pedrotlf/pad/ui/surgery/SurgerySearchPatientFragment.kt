package br.com.ufes.pedrotlf.pad.ui.surgery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.R
import br.com.ufes.pedrotlf.pad.data.Resource
import br.com.ufes.pedrotlf.pad.data.dto.SurgeryPatientDTO
import br.com.ufes.pedrotlf.pad.databinding.FragmentDialogSurgeryPatientDetailsBinding
import br.com.ufes.pedrotlf.pad.databinding.FragmentSurgerySearchPatientBinding
import com.redmadrobot.inputmask.MaskedTextChangedListener

class SurgerySearchPatientFragment: BaseFragment() {

    private var _binding: FragmentSurgerySearchPatientBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModels<SurgerySearchPatientViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSurgerySearchPatientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            applyMask()

            fragmentSurgerySearchPatientButton.setOnClickListener {
                val susNumber = fragmentSurgerySearchPatientSusNumber.text.toString()
                if (susNumber.isNotBlank()) searchViewModel.apply {
                    this.susNumber.value = susNumber
                    searchPatient(susNumber)
                }
            }
        }

        observeSearchRequest()
    }

    private fun FragmentSurgerySearchPatientBinding.applyMask(){
        MaskedTextChangedListener.Companion.installOn(fragmentSurgerySearchPatientSusNumber, "[000]-[0000]-[0000]-[0000]")
    }

    private fun observeSearchRequest() {
        searchViewModel.patientRequest.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    dismissLoading()
                    context?.showDialog(it.data)
                }
                is Resource.Failure -> {
                    dismissLoading()
                    Toast.makeText(context, "O paciente não foi encontrado", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun Context.showDialog(patient: SurgeryPatientDTO){
        val builder = AlertDialog.Builder(this)

        val inflater = layoutInflater
        val binding = FragmentDialogSurgeryPatientDetailsBinding.inflate(inflater)

        binding.apply {
            dialogSurgeryPatientDetailsName.text = patient.name
            dialogSurgeryPatientDetailsMedicalRecord.text = patient.medicalRecord
            dialogSurgeryPatientDetailsAllergy.text = patient.allergy
            dialogSurgeryPatientDetailsDiabetes.text = patient.diabetes
            dialogSurgeryPatientDetailsAnticoagulant.text = patient.anticoagulant
            dialogSurgeryPatientDetailsHas.text = patient.has
            dialogSurgeryPatientDetailsSystolicPressure.text = patient.systolicPressure.toString()
            dialogSurgeryPatientDetailsDiastolicPressure.text = patient.diastolicPressure.toString()
            dialogSurgeryPatientDetailsLesionsNumber.text = patient.numLesions.toString()
        }

        builder.setView(binding.root).setPositiveButton(R.string.word_continue) { dialog, _ ->
            val number = searchViewModel.susNumber.value ?: ""
            val action = SurgerySearchPatientFragmentDirections.actionSurgerySearchPatientFragmentToSurgeryPatientDetailsFragment(
                patient,
                number
            )
            findNavController().navigate(action)
            dialog.dismiss()
        }

        builder.create().show()
    }
}