package br.com.ufes.pedrotlf.pad.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.ufes.pedrotlf.pad.BaseFragment
import br.com.ufes.pedrotlf.pad.R
import br.com.ufes.pedrotlf.pad.data.Resource
import br.com.ufes.pedrotlf.pad.databinding.FragmentDialogServerSettingsChangeBinding
import br.com.ufes.pedrotlf.pad.databinding.FragmentPadHomeBinding

class PadHomeFragment: BaseFragment() {

    private var _binding: FragmentPadHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<PadHomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPadHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                context?.showDialog()
            }

            fragmentHomeFooterTestConnection.setOnClickListener {
                homeViewModel.testConnection()
            }

            observeConnectionStatus()
        }
    }

    private fun FragmentPadHomeBinding.observeConnectionStatus() {
        homeViewModel.connectionStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    fragmentHomeServerConnectionStatusLoadingLayout.isVisible = true
                    fragmentHomeServerConnectionStatusLoadingBar.show()
                    fragmentHomeServerConnectionStatus.isVisible = false
                }
                is Resource.Success -> {
                    fragmentHomeServerConnectionStatusLoadingLayout.isVisible = false
                    fragmentHomeServerConnectionStatusLoadingBar.hide()
                    fragmentHomeServerConnectionStatus.isVisible = true
                    fragmentHomeServerConnectionStatus.text =
                        getString(R.string.home_footer_server_connection_status_ok)
                    context?.let { ctx ->
                        fragmentHomeServerConnectionStatus.setTextColor(
                            ContextCompat.getColor(ctx, R.color.green_dark)
                        )
                    }
                }
                is Resource.Failure -> {
                    fragmentHomeServerConnectionStatusLoadingLayout.isVisible = false
                    fragmentHomeServerConnectionStatusLoadingBar.hide()
                    fragmentHomeServerConnectionStatus.isVisible = true
                    fragmentHomeServerConnectionStatus.text =
                        getString(R.string.home_footer_server_connection_status_failed)
                    context?.let { ctx ->
                        fragmentHomeServerConnectionStatus.setTextColor(
                            ContextCompat.getColor(ctx, R.color.red_dark)
                        )
                    }
                }
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
        val binding = FragmentDialogServerSettingsChangeBinding.inflate(inflater)

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