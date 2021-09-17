package ru.sber.oop

data class User(val name: String, val age: Long) {
    var city: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (name != other.name) return false
        if (age != other.age) return false
        if (city != other.city) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + age.hashCode()
        result = 31 * result + (city?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "User(name='$name', age=$age, city=$city)"
    }

}

fun main() {
    val user1 = User("Alex", 13)
    println(user1)
    val user2 = user1.copy(name = "Jimi")
    user1.city = "Omsk"
    println(user1)
    println(user2)
    val user3 = user1.copy()
    user3.city = "Tomsk"
    println(user1)
    println(user2)
    println(user3)
    println(user1 == user3)
}