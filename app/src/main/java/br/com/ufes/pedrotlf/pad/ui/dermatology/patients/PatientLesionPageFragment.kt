package br.com.ufes.pedrotlf.pad.ui.dermatology.patients

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import br.com.ufes.pedrotlf.pad.*
import br.com.ufes.pedrotlf.pad.data.dto.LesionDTO
import br.com.ufes.pedrotlf.pad.data.dto.LesionImageDTO
import br.com.ufes.pedrotlf.pad.databinding.FragmentDermatologyPatientLesionPageBinding
import com.bumptech.glide.Glide
import java.io.File
import java.io.IOException

class PatientLesionPageFragment(
    private val patientId: Int,
    private val lesion: LesionDTO?,
    private val selfDestroy: () -> Unit,
    private val onLesionAttached: (LesionDTO) -> Unit
): BaseFragment() {

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

        viewModel.currentLesion.value = lesion

        binding.apply {
            setLesionFields()
            setPictures()

            observeLesionChanges()
            observePicturesChanges()
        }
    }

    private fun FragmentDermatologyPatientLesionPageBinding.observeLesionChanges() {
        viewModel.lesionUpdated.observe(viewLifecycleOwner) {
            if (it != 0) {
                Toast.makeText(root.context, "Lesão atualizada!", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.lesionCreated.observe(viewLifecycleOwner) {
            viewModel.currentLesion.value = it
            Toast.makeText(root.context, "Lesão vinculada ao paciente!", Toast.LENGTH_SHORT).show()
            onLesionAttached.invoke(it)
        }
        viewModel.lesionRemoved.observe(viewLifecycleOwner) { wasRemoved ->
            if(wasRemoved)
                selfDestroy.invoke()
        }

        viewModel.currentLesion.observe(viewLifecycleOwner) { lesion ->
            if (lesion != null) {
                fragmentDermatologyPatientWoundFooterNextButton.apply {
                    text = getString(R.string.dermatology_patient_details_update_lesion_info)
                    setOnClickListener { viewModel.updateLesion(lesion.lessionData) }
                }
                fragmentDermatologyPatientDetailsLesionPhotosCamera.setOnClickListener {
                    it.openCamera()
                }
            } else {
                fragmentDermatologyPatientWoundFooterNextButton.apply {
                    text = getString(R.string.dermatology_patient_details_attach_lesion)
                    setOnClickListener { viewModel.attachLesion(patientId) }
                }
                fragmentDermatologyPatientDetailsLesionPhotosCamera.setOnClickListener {
                    Toast.makeText(root.context, "Vincule a lesão ao paciente antes de adicionar imagens!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun FragmentDermatologyPatientLesionPageBinding.setLesionFields(){
        viewModel.apply {
            val regionList = root.context.getRegionList()
            val diagnosisList = root.context.getDiagnosisList()
            fragmentDermatologyPatientWoundBodyRegion.apply {
                doOnTextChanged { text, _, _, _ ->
                    bodyRegion = text?.toString() ?: ""
                }
                setText(lesion?.lessionData?.bodyRegion)
                setAutoCompleteOptions(regionList)
            }
            fragmentDermatologyPatientWoundDiagnostic.apply {
                doOnTextChanged { text, _, _, _ ->
                    diagnostic = text?.toString() ?: ""
                }
                setText(lesion?.lessionData?.diagnostic)
                setAutoCompleteOptions(diagnosisList)
            }
            fragmentDermatologyPatientWoundDiagnosticSecondary.apply {
                doOnTextChanged { text, _, _, _ ->
                    diagnosticSecondary = text?.toString() ?: ""
                }
                setText(lesion?.lessionData?.diagnosticSecondary)
                setAutoCompleteOptions(diagnosisList)
            }
            fragmentDermatologyPatientWoundRadioGrown.setBooleanRadioGroupListener(lesion?.lessionData?.woundGrown){ woundGrown = it }
            fragmentDermatologyPatientWoundRadioItched.setBooleanRadioGroupListener(lesion?.lessionData?.woundItched){ woundItched = it }
            fragmentDermatologyPatientWoundRadioBleed.setBooleanRadioGroupListener(lesion?.lessionData?.woundBleed){woundBleed = it}
            fragmentDermatologyPatientWoundRadioHurt.setBooleanRadioGroupListener(lesion?.lessionData?.woundHurt){woundHurt = it}
            fragmentDermatologyPatientWoundRadioPatternChanged.setBooleanRadioGroupListener(lesion?.lessionData?.woundPatternChanged){woundPatternChanged = it}
            fragmentDermatologyPatientWoundRadioRelief.setBooleanRadioGroupListener(lesion?.lessionData?.woundHasRelief){woundHasRelief = it}

            fragmentDermatologyPatientWoundDeleteButton.setOnClickListener {
                it.context.showDeleteLesionDialog()
            }
        }
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
        lesion?.images?.let { imagesDto ->
            viewModel.setInitialImagePathList(imagesDto)
        }
    }

    private fun FragmentDermatologyPatientLesionPageBinding.observePicturesChanges() {
        viewModel.imageList.observe(viewLifecycleOwner) {
            fragmentDermatologyPatientDetailsLesionPhotosList.removeAllViews()
            if (!it.isNullOrEmpty()) {
                fragmentDermatologyPatientDetailsLesionPhotosList.isVisible = true
                it.forEachIndexed { i, img ->
                    context?.let { ctx ->
                        val imageView = ImageView(ctx)
                        val params = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        if(i > 0)
                            params.setMargins(0,20,0,0)
                        imageView.layoutParams = params
                        imageView.id = View.generateViewId()
                        imageView.setOnClickListener { ctx.showDeleteImageDialog(img) }
                        Glide.with(ctx).load(img.image).override(700).into(imageView)
                        fragmentDermatologyPatientDetailsLesionPhotosList.addView(imageView)
                    }
                }
            } else {
                fragmentDermatologyPatientDetailsLesionPhotosList.isVisible = false
            }
        }
    }

    private fun Context.showDeleteImageDialog(image: LesionImageDTO){
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.dialog_delete_image_confirmation))
            .setPositiveButton(R.string.word_confirm) { dialog, _ ->
                viewModel.deleteImage(image)
                dialog.dismiss()
            }.setNegativeButton(R.string.word_cancel){ dialog, _ ->
                dialog.cancel()
            }.setOnCancelListener {
                //Do nothing
            }.create()
            .show()
    }

    private fun Context.showDeleteLesionDialog(){
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.dialog_delete_lesion_confirmation))
            .setPositiveButton(R.string.word_confirm) { dialog, _ ->
                viewModel.deleteLesion()
                dialog.dismiss()
            }.setNegativeButton(R.string.word_cancel){ dialog, _ ->
                dialog.cancel()
            }.setOnCancelListener {
                //Do nothing
            }.create()
            .show()
    }

    private fun View.openCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            activity?.packageManager?.let { packageManager ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile{
                            viewModel.currentImagePath.value = it
                        }
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
                viewModel.confirmImagePath()
            } else {
                context?.let { ctx ->
                    viewModel.currentImagePath.value?.also{
                        val file = File(it)
                        if(file.delete())
                            Toast.makeText(ctx, "Arquivo de imagem cancelado", Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(ctx, "Não foi possível excluir o arquivo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
}