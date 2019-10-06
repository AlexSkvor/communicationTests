package ru.lingstra.avitocopy.system.network

class Queue<T>(val size: Int) {

    init {
        require(size > 0) { "queue size might not be less then one $size" }
    }

    private val list: MutableList<T> = mutableListOf()

    fun firstOrDefault(default: T) = list.firstOrNull() ?: default

    fun put(element: T) {
        if (list.size < size) list.add(element)
        else {
            list.removeAt(0)
            list.add(element)
        }
    }
}