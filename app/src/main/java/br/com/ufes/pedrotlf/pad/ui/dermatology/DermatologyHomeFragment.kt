package br.com.ufes.pedrotlf.pad.ui.dermatology

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.data.Resource
import br.com.ufes.pedrotlf.pad.databinding.FragmentDermatologyHomeBinding
import br.com.ufes.pedrotlf.pad.ui.dermatology.patients.PatientsViewModel

class DermatologyHomeFragment : BaseFragment() {

    private var _binding: FragmentDermatologyHomeBinding? = null
    private val binding get() = _binding!!
    private val patientsViewModel by viewModels<PatientsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDermatologyHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            configureView()

            observeRequest()
        }
    }

    private fun FragmentDermatologyHomeBinding.configureView() {
        fragmentDermatologyHomeFooterChangeFunctionButton.setOnClickListener {
            findNavController().popBackStack()
        }

        fragmentDermatologyHomePatientAddButton.setOnClickListener {
            val action =
                DermatologyHomeFragmentDirections.actionDermatologyHomeFragmentToNewPatientDataFragment()
            findNavController().navigate(action)
        }

        fragmentDermatologyHomeFooterPatientListButton.setOnClickListener {
            val action =
                DermatologyHomeFragmentDirections.actionDermatologyHomeFragmentToPatientsListFragment()
            findNavController().navigate(action)
        }

        fragmentDermatologyHomeFooterSendPatientsButton.setOnClickListener {
            patientsViewModel.sendPatientsToServer()
        }
    }

    private fun observeRequest() {
        patientsViewModel.sendPatientRequest.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    dismissLoading()
                    Toast.makeText(context, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                }
                is Resource.Failure -> {
                    dismissLoading()
                    Toast.makeText(context, "Houve algum problema com o envio", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        patientsViewModel.patients.observe(viewLifecycleOwner){
            // forcing patient list to be requested
        }
    }
}