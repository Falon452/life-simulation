package agh.ics.oop.classes


import agh.ics.oop.enumClasses.MapDirection
import agh.ics.oop.interfaces.IElement
import agh.ics.oop.interfaces.IWorldMap
import agh.ics.oop.enumClasses.MoveDirection
import agh.ics.oop.enumClasses.MapDirection.*
import agh.ics.oop.enumClasses.MoveDirection.*
import javafx.scene.image.Image
import java.io.FileInputStream
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.properties.Delegates


typealias PositionObserverFunc = (Pair<Vector2d, String>, Vector2d) -> Unit


class Animal(
    val name: String,
    initialPosition: Vector2d,
    initialEnergy: Float,
    genes: List<MoveDirection>,
    minimumEnergyToLove: Float,
    isAncestorOfObservedAnimal: Boolean = false,
    private val map: IWorldMap) : IElement {

    companion object {
        fun northImage() = Image(FileInputStream("src/main/resources/snail/snailN.png"))
        fun northEastImage() = Image(FileInputStream("src/main/resources/snail/snailNE.png"))
        fun eastImage() = Image(FileInputStream("src/main/resources/snail/snailE.png"))
        fun southEastImage() = Image(FileInputStream("src/main/resources/snail/snailSE.png"))
        fun southImage() = Image(FileInputStream("src/main/resources/snail/snailS.png"))
        fun southWestImage() = Image(FileInputStream("src/main/resources/snail/snailSW.png"))
        fun westImage() = Image(FileInputStream("src/main/resources/snail/snailW.png"))
        fun northWestImage() = Image(FileInputStream("src/main/resources/snail/snailNW.png"))
    }

    private val moveObservers: MutableList<PositionObserverFunc> = mutableListOf()

    val genes = if (genes.size == 32) genes
        else throw IllegalArgumentException("Genes List must be of length 32.")

    private val minimumEnergyToLove = if (minimumEnergyToLove >= 0) minimumEnergyToLove
        else throw IllegalArgumentException("minimumEnergyToLove must be positive.")

    var orientation: MapDirection = NORTH
        private set

    private val discreteCDF: Array<Float> = computeDiscreteCumulativeDensityFunction(genes)
    lateinit var genesCounter: HashMap<MoveDirection, Int>

    override var position: Vector2d by Delegates.observable(initialPosition) { _, new, old ->
        moveObservers.forEach { observer -> observer(Pair(new, name), old) }
    }
        private set

    var alive: Boolean = true
    var dayOfDeath by Delegates.notNull<Int>()
    var numberOfKids = 0
    var observedNOfKids = 0
    var observedNofDescendants = 0
    val isDescendantOfObservedAnimal = isAncestorOfObservedAnimal

    override var energy: Float= if (initialEnergy >= 0) initialEnergy
    else throw IllegalArgumentException("Energy must be positive.")
        private set

    fun addObserver(observer: PositionObserverFunc) = moveObservers.add(observer)

    fun eat(gainedEnergy: Float) {
        energy += gainedEnergy
    }
    fun oneDayCloserToDeath(energyCostOfDay: Float){
        energy -= energyCostOfDay
    }
    fun increaseKidsCounter(){
        numberOfKids += 1
    }

    fun move() {
        when (setPreferredOrientation(discreteCDF)) {
            FORWARD -> position = map.getForwardPosition(position, orientation)
            LIL_RIGHT -> orientation = orientation.toLilRight()
            RIGHT -> orientation = orientation.toRight()
            MAX_RIGHT -> orientation = orientation.toMaxRight()
            BACKWARD -> {

                position = map.getForwardPosition(position, orientation.toBackward())
            }
            MAX_LEFT -> orientation = orientation.toMaxLeft()
            LEFT -> orientation = orientation.toLeft()
            LIL_LEFT -> orientation = orientation.toLilLeft()
        }
    }

    infix fun loves(that: Animal): List<MoveDirection>? {
        if (this.energy >= minimumEnergyToLove && that.energy >= minimumEnergyToLove){
            val kidGenes = mutableListOf<MoveDirection>()
            val parent1Part: Float = this.energy.div((that.energy + this.energy))
            val parent2Part: Float = that.energy.div((that.energy + this.energy))
            val side = listOf(0, 1).random()
            if (parent1Part > parent2Part) {
                val nOfGenesFromParent1 = (parent1Part * 32).toInt()
                if (side == 0) {
                    kidGenes.addAll(this.genes.subList(0, nOfGenesFromParent1))
                    kidGenes.addAll(that.genes.subList(nOfGenesFromParent1, 32))
                } else {
                    kidGenes.addAll(that.genes.subList(0, 32 - nOfGenesFromParent1))
                    kidGenes.addAll(this.genes.subList(32 - nOfGenesFromParent1, 32))
                }
            } else {
                val nOfGenesFromParent2 = (parent2Part * 32).toInt()
                if (side == 0) {
                    kidGenes.addAll(that.genes.subList(0, nOfGenesFromParent2))
                    kidGenes.addAll(this.genes.subList(nOfGenesFromParent2, 32))
                } else {
                    kidGenes.addAll(this.genes.subList(0, 32 - nOfGenesFromParent2))
                    kidGenes.addAll(that.genes.subList(32 - nOfGenesFromParent2, 32))
                }
            }
            this.energy = 3 * this.energy.div(4)
            that.energy = 3 * that.energy.div(4)
            return kidGenes
        } else
            return null
    }

    private fun setPreferredOrientation(discreteCDF: Array<Float>): MoveDirection {
        val randomNumber = ((0..100).random().toFloat()).div(100F)
        val index = discreteCDF.binarySearch(randomNumber, 0, discreteCDF.size)
        return when (val indexOfOrientation = if (index >= 0) index else (-index) - 2){
            0 -> FORWARD
            1 -> LIL_RIGHT
            2 -> RIGHT
            3 -> MAX_RIGHT
            4 -> BACKWARD
            5 -> MAX_LEFT
            6 -> LEFT
            7 -> LIL_LEFT
            else -> {throw IllegalStateException("$indexOfOrientation must be in 0..7")}
        }
    }

    private fun computeDiscreteCumulativeDensityFunction(genes: List<MoveDirection>): Array<Float> {
        val genesCountMap = hashMapOf<MoveDirection, Int>()

        genes.forEach { gene ->
            when (gene) {
                FORWARD -> genesCountMap[FORWARD] = genesCountMap.getOrDefault(FORWARD, 0)  + 1
                LIL_RIGHT -> genesCountMap[LIL_RIGHT] = genesCountMap.getOrDefault(LIL_RIGHT, 0)  + 1
                RIGHT -> genesCountMap[RIGHT] = genesCountMap.getOrDefault(RIGHT, 0)  + 1
                MAX_RIGHT -> genesCountMap[MAX_RIGHT] = genesCountMap.getOrDefault(MAX_RIGHT, 0)  + 1
                BACKWARD -> genesCountMap[BACKWARD] = genesCountMap.getOrDefault(BACKWARD, 0)  + 1
                MAX_LEFT -> genesCountMap[MAX_LEFT] = genesCountMap.getOrDefault(MAX_LEFT, 0)  + 1
                LEFT -> genesCountMap[LEFT] = genesCountMap.getOrDefault(LEFT, 0)  + 1
                LIL_LEFT -> genesCountMap[LIL_LEFT] = genesCountMap.getOrDefault(LIL_LEFT, 0)  + 1
            }
        }
        genesCounter = genesCountMap

        val probabilities = arrayListOf<Float>()
        for (count in genesCountMap.values){ probabilities.add((count.toFloat()).div(32F)) }
        val dCDF: Array<Float> = arrayOf(0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F)
        for (i in 0..(probabilities.size-2)) {
            dCDF[i+1] = dCDF[i] + probabilities[i]
        }
        return dCDF
     }
}

class EnergyComparator: Comparator<Animal>{
    override fun compare(o1: Animal?, o2: Animal?): Int {
        if(o1 == null || o2 == null){
            return 0
        }
        return o1.energy.compareTo(o2.energy)
    }
}
