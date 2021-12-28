package agh.ics.oop.classes

import agh.ics.oop.FoldableMap
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class AnimalTest {

    @Test
    fun loves() {
        val m = FoldableMap(4, 4, 1,1F,1F,1F,1F)
        val a = Animal("1", Vector2d(2,2), 16F, GenesGenerator().getRandomGenes(),
            10F, false, m)
        val a2 = Animal("2", Vector2d(2,2), 16F, GenesGenerator().getRandomGenes(),
            10F, false, m)
        val kidGenes = a loves a2
        assertEquals(32, kidGenes?.size)
        assertEquals(12.0, a.energy)
        assertEquals(24.0, a2.energy)
    }
}