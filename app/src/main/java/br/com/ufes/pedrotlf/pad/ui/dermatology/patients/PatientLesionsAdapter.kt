package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.ufes.pedrotlf.pad.data.dto.LesionDTO

class PatientLesionsAdapter(
    f: Fragment,
    private val patientId: Int,
    lesions: List<LesionDTO?>
): FragmentStateAdapter(f) {
    private val list = lesions.toMutableList()

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment = PatientLesionPageFragment(patientId, list[position])

    fun addEmptyLesion(){
        list.add(null)
        notifyDataSetChanged()
    }
}