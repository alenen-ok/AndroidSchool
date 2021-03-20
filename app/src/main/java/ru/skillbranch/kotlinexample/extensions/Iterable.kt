package ru.skillbranch.kotlinexample.extensions

fun <T> List<T>.dropLastUntil(predicate: (T) -> Boolean): List<T> {
    val indexOfTruePredicate = this.indexOfLast(predicate)
    val size = this.size
    return this.dropLast(size - indexOfTruePredicate)
}