package agh.ics.oop.maps

import agh.ics.oop.FoldableMap
import agh.ics.oop.classes.Vector2d
import agh.ics.oop.enumClasses.MapDirection
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class FoldableMapTest {

    @Test
    fun getForwardPosition() {
        val m = FoldableMap(4, 4, 1,1F,1F,1F,1F)
        assertEquals(Vector2d(0,3), m.getForwardPosition(Vector2d(3,3), MapDirection.SOUTH))
        assertEquals(Vector2d(2,0), m.getForwardPosition(Vector2d(3,3), MapDirection.NORTH_EAST))
        assertEquals(Vector2d(3,0), m.getForwardPosition(Vector2d(3,3), MapDirection.EAST))
        assertEquals(Vector2d(0,0), m.getForwardPosition(Vector2d(3,3), MapDirection.SOUTH_EAST))
        assertEquals(Vector2d(0,2), m.getForwardPosition(Vector2d(3,3), MapDirection.SOUTH_WEST))

        assertEquals(Vector2d(3,0), m.getForwardPosition(Vector2d(0,0), MapDirection.NORTH))
        assertEquals(Vector2d(3,3), m.getForwardPosition(Vector2d(0,0), MapDirection.NORTH_WEST))
        assertEquals(Vector2d(0,3), m.getForwardPosition(Vector2d(0,0), MapDirection.WEST))
        assertEquals(Vector2d(1,3), m.getForwardPosition(Vector2d(0,0), MapDirection.SOUTH_WEST))
        assertEquals(Vector2d(3,3), m.getForwardPosition(Vector2d(0,0), MapDirection.NORTH_WEST))
    }
}