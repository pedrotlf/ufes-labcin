package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.ufes.pedrotlf.pad.data.dto.LesionDTO

class PatientLesionsAdapter(f: Fragment, private val lesionsList: List<LesionDTO>): FragmentStateAdapter(f) {

    override fun getItemCount(): Int = lesionsList.size

    override fun createFragment(position: Int): Fragment = PatientLesionPageFragment(lesionsList[position])
}