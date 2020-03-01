package com.example.endlessrecyclerview.utils

class PageConfigurator {
    companion object {
        var START_PAGE = 1
        var CURRENT_PAGE = START_PAGE
        var TOTAL_PAGES = 2
        var ITEMS_PER_PAGE = 5

        fun isPageAvailable() = CURRENT_PAGE <= TOTAL_PAGES
    }
}