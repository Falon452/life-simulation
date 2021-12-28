package agh.ics.oop.classes

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PositionParserTest {

    @Test
    fun parse() {
        val input = "     (1,2) (3,4)    "
        val p = PositionParser()
        assertEquals(listOf(Vector2d(1,2), Vector2d(3,4)), p.parse(input))
    }
}