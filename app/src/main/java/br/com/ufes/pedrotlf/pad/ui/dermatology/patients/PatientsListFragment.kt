package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.R
import br.com.ufes.pedrotlf.pad.data.dto.PatientDTO
import br.com.ufes.pedrotlf.pad.databinding.FragmentDermatologyPatientsListBinding
import br.com.ufes.pedrotlf.pad.databinding.FragmentDialogServerSettingsChangeBinding

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

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val patient = adapter.currentList[viewHolder.adapterPosition]
                    context?.showDeletePatientDialog(patient)
                }
            }).attachToRecyclerView(fragmentDermatologyPatientsListList)

            patientsViewModel.patients.observe(viewLifecycleOwner){
                adapter.submitList(it)
            }
        }
    }

    private fun Context.showDeletePatientDialog(patient: PatientDTO){
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.dialog_delete_patient_confirmation, patient.patientData.susNumber))
            .setPositiveButton(R.string.word_confirm) { dialog, _ ->
                patientsViewModel.launchDeletePatient(patient)
                dialog.dismiss()
            }.setNegativeButton(R.string.word_cancel){ dialog, _ ->
                dialog.cancel()
            }.setOnCancelListener {
                val position = adapter.currentList.indexOf(patient)
                if(position >= 0) adapter.notifyItemChanged(position)
            }
            .create()
            .show()
    }
}