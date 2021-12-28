package agh.ics.oop.maps


import agh.ics.oop.classes.Vector2d
import agh.ics.oop.enumClasses.MapDirection

class ConcreteMap(
    width: Int,
    height: Int,
    nOfPlants: Int,
    grassEnergy: Float,
    dayCostEnergy: Float,
    minLoveEnergy: Float,
    animalInitialEnergy: Float
) : AbstractWorldMap(width, height, nOfPlants, grassEnergy, dayCostEnergy, minLoveEnergy, animalInitialEnergy) {


    override fun getForwardPosition(position: Vector2d, orientation: MapDirection): Vector2d {
        val newPosition = position + orientation.toUnitVector()
        if (newPosition follows upperLeft && newPosition precedes lowerRight){
            return newPosition
        }
        return position
    }

}