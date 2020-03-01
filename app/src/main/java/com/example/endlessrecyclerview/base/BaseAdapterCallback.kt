package com.bohdan.kolomiiets.eventsreminder.base.adapter

import android.view.View

interface BaseAdapterCallback<T> {
    fun onItemClick(model: T, view: View)
    fun onLongItemClick(model: T, view: View): Boolean {
        return false
    }
}