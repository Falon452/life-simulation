package agh.ics.oop.classes

import java.util.*
import kotlin.math.max
import kotlin.math.min


class Vector2d(var x: Int, var y: Int) {
    override fun toString(): String = "($x,$y)"

    infix fun precedes(other: Vector2d) = (this.x <= other.x) && (this.y <= other.y)
    infix fun follows(other: Vector2d) = (this.x >= other.x) && (this.y >= other.y)


    operator fun plus(other: Vector2d) = Vector2d(this.x + other.x, this.y + other.y)
    operator fun minus(other: Vector2d) = Vector2d(this.x - other.x, this.y - other.y)

    override fun equals(other: Any?): Boolean {
        if (other is Vector2d) {
            return (this.x == other.x) && (this.y == other.y)
        }
        return false
    }

    /* these were created in order to use this:val (left, bottom) = Any:Vector2d */
    operator fun component1() = x
    operator fun component2() = y

    override fun hashCode(): Int {
        return Objects.hash(this.x, this.y)
    }
}
