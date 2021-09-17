package ru.sber.oop

open class Room(val name: String, val size: Int, val monster: Monster = Goblin("Gazlowe", "The Creator of Orgrimmar", "EPIC", 99999)) {

    constructor(name: String) : this(name, 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

class TownSquare : Room("Town Square", 1000) {
    override val dangerLevel = super.dangerLevel - 3

    final override fun load() = "Hop! Hey! La-la-ley!"
}

fun main() {
    println(Room("№1", 2).load())
    println(TownSquare().load())
}