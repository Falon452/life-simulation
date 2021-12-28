package agh.ics.oop.classes

import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.Test
import org.junit.jupiter.api.Test

internal class Vector2dTest {

    @Test
    fun testToString() {
        assertEquals("(2,3)", Vector2d(2,3).toString())
    }

    @Test
    fun precedes() {
        assertEquals(true, Vector2d(0,0 ) precedes Vector2d(0, 1))
        assertEquals(true, Vector2d(0,0 ) precedes Vector2d(1, 0))
    }

    @Test
    fun follows() {
        assertEquals(false, Vector2d(0,0 ) follows Vector2d(0, 1))
        assertEquals(false, Vector2d(0,0 ) follows Vector2d(1, 0))
    }

    @Test
    fun plus() {
        assertEquals(Vector2d(1,1), Vector2d(0, 1) + Vector2d(1, 0))
    }

    @Test
    fun minus() {
        assertEquals(Vector2d(-1,1), Vector2d(0, 1) - Vector2d(1, 0))
    }


    @Test
    fun testEquals() {
        assertEquals(true, Vector2d(0,1) == Vector2d(0, 1))
    }

    @Test
    operator fun component1() {
        assertEquals(1, Vector2d(1,0).x)
    }

    @org.junit.jupiter.api.Test
    operator fun component2() {
        assertEquals(1, Vector2d(0,1).y)
    }
}