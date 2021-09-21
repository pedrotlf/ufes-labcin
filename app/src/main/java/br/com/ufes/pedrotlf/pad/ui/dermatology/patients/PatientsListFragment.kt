package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.data.dto.PatientDTO
import br.com.ufes.pedrotlf.pad.databinding.FragmentDermatologyPatientsListBinding

class PatientsListFragment: BaseFragment() {

    private var _binding: FragmentDermatologyPatientsListBinding? = null
    private val binding get() = _binding!!
    private val patientsViewModel: PatientsViewModel by viewModels()

    private val adapter by lazy {
        PatientsAdapter(object : PatientsAdapter.OnItemClickListener{
            override fun onItemClick(patient: PatientDTO) {
                val action = PatientsListFragmentDirections.actionPatientsListFragmentToPatientDetailsFragment(patient)
                findNavController().navigate(action)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDermatologyPatientsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentDermatologyPatientsListList.adapter = adapter

            patientsViewModel.patients.observe(viewLifecycleOwner){
                adapter.submitList(it)
            }
        }
    }
}