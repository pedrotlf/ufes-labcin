package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import br.com.ufes.pedrotlf.pad.*
import br.com.ufes.pedrotlf.pad.data.dto.LesionDTO
import br.com.ufes.pedrotlf.pad.databinding.FragmentDermatologyPatientLesionPageBinding
import com.bumptech.glide.Glide

class PatientLesionPageFragment(private val lesion: LesionDTO): BaseFragment() {

    private var _binding: FragmentDermatologyPatientLesionPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PatientLesionPageViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDermatologyPatientLesionPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply { viewModel.apply {

            val regionList = root.context.getRegionList()
            val diagnosisList = root.context.getDiagnosisList()
            fragmentDermatologyPatientWoundBodyRegion.apply {
                setText(lesion.lessionData.bodyRegion)
                doOnTextChanged { text, _, _, _ ->
                    bodyRegion = text?.toString() ?: ""
                }
                setAutoCompleteOptions(regionList)
            }
            fragmentDermatologyPatientWoundDiagnostic.apply {
                setText(lesion.lessionData.diagnostic)
                doOnTextChanged { text, _, _, _ ->
                    diagnostic = text?.toString() ?: ""
                }
                setAutoCompleteOptions(diagnosisList)
            }
            fragmentDermatologyPatientWoundDiagnosticSecondary.apply {
                setText(lesion.lessionData.diagnosticSecondary)
                doOnTextChanged { text, _, _, _ ->
                    diagnosticSecondary = text?.toString() ?: ""
                }
                setAutoCompleteOptions(diagnosisList)
            }
            fragmentDermatologyPatientWoundRadioGrown.setBooleanRadioGroupListener(lesion.lessionData.woundGrown){ woundGrown = it }
            fragmentDermatologyPatientWoundRadioItched.setBooleanRadioGroupListener(lesion.lessionData.woundItched){ woundItched = it }
            fragmentDermatologyPatientWoundRadioBleed.setBooleanRadioGroupListener(lesion.lessionData.woundBleed){woundBleed = it}
            fragmentDermatologyPatientWoundRadioHurt.setBooleanRadioGroupListener(lesion.lessionData.woundHurt){woundHurt = it}
            fragmentDermatologyPatientWoundRadioPatternChanged.setBooleanRadioGroupListener(lesion.lessionData.woundPatternChanged){woundPatternChanged = it}
            fragmentDermatologyPatientWoundRadioRelief.setBooleanRadioGroupListener(lesion.lessionData.woundHasRelief){woundHasRelief = it}
            
            setPictures()
        }}
    }

    private fun RadioGroup.setBooleanRadioGroupListener(value: Boolean?, valueSetter: (Boolean) -> Unit){
        setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById<View>(checkedId)
            if(radioButton is RadioButton)
                when(radioButton.text){
                    getString(R.string.word_sim) -> valueSetter(true)
                    getString(R.string.word_nao) -> valueSetter(false)
                }
        }

        if(value != null)
            children.forEach { button ->
                if(button is RadioButton){
                    if(value && button.text == getString(R.string.word_sim)) {
                        button.isChecked = true
                        return
                    }
                    if(!value && button.text == getString(R.string.word_nao)) {
                        button.isChecked = true
                        return
                    }
                }
            }
    }

    private fun FragmentDermatologyPatientLesionPageBinding.setPictures() {
        if(lesion.images.isNotEmpty()) {
            lesion.images.forEach { img ->
                context?.let { ctx ->
                    val imageView = ImageView(ctx)
                    imageView.layoutParams =
                        ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    imageView.id = View.generateViewId()
                    Glide.with(ctx).load(img.image).override(500).into(imageView)
                    fragmentDermatologyPatientDetailsLesionPhotosList.addView(imageView)
                    fragmentDermatologyPatientDetailsLesionPhotosListFlow.addView(imageView)
                }
            }
        }else{
            fragmentDermatologyPatientDetailsLesionPhotosList.isVisible = false
        }
    }
}