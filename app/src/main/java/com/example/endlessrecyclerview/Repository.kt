package com.example.endlessrecyclerview

import com.example.endlessrecyclerview.models.Row

object Repository {
    private var idIncrementor = 0

    fun getNewPortionOfData(page: Int, itemsCount: Int): List<Row> {
        val tempList = mutableListOf<Row>()
        repeat(itemsCount) {
            tempList.add(Row(++idIncrementor, "Row id $idIncrementor from page $page"))
        }
        return tempList
    }
}