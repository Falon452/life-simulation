package agh.ics.oop

import agh.ics.oop.classes.Vector2d
import agh.ics.oop.enumClasses.MapDirection
import agh.ics.oop.maps.AbstractWorldMap


class FoldableMap(
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
        if (newPosition.x > lowerRight.x) newPosition.x = upperLeft.x
        if (newPosition.y > lowerRight.y) newPosition.y = upperLeft.y
        if (newPosition.x < upperLeft.x) newPosition.x = lowerRight.x
        if (newPosition.y < upperLeft.y) newPosition.y = lowerRight.y
        return newPosition
    }

}



