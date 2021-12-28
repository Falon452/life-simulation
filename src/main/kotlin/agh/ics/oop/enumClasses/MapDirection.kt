package agh.ics.oop.enumClasses

import agh.ics.oop.classes.Vector2d


enum class MapDirection {
    NORTH{
        override fun toLilRight(): MapDirection = NORTH_EAST
        override fun toRight(): MapDirection = EAST
        override fun toMaxRight(): MapDirection = SOUTH_EAST
        override fun toBackward(): MapDirection = SOUTH
        override fun toMaxLeft(): MapDirection = SOUTH_WEST
        override fun toLeft(): MapDirection = WEST
        override fun toLilLeft(): MapDirection = NORTH_WEST
        override fun toString(): String = "North"
        override fun toUnitVector() = Vector2d(-1, 0)
    },
    NORTH_EAST {
        override fun toLilRight(): MapDirection = EAST
        override fun toRight(): MapDirection = SOUTH_EAST
        override fun toMaxRight(): MapDirection = SOUTH
        override fun toBackward(): MapDirection = SOUTH_WEST
        override fun toMaxLeft(): MapDirection = WEST
        override fun toLeft(): MapDirection = NORTH_WEST
        override fun toLilLeft(): MapDirection = NORTH
        override fun toString(): String = "North East"
        override fun toUnitVector() = Vector2d(-1, 1)
    },
    EAST {
        override fun toLilRight(): MapDirection =  SOUTH_EAST
        override fun toRight(): MapDirection = SOUTH
        override fun toMaxRight(): MapDirection = SOUTH_WEST
        override fun toBackward(): MapDirection = WEST
        override fun toMaxLeft(): MapDirection = NORTH_WEST
        override fun toLeft(): MapDirection = NORTH
        override fun toLilLeft(): MapDirection = NORTH_EAST
        override fun toString(): String = "East"
        override fun toUnitVector() = Vector2d(0, 1)
     },
    SOUTH_EAST{
        override fun toLilRight(): MapDirection =  SOUTH
        override fun toRight(): MapDirection = SOUTH_WEST
        override fun toMaxRight(): MapDirection = WEST
        override fun toBackward(): MapDirection = NORTH_WEST
        override fun toMaxLeft(): MapDirection = NORTH
        override fun toLeft(): MapDirection = NORTH_EAST
        override fun toLilLeft(): MapDirection = EAST
        override fun toString(): String = "South East"
        override fun toUnitVector() = Vector2d(1, 1)
    },
    SOUTH{
        override fun toLilRight(): MapDirection =  SOUTH_WEST
        override fun toRight(): MapDirection = WEST
        override fun toMaxRight(): MapDirection = NORTH_WEST
        override fun toBackward(): MapDirection = NORTH
        override fun toMaxLeft(): MapDirection = NORTH_EAST
        override fun toLeft(): MapDirection = EAST
        override fun toLilLeft(): MapDirection = SOUTH_EAST
        override fun toString(): String = "South"
        override fun toUnitVector() = Vector2d(1, 0)
    },
    SOUTH_WEST {
        override fun toLilRight(): MapDirection =  WEST
        override fun toRight(): MapDirection = NORTH_WEST
        override fun toMaxRight(): MapDirection = NORTH
        override fun toBackward(): MapDirection = NORTH_EAST
        override fun toMaxLeft(): MapDirection = EAST
        override fun toLeft(): MapDirection = SOUTH_EAST
        override fun toLilLeft(): MapDirection = SOUTH
        override fun toString(): String = "South West"
        override fun toUnitVector() = Vector2d(1, -1)
    },
    WEST{
        override fun toLilRight(): MapDirection =  NORTH_WEST
        override fun toRight(): MapDirection = NORTH
        override fun toMaxRight(): MapDirection = NORTH_EAST
        override fun toBackward(): MapDirection = EAST
        override fun toMaxLeft(): MapDirection = SOUTH_EAST
        override fun toLeft(): MapDirection = SOUTH
        override fun toLilLeft(): MapDirection = SOUTH_WEST
        override fun toString(): String = "West"
        override fun toUnitVector() = Vector2d(0, -1)
    },
    NORTH_WEST{
        override fun toLilRight(): MapDirection =  NORTH
        override fun toRight(): MapDirection = NORTH_EAST
        override fun toMaxRight(): MapDirection = EAST
        override fun toBackward(): MapDirection = SOUTH_EAST
        override fun toMaxLeft(): MapDirection = SOUTH
        override fun toLeft(): MapDirection = SOUTH_WEST
        override fun toLilLeft(): MapDirection = WEST
        override fun toString(): String = "North West"
        override fun toUnitVector() = Vector2d(-1, -1)
    };

    abstract fun toLilRight(): MapDirection
    abstract fun toRight(): MapDirection
    abstract fun toMaxRight(): MapDirection
    abstract fun toBackward(): MapDirection
    abstract fun toMaxLeft(): MapDirection
    abstract fun toLeft(): MapDirection
    abstract fun toLilLeft(): MapDirection
    abstract override fun toString(): String
    abstract fun toUnitVector(): Vector2d

}
