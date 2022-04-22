package com.jefftrotz.fetchcodingexercise.data.repository

import com.jefftrotz.fetchcodingexercise.data.model.Item
import org.json.JSONArray
import org.json.JSONTokener
import java.net.URL

/**
 * Main repository for this application.
 */
class MainRepository {
    /**
     * Fetches data from the URL and returns it as a List of Item objects.
     * @return A List of Item objects.
     */
    fun getItems(): List<Item> {
        val items = URL("https://fetch-hiring.s3.amazonaws.com/hiring.json").readText()
        val jsonArray = JSONTokener(items).nextValue() as JSONArray
        val itemList: ArrayList<Item> = ArrayList()

        // Convert the JSON objects to Item objects
        for (index in 0 until jsonArray.length()) {
            val id = jsonArray.getJSONObject(index).getInt("id")
            val listId = jsonArray.getJSONObject(index).getInt("listId")
            val name = jsonArray.getJSONObject(index).getString("name")
            val item = Item(id, listId, name)
            itemList.add(item)
        }

        return itemList
    }
}