package agh.ics.oop.settings

import agh.ics.oop.classes.Vector2d
import agh.ics.oop.interfaces.ISettings

class DefaultSettings: ISettings{
    override val width = 10
    override val height = 10
    override val nOfPlants = 32
    override val grassEnergy = 4F
    override val minLoveEnergy = 10F
    override val animalEnergy = 60F
    override val dayCostEnergy = 1F
    override val initialPositions = listOf(
        Vector2d(4,4),
        Vector2d(3,4),
        Vector2d(4,2),
        Vector2d(7,2),
        Vector2d(8,5),
        Vector2d(4,7),
        Vector2d(6,4),
        Vector2d(1,4),
        Vector2d(7,2),
        Vector2d(2,6),
    )
}