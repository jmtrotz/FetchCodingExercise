package com.jefftrotz.fetchcodingexercise.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Extension function to execute tasks before and after the background task has finished.
 * @param onPreExecute A task to perform before the background task begins.
 * @param doInBackground A task to perform in the background.
 * @param onPostExecute A task to perform after the background task has finished.
 */
fun <R> CoroutineScope.executeAsyncTask(
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