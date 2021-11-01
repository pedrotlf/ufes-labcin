package br.com.ufes.pedrotlf.pad.ui

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import br.com.ufes.pedrotlf.pad.databinding.FragmentLoadingBinding

class LoadingDialog(private val activity: Activity) {

    private var dialog: AlertDialog? = null

    fun showLoading(loadingMessage: String? = null) {
        val builder = AlertDialog.Builder(activity)

        val inflater = activity.layoutInflater
        val binding = FragmentLoadingBinding.inflate(inflater)

        loadingMessage?.let {
            binding.loadingText.text = it
        }

        builder.setView(binding.root)
        builder.setCancelable(false)

        dialog = builder.create()
        dialog?.show()
    }

    fun dismissLoading() {
        dialog?.dismiss()
    }
}