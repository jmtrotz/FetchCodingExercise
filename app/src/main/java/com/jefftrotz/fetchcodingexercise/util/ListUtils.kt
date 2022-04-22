package com.jefftrotz.fetchcodingexercise.util

import com.jefftrotz.fetchcodingexercise.data.model.Item

/**
 * Utilities for cleaning up a List of Item objects. Removes blank/null names
 * from the List, sorts them by their listId property, and by their name
 * property.
 */
class ListUtils {
    /**
     * Gets a List of Item objects sorted by their listId and name properties.
     * Items with null or blank names are removed first to make sorting them
     * faster as there will be fewer Item objects to sort through.
     * @return A List of Item objets.
     */
    fun cleanUpList(itemList: List<Item>): List<Item> {
        return sortListByListId(sortListByName(filterNames(itemList)))
    }

    /**
     * Removes an Item object from the List if its name property is blank or null.
     * @param itemList A List of Item objects to be filtered by their name property.
     * @return A List of Item objects whose name property is not blank or null.
     */
    private fun filterNames(itemList: List<Item>): List<Item> {
        return itemList.filterNot {item -> item.name.isBlank() || item.name == "null" }
    }

    /**
     * Sorts a List of Item objects by their listId property.
     * @param itemList A List of Item objects to be sorted by
     * their listId property.
     * @return A List of Item objects sorted by their listId
     * property.
     */
    private fun sortListByListId(itemList: List<Item>): List<Item> {
        return itemList.sortedWith(compareBy { it.listId })
    }

    /**
     * Sorts a List of Item objects by their name property.
     * @param itemList A List of Item objects to be sorted
     * by their name property.
     * @return A List of Item objects sorted by their name
     * property.
     * @see extractInt
     */
    private fun sortListByName(itemList: List<Item>): List<Item> {
        return itemList.sortedWith { object1, object2 ->
            extractInt(object1) - extractInt(object2)
        }
    }

    /**
     * Extracts the Integer following the String in an Item's name property
     * so a List of Item objects can be properly sored by their names.
     * @param item An Item object that is being sorted by its name property.
     * @return The value following an Item's name property as an Int.
     * @see sortListByName
     */
    private fun extractInt(item: Item): Int {
        val number = item.name.replace("\\D".toRegex(), "")

        if (number.isNotBlank()) {
            return Integer.parseInt(number)
        }

        return 0
    }
}