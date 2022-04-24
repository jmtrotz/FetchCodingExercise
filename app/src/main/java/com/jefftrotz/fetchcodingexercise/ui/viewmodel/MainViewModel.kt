package com.jefftrotz.fetchcodingexercise.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.jefftrotz.fetchcodingexercise.data.model.Item
import com.jefftrotz.fetchcodingexercise.data.repository.MainRepository
import com.jefftrotz.fetchcodingexercise.util.ListUtils
import kotlin.collections.ArrayList

/**
 * Main ViewModel for this application. Acts as a link
 * between the MainActivity and the MainRepository.
 */
class MainViewModel: ViewModel() {
    private var mItemList: List<Item> = ArrayList()

    /**
     * Initializes the mItemList variable with
     * data returned by the MainRepository.
     */
    init {
        mItemList = MainRepository().getItems()
    }

    /**
     * Gets a List of Item objects sorted by their
     * listId and name properties from ListUtils.
     * @return A List of Item objets.
     * @see ListUtils
     */
    fun getItems(): List<Item> {
        return ListUtils().cleanUpList(mItemList)
    }
}