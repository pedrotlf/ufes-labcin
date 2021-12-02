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
import br.com.ufes.pedrotlf.pad.data.dto.LesionDTO
import br.com.ufes.pedrotlf.pad.databinding.FragmentDermatologyPatientDetailsBinding
import br.com.ufes.pedrotlf.pad.getCitiesList
import br.com.ufes.pedrotlf.pad.setAutoCompleteOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.redmadrobot.inputmask.MaskedTextChangedListener

class PatientDetailsFragment: BaseFragment() {

    private var _binding: FragmentDermatologyPatientDetailsBinding? = null
    private val binding get() = _binding!!
    private val patientViewModel: PatientDetailsViewModel by viewModels()
    private var adapter: PatientLesionsAdapter? = null

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
            patientViewModel.patient?.let { patient ->
                setAdapter(patient.patientData.id, patient.lesions)
            }
            setPatientInfos()

            fragmentDermatologyPatientDetailsUpdate.setOnClickListener {
                patientViewModel.updatePatient()
            }

            fragmentDermatologyPatientDetailsAddLesion.setOnClickListener {
                adapter?.addEmptyLesion()
                fragmentDermatologyPatientDetailsLesionsList.post {
                    val current = fragmentDermatologyPatientDetailsLesionsList.currentItem
                    fragmentDermatologyPatientDetailsLesionsList.currentItem = current + 1
                }
            }

            patientViewModel.patientUpdated.observe(viewLifecycleOwner){
                if(it != 0){
                    Toast.makeText(root.context, "Dados do paciente atualizados!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun FragmentDermatologyPatientDetailsBinding.setAdapter(patientId: Int, list: List<LesionDTO?>) {
        val finalList = if(list.isNotEmpty()) list else listOf(null)
        adapter = PatientLesionsAdapter(
                this@PatientDetailsFragment,
                patientId,
                finalList
        ) { i, lesionRemoved ->
            val newList: MutableList<LesionDTO?> =
                adapter?.list?.toMutableList() ?: mutableListOf()
            newList.removeAt(i)
            setAdapter(patientId, newList)
        }
        fragmentDermatologyPatientDetailsLesionsList.adapter = adapter

        TabLayoutMediator(
            fragmentDermatologyPatientDetailsLesionsTab,
            fragmentDermatologyPatientDetailsLesionsList
        ) { tab, position ->
            tab.text = (position + 1).toString()
        }.attach()
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