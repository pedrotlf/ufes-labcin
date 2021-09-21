package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.ufes.pedrotlf.pad.data.dto.PatientDTO
import br.com.ufes.pedrotlf.pad.databinding.ItemPatientBinding

class PatientsAdapter(private val onClickListener: OnItemClickListener) : ListAdapter<PatientDTO, PatientsAdapter.PatientViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = ItemPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class PatientViewHolder (private val binding: ItemPatientBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val patient = getItem(position)
                        onClickListener.onItemClick(patient)
                    }
                }
            }
        }

        fun bind(patient: PatientDTO){
            binding.apply {
                itemPatientNumber.text = patient.patientData.susNumber
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(patient: PatientDTO)
    }

    class DiffCallback: DiffUtil.ItemCallback<PatientDTO>(){
        override fun areItemsTheSame(oldItem: PatientDTO, newItem: PatientDTO): Boolean =
            oldItem.patientData.id == newItem.patientData.id

        override fun areContentsTheSame(oldItem: PatientDTO, newItem: PatientDTO): Boolean =
            oldItem == newItem

    }
}