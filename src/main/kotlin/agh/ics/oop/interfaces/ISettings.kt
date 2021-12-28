package agh.ics.oop.interfaces

import agh.ics.oop.classes.Vector2d

interface ISettings {
    val width: Int
    val height: Int
    val nOfPlants: Int
    val grassEnergy: Float
    val minLoveEnergy: Float
    val animalEnergy: Float
    val dayCostEnergy: Float
    val initialPositions: List<Vector2d>
}