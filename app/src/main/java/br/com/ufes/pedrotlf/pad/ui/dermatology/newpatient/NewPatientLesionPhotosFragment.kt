package br.com.ufes.pedrotlf.pad.ui.dermatology.newpatient

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.databinding.FragmentDermatologyNewPatientLesionPhotosBinding
import com.bumptech.glide.Glide
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class NewPatientLesionPhotosFragment : BaseFragment() {

    private var _binding: FragmentDermatologyNewPatientLesionPhotosBinding? = null
    private val binding get() = _binding!!
    private val newPatientViewModel: NewPatientViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDermatologyNewPatientLesionPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentDermatologyNewPatientLesionPhotosCamera.setOnClickListener {
                openCamera()
            }

            fragmentDermatologyNewPatientLesionPhotosFooterConfirmButton.setOnClickListener {
                newPatientViewModel.savePatientData()
                val action = NewPatientLesionPhotosFragmentDirections.actionBackToDermatologyHomeFragment()
                findNavController().navigate(action)
            }

            observeFilesList()
            loadSavedList()
        }
    }

    private fun FragmentDermatologyNewPatientLesionPhotosBinding.loadSavedList(){
        newPatientViewModel.imagePathList.value?.forEach { addImagePreview(it) }
    }

    private fun FragmentDermatologyNewPatientLesionPhotosBinding.observeFilesList() {
        newPatientViewModel.apply {
            imagePathList.observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty() && it.last() == currentImagePath.value) {
                    addImagePreview(it.last())
                    currentImagePath.value = null
                }
            }
        }
    }

    private fun FragmentDermatologyNewPatientLesionPhotosBinding.addImagePreview(imgPath: String) {
        context?.let { ctx ->
            val imageView = ImageView(ctx)
            imageView.layoutParams =
                ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            imageView.id = View.generateViewId()
            Glide.with(ctx).load(imgPath).override(300).into(imageView)
            fragmentDermatologyNewPatientLesionPhotosList.addView(imageView)
            fragmentDermatologyNewPatientLesionPhotosListFlow.addView(imageView)
        }
    }

    private fun openCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            activity?.packageManager?.let { packageManager ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        Toast.makeText(
                            context,
                            "Erro ao tentar gerar um arquivo para a foto",
                            Toast.LENGTH_SHORT).show()
                        null
                    }

                    // Continue only if the File was successfully created
                    context?.also { ctx -> photoFile?.also { file ->
                        val photoURI: Uri = FileProvider.getUriForFile(
                            ctx,
                            "br.com.ufes.pedrotlf.pad.fileprovider",
                            file)
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        cameraActivityLauncher.launch(takePictureIntent)
                    }}
                }
            }
        }
    }

    private val cameraActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                newPatientViewModel.confirmImagePath()
            } else {
                context?.let { ctx ->
                    newPatientViewModel.currentImagePath.value?.also{
                        val file = File(it)
                        if(file.delete())
                            Toast.makeText(ctx, "Arquivo de imagem cancelado", Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(ctx, "Não foi possível deletar o arquivo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale("pt-BR")).format(Date())
        val storageDir: File = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return null
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            newPatientViewModel.currentImagePath.value = absolutePath
        }
    }
}