package com.jefftrotz.fetchcodingexercise.ui.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jefftrotz.fetchcodingexercise.R
import com.jefftrotz.fetchcodingexercise.data.model.Item
import com.jefftrotz.fetchcodingexercise.databinding.ActivityMainBinding
import com.jefftrotz.fetchcodingexercise.ui.adapter.MainAdapter
import com.jefftrotz.fetchcodingexercise.ui.viewmodel.MainViewModel
import kotlinx.coroutines.*

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
     * Called when the activity is destroyed. Sets the mBinding
     * variable back to null to avoid memory leaks.
     */
    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
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
     * Shows a dialog to inform the user that data is being loaded.
     */
    private fun showLoadingDialog() {
        mLoadingDialog = Dialog(this)
        mLoadingDialog?.setContentView(R.layout.dialog_loading)
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

    /**
     * Extension function to execute tasks before and after the background task has finished.
     * @param onPreExecute A task to perform before the background task begins.
     * @param doInBackground A task to perform in the background.
     * @param onPostExecute A task to perform after the background task has finished.
     */
    private fun <R> CoroutineScope.executeAsyncTask(
        onPreExecute: () -> Unit,
        doInBackground: () -> R,
        onPostExecute: (R) -> Unit
    ) = launch {
        onPreExecute()
        val result = withContext(Dispatchers.IO) {
            doInBackground()
        }
        onPostExecute(result)
    }
}