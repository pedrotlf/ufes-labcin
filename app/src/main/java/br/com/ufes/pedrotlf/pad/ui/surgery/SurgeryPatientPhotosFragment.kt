package br.com.ufes.pedrotlf.pad.ui.surgery

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.data.Resource
import br.com.ufes.pedrotlf.pad.databinding.FragmentSurgeryPatientPhotosBinding
import br.com.ufes.pedrotlf.pad.ui.dermatology.newpatient.NewPatientLesionPhotosFragmentDirections
import com.bumptech.glide.Glide
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SurgeryPatientPhotosFragment: BaseFragment() {

    private var _binding: FragmentSurgeryPatientPhotosBinding? = null
    private val binding get() = _binding!!
    private val patientPhotosViewModel by viewModels<SurgeryPatientPhotosViewModel>()
    private val args by navArgs<SurgeryPatientPhotosFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSurgeryPatientPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentSurgeryPatientPhotosCamera.setOnClickListener {
                openCamera()
            }

            fragmentSurgeryPatientPhotosFooterConfirmButton.setOnClickListener {
                patientPhotosViewModel.sendPatientLesion(
                    args.susNumber,
                    args.surgeryPatientLesion
                )
            }

            observeFilesList()
            loadSavedList()
        }

        patientPhotosViewModel.sendPatientLesionRequest.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    dismissLoading()
                    Toast.makeText(context, "Lesão cadastrada com sucesso!", Toast.LENGTH_LONG).show()
                    val action = SurgeryPatientPhotosFragmentDirections.actionBackToSurgeryHomeFragment()
                    findNavController().navigate(action)
                }
                is Resource.Failure -> {
                    dismissLoading()
                    Toast.makeText(context, "Erro! Não foi possível enviar a lesão", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun FragmentSurgeryPatientPhotosBinding.loadSavedList(){
        patientPhotosViewModel.imagePathList.value?.forEach { addImagePreview(it) }
    }

    private fun FragmentSurgeryPatientPhotosBinding.observeFilesList() {
        patientPhotosViewModel.apply {
            imagePathList.observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty() && it.last() == currentImagePath.value) {
                    addImagePreview(it.last())
                    currentImagePath.value = null
                }
            }
        }
    }

    private fun FragmentSurgeryPatientPhotosBinding.addImagePreview(imgPath: String) {
        context?.let { ctx ->
            val imageView = ImageView(ctx)
            imageView.layoutParams =
                ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            imageView.id = View.generateViewId()
            Glide.with(ctx).load(imgPath).override(300).into(imageView)
            fragmentSurgeryPatientPhotosList.addView(imageView)
            fragmentSurgeryPatientPhotosListFlow.addView(imageView)
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
                patientPhotosViewModel.confirmImagePath()
            } else {
                context?.let { ctx ->
                    patientPhotosViewModel.currentImagePath.value?.also{
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
            patientPhotosViewModel.currentImagePath.value = absolutePath
        }
    }
}