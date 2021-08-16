package br.com.ufes.pedrotlf.pad.dermatology.newpatient

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.databinding.FragmentDermatologyNewPatientLesionPhotosBinding

class NewPatientLesionPhotosFragment : BaseFragment() {

    private var _binding: FragmentDermatologyNewPatientLesionPhotosBinding? = null
    private val binding get() = _binding!!
    private val newPatientViewModel: NewPatientViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDermatologyNewPatientLesionPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentDermatologyNewPatientLesionPhotosCamera.setOnClickListener {
                openCamera()
            }

            newPatientViewModel.apply {
                photosList.observe(viewLifecycleOwner){
                    if(!it.isNullOrEmpty()){
                        val imageView = ImageView(context)
                        imageView.layoutParams = ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                        imageView.id = View.generateViewId()
                        imageView.setImageBitmap(it.last())
                        fragmentDermatologyNewPatientLesionPhotosList.addView(imageView)
                        fragmentDermatologyNewPatientLesionPhotosListFlow.addView(imageView)
                    }
                }
            }
        }
    }

    private val launchActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageBitmap = data?.extras?.get("data")
                if(imageBitmap is Bitmap) {
                    newPatientViewModel.photosList.apply {
                        val photosList = value?.toMutableList() ?: mutableListOf()
                        photosList.add(imageBitmap)
                        value = photosList
                    }
                }
            }
        }

    private fun openCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            activity?.packageManager?.let { packageManager ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    launchActivity.launch(takePictureIntent)
                }
            }
        }
    }
}