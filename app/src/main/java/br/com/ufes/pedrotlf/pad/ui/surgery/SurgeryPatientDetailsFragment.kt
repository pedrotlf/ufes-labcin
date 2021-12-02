package br.com.ufes.pedrotlf.pad.ui.surgery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.ufes.pedrotlf.pad.*
import br.com.ufes.pedrotlf.pad.data.Resource
import br.com.ufes.pedrotlf.pad.data.dto.SurgeryPatientLesionDTO
import br.com.ufes.pedrotlf.pad.databinding.FragmentSurgeryPatientDetailsBinding

class SurgeryPatientDetailsFragment: BaseFragment() {

    private var _binding: FragmentSurgeryPatientDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SurgeryPatientDetailsViewModel>()
    private val args by navArgs<SurgeryPatientDetailsFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSurgeryPatientDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentSurgeryPatientDetailsName.text = args.surgeryPatient.name

            val regionList = root.context.getRegionList()
            val diagnosisList = root.context.getDiagnosisList()
            val procedureList = root.context.getProceduresList()

            fragmentSurgeryPatientDetailsRegion.setAutoCompleteOptions(regionList)
            fragmentSurgeryPatientDetailsDiagnosis.setAutoCompleteOptions(diagnosisList)
            fragmentSurgeryPatientDetailsDiagnosisSecondary.setAutoCompleteOptions(diagnosisList)
            fragmentSurgeryPatientDetailsProcedure.setAutoCompleteOptions(procedureList)

            fragmentSurgeryPatientDetailsFooterNextButton.setOnClickListener {
                val patientLesion = SurgeryPatientLesionDTO(
                    fragmentSurgeryPatientDetailsRegion.text.toString(),
                    fragmentSurgeryPatientDetailsHigherDiameter.text.toString(),
                    fragmentSurgeryPatientDetailsLowerDiameter.text.toString(),
                    fragmentSurgeryPatientDetailsDiagnosis.text.toString(),
                    fragmentSurgeryPatientDetailsDiagnosisSecondary.text.toString(),
                    fragmentSurgeryPatientDetailsProcedure.text.toString(),
                    fragmentSurgeryPatientDetailsObs.text.toString(),
                    fragmentSurgeryPatientDetailsSurgeon.text.toString()
                )

                val action = SurgeryPatientDetailsFragmentDirections.actionSurgeryPatientDetailsFragmentToSurgeryPatientPhotosFragment(
                    args.surgeryPatient,
                    patientLesion,
                    args.susNumber
                )
                findNavController().navigate(action)
            }

            viewModel.surgeonsRequest.observe(viewLifecycleOwner){
                when(it){
                    is Resource.Loading -> showLoading()
                    is Resource.Success -> {
                        dismissLoading()
                        fragmentSurgeryPatientDetailsSurgeon.setAutoCompleteOptions(it.data)
                    }
                    is Resource.Failure -> dismissLoading()
                }
            }
        }
    }
}