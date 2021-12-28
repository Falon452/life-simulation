package agh.ics.oop.enumClasses

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import agh.ics.oop.enumClasses.MapDirection.*

internal class MapDirectionTest {

    @Test
    fun toLilRight() {
        assertEquals(NORTH_EAST, NORTH.toLilRight())
        assertEquals(EAST, NORTH_EAST.toLilRight())
        assertEquals(SOUTH_EAST, EAST.toLilRight())
        assertEquals(SOUTH, SOUTH_EAST.toLilRight())
        assertEquals(SOUTH_WEST, SOUTH.toLilRight())
        assertEquals(WEST, SOUTH_WEST.toLilRight())
        assertEquals(NORTH_WEST, WEST.toLilRight())
        assertEquals(NORTH, NORTH_WEST.toLilRight())
    }

    @Test
    fun toRight() {
        assertEquals(EAST, NORTH.toRight())
        assertEquals(SOUTH_EAST, NORTH_EAST.toRight())
        assertEquals(SOUTH, EAST.toRight())
        assertEquals(SOUTH_WEST, SOUTH_EAST.toRight())
        assertEquals(WEST, SOUTH.toRight())
        assertEquals(NORTH_WEST, SOUTH_WEST.toRight())
        assertEquals(NORTH, WEST.toRight())
        assertEquals(NORTH_EAST, NORTH_WEST.toRight())
    }

    @Test
    fun toMaxRight() {
        assertEquals(SOUTH_EAST, NORTH.toMaxRight())
        assertEquals(SOUTH, NORTH_EAST.toMaxRight())
        assertEquals(SOUTH_WEST, EAST.toMaxRight())
        assertEquals(WEST, SOUTH_EAST.toMaxRight())
        assertEquals(NORTH_WEST, SOUTH.toMaxRight())
        assertEquals(NORTH, SOUTH_WEST.toMaxRight())
        assertEquals(NORTH_EAST, WEST.toMaxRight())
        assertEquals(EAST, NORTH_WEST.toMaxRight())
    }

    @Test
    fun toBackward() {
        assertEquals(SOUTH, NORTH.toBackward())
        assertEquals(SOUTH_WEST, NORTH_EAST.toBackward())
        assertEquals(WEST, EAST.toBackward())
        assertEquals(NORTH_WEST, SOUTH_EAST.toBackward())
        assertEquals(NORTH, SOUTH.toBackward())
        assertEquals(NORTH_EAST, SOUTH_WEST.toBackward())
        assertEquals(EAST, WEST.toBackward())
        assertEquals(SOUTH_EAST, NORTH_WEST.toBackward())
    }

    @Test
    fun toMaxLeft() {
        assertEquals(SOUTH_WEST, NORTH.toMaxLeft())
        assertEquals(WEST, NORTH_EAST.toMaxLeft())
        assertEquals(NORTH_WEST, EAST.toMaxLeft())
        assertEquals(NORTH, SOUTH_EAST.toMaxLeft())
        assertEquals(NORTH_EAST, SOUTH.toMaxLeft())
        assertEquals(EAST, SOUTH_WEST.toMaxLeft())
        assertEquals(SOUTH_EAST, WEST.toMaxLeft())
        assertEquals(SOUTH, NORTH_WEST.toMaxLeft())
    }

    @Test
    fun toLeft() {
        assertEquals(WEST, NORTH.toLeft())
        assertEquals(NORTH_WEST, NORTH_EAST.toLeft())
        assertEquals(NORTH, EAST.toLeft())
        assertEquals(NORTH_EAST, SOUTH_EAST.toLeft())
        assertEquals(EAST, SOUTH.toLeft())
        assertEquals(SOUTH_EAST, SOUTH_WEST.toLeft())
        assertEquals(SOUTH, WEST.toLeft())
        assertEquals(SOUTH_WEST, NORTH_WEST.toLeft())
    }

    @Test
    fun toLilLeft() {
        assertEquals(NORTH_WEST, NORTH.toLilLeft())
        assertEquals(NORTH, NORTH_EAST.toLilLeft())
        assertEquals(NORTH_EAST, EAST.toLilLeft())
        assertEquals(EAST, SOUTH_EAST.toLilLeft())
        assertEquals(SOUTH_EAST, SOUTH.toLilLeft())
        assertEquals(SOUTH, SOUTH_WEST.toLilLeft())
        assertEquals(SOUTH_WEST, WEST.toLilLeft())
        assertEquals(WEST, NORTH_WEST.toLilLeft())
    }
}