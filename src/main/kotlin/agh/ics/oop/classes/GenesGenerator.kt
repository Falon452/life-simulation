package agh.ics.oop.classes

import agh.ics.oop.enumClasses.MoveDirection
import agh.ics.oop.enumClasses.MoveDirection.*

class GenesGenerator {
    fun getRandomGenes(): List<MoveDirection> {
        val randomGenes = mutableListOf<MoveDirection>()
        val possibleGenes = listOf(FORWARD, LIL_RIGHT, RIGHT, MAX_RIGHT, BACKWARD, MAX_LEFT, LEFT, LIL_LEFT)

        for (i in 0..31)
            randomGenes.add(possibleGenes.random())
        return randomGenes
    }
}