package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.ufes.pedrotlf.pad.data.dto.LesionDTO

class PatientLesionsAdapter(
    f: Fragment,
    private val patientId: Int,
    lesions: List<LesionDTO?>,
    private val onLesionRemovedCallback: (index: Int, lesion: LesionDTO?) -> Unit
): FragmentStateAdapter(f) {
    private val _list = lesions.map{ DynamicLesion(it) }.toMutableList()
    val list: List<LesionDTO?> get() = _list.map { it.lesion }

    override fun getItemCount(): Int = _list.size

    override fun createFragment(position: Int): Fragment {
        return PatientLesionPageFragment(patientId, _list[position].lesion, {
            onLesionRemovedCallback(position, _list[position].lesion)
        },{
            _list[position].lesion = it
        })
    }

    fun addEmptyLesion(){
        _list.add(DynamicLesion(null))
        notifyDataSetChanged()
    }

    private data class DynamicLesion(
        var lesion: LesionDTO?
    )
}