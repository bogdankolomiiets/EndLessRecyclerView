package com.example.endlessrecyclerview.adapters

import com.example.endlessrecyclerview.models.Row

interface CustomItemCallback {
    fun onToastBtn(model: Row, position: Int)
    fun onSnackBtn(model: Row, position: Int)
}