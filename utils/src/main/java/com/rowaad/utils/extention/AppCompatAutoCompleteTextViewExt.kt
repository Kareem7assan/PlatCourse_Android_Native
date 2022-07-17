package com.rowaad.utils.extention

import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatAutoCompleteTextView

fun AppCompatAutoCompleteTextView.setMyAdapter(
    keys: List<*>,
    arrayAdapter: ArrayAdapter<*>,
    hasHint: Boolean? = false,
    index: Int? = 0,
    onItemSelected: (item: String) -> Unit
): String {
    var selection = keys[index!!] as String
    isCursorVisible = false
    if (hasHint!!.not()) {
        setText(selection)
    }

    setAdapter(arrayAdapter)
    onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
        this@setMyAdapter.showDropDown()
        this@setMyAdapter.error = null
        selection = parent.getItemAtPosition(position) as String
        onItemSelected(selection)


    }
    setOnClickListener {
        this@setMyAdapter.showDropDown()
    }
    return selection

}

fun AppCompatAutoCompleteTextView.setMyAdapter(
    keys: List<*>,
    arrayAdapter: ArrayAdapter<*>,
    isHint: Boolean? = false,
    index: Int? = 0
): String {
    var selection = keys[index!!] as String
    isCursorVisible = false
    if (!isHint!!) {
        setText(selection)
    }

    setAdapter(arrayAdapter)
    onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
        this@setMyAdapter.showDropDown()
        this@setMyAdapter.error = null
        selection = parent.getItemAtPosition(position) as String


    }
    setOnClickListener{
        this@setMyAdapter.showDropDown()
    }
    return selection

}