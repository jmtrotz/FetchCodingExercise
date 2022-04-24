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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jefftrotz.fetchcodingexercise.R
import com.jefftrotz.fetchcodingexercise.data.model.Item
import com.jefftrotz.fetchcodingexercise.databinding.ActivityMainBinding
import com.jefftrotz.fetchcodingexercise.ui.adapter.MainAdapter
import com.jefftrotz.fetchcodingexercise.ui.viewmodel.MainViewModel
import com.jefftrotz.fetchcodingexercise.util.executeAsyncTask

/**
 * Main Activity for this application. Displays a list
 * of items downloaded from a remote data source.
 */
class MainActivity : AppCompatActivity() {
    private var mBinding: ActivityMainBinding? = null
    private var mLoadingDialog: Dialog? = null
    private var mItemList: List<Item> = ArrayList()

    /**
     * Called when the activity is starting. Shows the splash screen,
     * initializes the mBinding variable, and shows the loading dialog.
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
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding?.root)

        if (isOnline(this)) {
            fetchData()
        } else {
            showOfflineAlertDialog()
        }
    }

    /**
     * Called when the activity is destroyed. Sets the mBinding
     * variable back to null to avoid memory leaks.
     */
    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    /**
     * Checks if the device has internet connectivity.
     * @return Current connectivity state as a Boolean.
     */
    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(
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

    /**
     * Retrieves data to be displayed from the ViewModel
     */
    private fun fetchData() {
        lifecycleScope.executeAsyncTask(onPreExecute = {
            showLoadingDialog()
        }, doInBackground = {
            mItemList = MainViewModel().getItems()
        }, onPostExecute = {
            initializeRecyclerView()
            dismissLoadingDialog()
        })
    }

    /**
     * Sets the LayoutManager for the RecyclerView, initializes the
     * RecyclerView adapter, and binds the adapter to the UI.
     */
    private fun initializeRecyclerView() {
        mBinding?.recyclerView?.layoutManager = LinearLayoutManager(this)
        val adapter = MainAdapter(mItemList)
        mBinding?.recyclerView?.adapter = adapter
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
            if (isOnline(this)) {
                dialogInterface.dismiss()
                fetchData()
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
        mLoadingDialog = Dialog(this)
        mLoadingDialog?.setContentView(R.layout.dialog_background)
        mLoadingDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mLoadingDialog?.show()
    }

    /**
     * Dismisses the loading dialog.
     */
    private fun dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog!!.isShowing) {
            mLoadingDialog?.dismiss()
            mLoadingDialog = null
        }
    }
}