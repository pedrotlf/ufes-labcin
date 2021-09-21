package br.com.ufes.pedrotlf.pad.dermatology.newpatient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.R
import br.com.ufes.pedrotlf.pad.databinding.FragmentDermatologyNewPatientLesionBinding

class NewPatientLesionFragment : BaseFragment() {

    private var _binding: FragmentDermatologyNewPatientLesionBinding? = null
    private val binding get() = _binding!!
    private val newPatientViewModel: NewPatientViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDermatologyNewPatientLesionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply { newPatientViewModel.apply {
            fragmentDermatologyNewPatientWoundBodyRegion.doOnTextChanged { text, _, _, _ ->
                bodyRegion.value = text?.toString()
            }
            fragmentDermatologyNewPatientWoundDiagnostic.doOnTextChanged { text, _, _, _ ->
                diagnostic.value = text?.toString()
            }
            fragmentDermatologyNewPatientWoundDiagnosticSecondary.doOnTextChanged { text, _, _, _ ->
                diagnosticSecondary.value = text?.toString()
            }
            fragmentDermatologyNewPatientWoundRadioGrown.setBooleanRadioGroupListener(woundGrown)
            fragmentDermatologyNewPatientWoundRadioItched.setBooleanRadioGroupListener(woundItched)
            fragmentDermatologyNewPatientWoundRadioBleed.setBooleanRadioGroupListener(woundBleed)
            fragmentDermatologyNewPatientWoundRadioHurt.setBooleanRadioGroupListener(woundHurt)
            fragmentDermatologyNewPatientWoundRadioPatternChanged.setBooleanRadioGroupListener(woundPatternChanged)
            fragmentDermatologyNewPatientWoundRadioRelief.setBooleanRadioGroupListener(woundHasRelief)

            fragmentDermatologyNewPatientWoundFooterNextButton.setOnClickListener {
                val action = NewPatientLesionFragmentDirections.actionNewPatientWoundFragmentToNewPatientLesionPhotosFragment()
                findNavController().navigate(action)
            }
        }}
    }

    private fun RadioGroup.setBooleanRadioGroupListener(mutableLiveData: MutableLiveData<Boolean?>){
        setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById<View>(checkedId)
            if(radioButton is RadioButton)
                when(radioButton.text){
                    getString(R.string.word_sim) -> mutableLiveData.value = true
                    getString(R.string.word_nao) -> mutableLiveData.value = false
                }
        }

        val currentValue = mutableLiveData.value
        if(currentValue != null)
            children.forEach { button ->
                if(button is RadioButton){
                    if(currentValue && button.text == getString(R.string.word_sim)) {
                        button.isChecked = true
                        return
                    }
                    if(!currentValue && button.text == getString(R.string.word_nao)) {
                        button.isChecked = true
                        return
                    }
                }
            }
    }
}