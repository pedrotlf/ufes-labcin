package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.databinding.FragmentDermatologyPatientDetailsBinding
import com.bumptech.glide.Glide

class PatientDetailsFragment: BaseFragment() {

    private var _binding: FragmentDermatologyPatientDetailsBinding? = null
    private val binding get() = _binding!!
    private val patientViewModel: PatientDetailsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDermatologyPatientDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            patientViewModel.patient.observe(viewLifecycleOwner) { pat ->
                pat.lesions.forEach { lesion ->
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
                }
            }
        }
    }
}