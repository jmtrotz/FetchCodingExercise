package com.jefftrotz.fetchcodingexercise.ui.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jefftrotz.fetchcodingexercise.R
import com.jefftrotz.fetchcodingexercise.databinding.ActivityMainBinding
import com.jefftrotz.fetchcodingexercise.ui.adapter.MainAdapter
import com.jefftrotz.fetchcodingexercise.ui.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Main Activity for this application. Displays a list
 * of items downloaded from a remote data source.
 */
class MainActivity: AppCompatActivity() {
    private val mViewModel by viewModel<MainViewModel>()

    private var _mBinding: ActivityMainBinding? = null
    private var _mLoadingDialog: Dialog? = null

    /**
     * Called when the activity is starting. Shows the splash screen,
     * initializes the _mBinding variable, and shows the loading dialog.
     * Data is downloaded in a background thread. Once the data has been
     * downloaded, the RecyclerView is initialized and the loading dialog
     * is dismissed.
     * @param savedInstanceState If the activity is being re-initialized
     * after previously being shut down, then this Bundle contains the
     * data it most recently supplied in onSaveInstanceState().
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        _mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_mBinding?.root)

        if (isOnline()) {
            initializeUi()
        } else {
            showOfflineAlertDialog()
        }
    }

    /**
     * Called when the activity is destroyed. Sets the _mBinding
     * variable back to null to avoid memory leaks.
     */
    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

    /**
     * Checks if the device has internet connectivity.
     * @return Current connectivity state as a Boolean.
     */
    private fun isOnline(): Boolean {
        val connectivityManager = this.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork)

        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }

        return false
    }

    private fun initializeUi() {
        lifecycleScope.launch {
            launch {
                mViewModel.items.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                    .collect { items ->
                        _mBinding?.recyclerView?.adapter = items?.let { MainAdapter(it) }
                        _mBinding?.recyclerView?.layoutManager =
                            LinearLayoutManager(this@MainActivity)
                    }
            }
            launch {
                mViewModel.isLoading.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                    .collect { isLoading ->
                        if (isLoading) {
                            showLoadingDialog()
                        } else {
                            dismissLoadingDialog()
                        }
                    }
            }
        }
    }

    /**
     * Shows an alert dialog informing the user that their device is offline.
     */
    private fun showOfflineAlertDialog() {
        val dialogBuilder = MaterialAlertDialogBuilder(this, R.layout.dialog_background)
        dialogBuilder.setTitle(R.string.offline_alert_dialog_title)
        dialogBuilder.setMessage(R.string.offline_alert_dialog_message)
        dialogBuilder.setPositiveButton(
            R.string.offline_alert_dialog_positive_button_text) { dialogInterface, _ ->
            if (isOnline()) {
                dialogInterface.dismiss()
                initializeUi()
            } else {
                showOfflineAlertDialog()
            }
        }
        dialogBuilder.setNegativeButton(
            R.string.offline_alert_dialog_negative_button_text) { _, _ ->
            finishAffinity()
        }

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    /**
     * Shows a dialog to inform the user that data is being loaded.
     */
    private fun showLoadingDialog() {
        _mLoadingDialog = Dialog(this)
        _mLoadingDialog?.setContentView(R.layout.dialog_background)
        _mLoadingDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _mLoadingDialog?.show()
    }

    /**
     * Dismisses the loading dialog.
     */
    private fun dismissLoadingDialog() {
        if (_mLoadingDialog != null && _mLoadingDialog!!.isShowing) {
            _mLoadingDialog?.dismiss()
            _mLoadingDialog = null
        }
    }
}