package br.com.ufes.pedrotlf.pad.ui.surgery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.ufes.pedrotlf.pad.*
import br.com.ufes.pedrotlf.pad.data.dto.SurgeryPatientLesionDTO
import br.com.ufes.pedrotlf.pad.databinding.FragmentSurgeryPatientDetailsBinding

class SurgeryPatientDetailsFragment: BaseFragment() {

    private var _binding: FragmentSurgeryPatientDetailsBinding? = null
    private val binding get() = _binding!!
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

            val regionList = requireContext().getRegionList()
            val diagnosisList = requireContext().getDiagnosisList()
            val procedureList = requireContext().getProceduresList()

            fragmentSurgeryPatientDetailsRegion.setAutoCompleteOptions(requireContext(), regionList)
            fragmentSurgeryPatientDetailsDiagnosis.setAutoCompleteOptions(requireContext(), diagnosisList)
            fragmentSurgeryPatientDetailsDiagnosisSecondary.setAutoCompleteOptions(requireContext(), diagnosisList)
            fragmentSurgeryPatientDetailsProcedure.setAutoCompleteOptions(requireContext(), procedureList)

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
        }
    }
}