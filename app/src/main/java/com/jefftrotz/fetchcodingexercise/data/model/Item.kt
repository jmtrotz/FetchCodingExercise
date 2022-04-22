package com.jefftrotz.fetchcodingexercise.data.model

/**
 * Item object model.
 * @param id The ID of the Item.
 * @param listId The list ID of the Item.
 * @param name The name of the Item.
 */
data class Item(
    val id: Int,
    val listId: Int,
    val name: String
)