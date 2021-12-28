package agh.ics.oop.interfaces

import agh.ics.oop.classes.Animal
import agh.ics.oop.classes.Vector2d
import agh.ics.oop.classes.Grass
import agh.ics.oop.enumClasses.MapDirection
import agh.ics.oop.enumClasses.MoveDirection


/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo
 * @author falon
 */
interface IWorldMap {
    /**
     * Place a animal on the map.
     *
     * @param animal
     * The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the map is already occupied.
     */
    fun place(animal: Animal): Boolean

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position
     * Position to check.
     * @return True if the position is occupied.
     */
    fun isAnythingAt(position: Vector2d): Boolean

    /**
     * Return an object at a given position.
     *
     * @param position
     * The position of the object.
     * @return Object or null if the position is not occupied.
     */
    fun objectAt(position: Vector2d): Pair<MutableList<Animal>, MutableList<Grass>>?


    /** Perform full day cycle
     *
     * @return true if there happened magic love and number of times it happened.
     */
    fun dayCycle(magic: Boolean): Boolean

    /** Return next name available for new animal
     *
     */
    fun nextAnimalName(): String

    /** Returns new position if moved forward from position with orientation
     *
     */
    fun getForwardPosition(position: Vector2d, orientation: MapDirection): Vector2d

    /** Gets number of living animals
     *
     */
    fun getNOfLivingAnimals(): Int

    /** Gets number of grasses on map.
     *
     */
    fun getNOfGrasses(): Int
    /** Gets genotype mode for all currently living animals
     *
     */
    fun getGenotypesMode(): MoveDirection?
    /** Gets average energy for all currently living animals.
     *
     */
    fun getAvgAnimalEnergy(): Float
    /** Gets average lifetime for all dead animals.
     *
     */
    fun getAvgLifetime(): Float?

    /** Gets average number of kids of currently living animals.
     *
     */
    fun getAvgNumberOfKids(): Float

    /** Whether given position is in jungle
     *
     */
    fun positionInJungle(position: Vector2d): Boolean

    /** set observed animal to track his descendant and kids
     *
     */
    fun setAnimalToObserve(animal: Animal)
}