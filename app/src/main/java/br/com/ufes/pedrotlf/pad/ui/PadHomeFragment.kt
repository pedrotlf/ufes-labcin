package br.com.ufes.pedrotlf.pad.ui

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.R
import br.com.ufes.pedrotlf.pad.databinding.FragmentPadHomeBinding
import br.com.ufes.pedrotlf.pad.databinding.FragmentServerSettingsChangeBinding

class PadHomeFragment: BaseFragment() {

    private var _binding: FragmentPadHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<PadHomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPadHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentHomeButtonCirurgia.setOnClickListener {
                val action = PadHomeFragmentDirections.actionPadHomeFragmentToSurgeryHomeFragment()
                findNavController().navigate(action)
            }

            fragmentHomeButtonDermatologia.setOnClickListener {
                val action = PadHomeFragmentDirections.actionPadHomeFragmentToDermatologyHomeFragment()
                findNavController().navigate(action)
            }

            setServerSettingsInfo()

            fragmentHomeFooterChangeSettings.setOnClickListener {
                context?.let{ it.showDialog() }
            }
        }
    }

    private fun setServerSettingsInfo() {
        homeViewModel.serverIpAndPort.apply {
            getOrNull(0)?.let { binding.fragmentHomeServerIp.text = it }
            getOrNull(1)?.let { binding.fragmentHomeServerPort.text = it }
        }
    }

    private fun Context.showDialog(){
        val builder = AlertDialog.Builder(this)

        val inflater = layoutInflater
        val binding = FragmentServerSettingsChangeBinding.inflate(inflater)

        homeViewModel.serverIpAndPort.apply {
            getOrNull(0)?.let { binding.serverIp.setText(it) }
            getOrNull(1)?.let { binding.serverPort.setText(it) }
        }

        builder.setView(binding.root).setPositiveButton(R.string.word_confirm) { dialog, _ ->
            val ip = binding.serverIp.text.toString()
            val port = binding.serverPort.text.toString()

            homeViewModel.changeServerUrl("http://$ip:$port/")
            setServerSettingsInfo()
            dialog.dismiss()
        }

        builder.create().show()
    }
}