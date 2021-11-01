package br.com.ufes.pedrotlf.pad

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import br.com.ufes.pedrotlf.pad.ui.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment: Fragment() {
    private var loadingDialog: LoadingDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { loadingDialog = LoadingDialog(it) }
    }

    protected fun showLoading(loadingText: String? = null){
        loadingDialog?.showLoading(loadingText)
    }

    protected fun dismissLoading(){
        loadingDialog?.dismissLoading()
    }
}