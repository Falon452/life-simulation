package agh.ics.oop.maps


import agh.ics.oop.classes.Animal
import agh.ics.oop.classes.EnergyComparator
import agh.ics.oop.classes.Grass
import agh.ics.oop.classes.Vector2d
import agh.ics.oop.enumClasses.MoveDirection
import agh.ics.oop.interfaces.IWorldMap
import java.util.*

abstract class AbstractWorldMap(
    width: Int,
    height: Int,
    nOfPlants: Int,
    grassEnergy: Float,
    dayCostEnergy: Float,
    minLoveEnergy: Float,
    animalInitialEnergy: Float
) : IWorldMap {

    private val width = if (width >= 1) width
    else throw IllegalArgumentException("Argument width must be positive.")

    private val height = if (height >= 1) height
    else throw IllegalArgumentException("Argument height must be positive.")

    private val nOfPlants = if (nOfPlants >= 0) nOfPlants
    else throw IllegalArgumentException("Argument numberOfPlants must be positive.")

    private val grassEnergy = if (grassEnergy >= 1) grassEnergy
    else throw IllegalArgumentException("Grass Energy must be greater than zero.")

    private val dayCostEnergy = if (dayCostEnergy >= 0) dayCostEnergy
    else throw IllegalArgumentException("Argument energyCostOfDay must be greater than zero.")

    private val minLoveEnergy = if (minLoveEnergy >= 0) minLoveEnergy
    else throw IllegalArgumentException("Argument minLoveEnergy must be greater than zero.")

    private val animalInitialEnergy = if (animalInitialEnergy >= 0) animalInitialEnergy
    else throw IllegalArgumentException("Argument minLoveEnergy must be greater than zero.")

    private val allAnimals: MutableList<Animal> = mutableListOf()
    private val animalNames = hashMapOf<String, Boolean>()

    protected val upperLeft = Vector2d(0, 0)
    protected val lowerRight = Vector2d(this.height - 1, this.width - 1)
    private val jungleUpperLeft = Vector2d(0 + this.height / 4, 0 + this.width / 4)
    private val jungleLowerRight = Vector2d(this.height - this.height / 4 - 1, this.width - this.width / 4 - 1)

    private val jungleNumberOfPlants = this.nOfPlants.div(3)
    private var day = 0
    private var nOfAnimals = 0
    private var nOfLivingAnimals = 0
    private var nOfLivingGrass = 0
    private var counterMagicHappened = 0
    private val elementsHashMap: HashMap<Vector2d, Pair<MutableList<Animal>, MutableList<Grass>>> = hashMapOf()
    private var observedAnimal: Animal? = null

    init {
        growInitialPlantsOnMap()
    }


    override fun dayCycle(magic: Boolean): Boolean {
        var magicLoveHappened = false
        buryDeadBodies()
        everyoneMoves()
        slowlyKillAnimals()
        everyoneEats()

        if (magic && counterMagicHappened < 3)
            magicLoveHappened = magicLove()
         else
            everyoneLoves()

        growPlantInJungle()
        growPlantOutsideJungle()

        day += 1

        if (magicLoveHappened) {
            counterMagicHappened += 1
            return true
        }
        return false
    }

    // Places animal on map such that list of animals are sorted by energy.
    override fun place(animal: Animal): Boolean {
        if (animalNames.containsKey(animal.name))
            throw IllegalArgumentException("Animals name must be unique.")
        if (!(animal.position follows upperLeft && animal.position precedes lowerRight))
            throw IllegalArgumentException("Animal can't be placed at ${animal.position}. " + "It is outside map.")

        nOfLivingAnimals += 1
        animalNames[animal.name] = true
        animal.addObserver(this::positionObserverFunc)

        if (elementsHashMap.containsKey(animal.position)) {
            elementsHashMap[animal.position]?.first?.insertInSortedOrder(animal)
            allAnimals.add(animal)
        } else {
            elementsHashMap[animal.position] = Pair(mutableListOf(), mutableListOf())
            elementsHashMap[animal.position]?.first?.add(animal)
            allAnimals.add(animal)
        }
        return true
    }


    override fun objectAt(position: Vector2d): Pair<MutableList<Animal>, MutableList<Grass>>? {
        if (isAnythingAt(position)) return elementsHashMap[position]
        return null
    }

    override fun setAnimalToObserve(animal: Animal) {
        observedAnimal = animal
    }

    override fun positionInJungle(position: Vector2d) =
        position follows jungleUpperLeft && position precedes jungleLowerRight

    override fun getNOfLivingAnimals(): Int = nOfLivingAnimals

    override fun getNOfGrasses(): Int = nOfLivingGrass

    override fun getGenotypesMode(): MoveDirection? {
        val aliveAnimals = getAliveAnimals()
        val globalGenesCounter = hashMapOf<MoveDirection, Int>()
        aliveAnimals.forEach { animal ->
            animal.genesCounter.forEach { (key, value) ->
                globalGenesCounter[key] = globalGenesCounter.getOrDefault(key, 0) + value
            }
        }
        var maxValue = 0
        var maxDirection: MoveDirection? = null
        globalGenesCounter.forEach { (t, u) ->
            if (u > maxValue) {
                maxDirection = t
                maxValue = u
            }
        }
        return maxDirection
    }

    override fun getAvgAnimalEnergy(): Float {
        val aliveAnimals = getAliveAnimals()
        var energySum = 0F

        aliveAnimals.forEach { energySum += it.energy }
        return energySum.div(aliveAnimals.size.toFloat())
    }

    override fun getAvgLifetime(): Float? {
        var deadAnimalsSumOfAge = 0F
        val deadAnimals = allAnimals.filter { !it.alive }
        if (deadAnimals.isEmpty())
            return null

        deadAnimals.forEach { deadAnimalsSumOfAge += it.dayOfDeath }
        return deadAnimalsSumOfAge.div(deadAnimals.size.toFloat())
    }

    override fun getAvgNumberOfKids(): Float {
        val aliveAnimals = getAliveAnimals()
        var sumOfKids = 0

        aliveAnimals.forEach { sumOfKids += it.numberOfKids }
        return sumOfKids.div(aliveAnimals.size.toFloat())
    }


    override fun nextAnimalName(): String {
        nOfAnimals += 1
        return nOfAnimals.toString()
    }

    override fun isAnythingAt(position: Vector2d): Boolean {
        if (elementsHashMap.containsKey(position))
            return elementsHashMap[position]?.first?.isNotEmpty() == true ||
                    elementsHashMap[position]?.second?.isNotEmpty() == true
        return false
    }

    private fun isAnimalAt(position: Vector2d): Boolean {
        if (elementsHashMap.containsKey(position))
            if (elementsHashMap[position]?.first?.isNotEmpty() == true)
                return true
        return false
    }

    private fun positionObserverFunc(old: Pair<Vector2d, String>, new: Vector2d) {
        val position = old.first
        val name = old.second
        val animal: Animal? = elementsHashMap[position]?.first?.find { animal -> animal.name == name }
        elementsHashMap[position]?.first?.removeIf { it -> it.name == name }

        if (animal != null) {
            if (elementsHashMap.containsKey(new)) {
                elementsHashMap[new]?.first?.insertInSortedOrder(animal)
            } else {
                elementsHashMap[new] = Pair(mutableListOf(), mutableListOf())
                elementsHashMap[new]?.first?.add(animal)
            }
        } else throw IllegalStateException("Problem with logic of a program")
    }

    private fun growPlantInJungle() {
        for (i in 1..10)  // Has 10 tries to grow plant, so it could happen that the plant won't grow.
            if (placeGrassAt(getRandomPosition(jungleUpperLeft, jungleLowerRight)))
                return

    }

    private fun growPlantOutsideJungle() {
        for (i in 1..10) {  // Has 10 tries to place a grass, so it could happen that the plant won't grow.
            val newPosition = getRandomPosition(upperLeft, lowerRight)
            if (newPosition follows jungleUpperLeft && newPosition precedes jungleLowerRight)
                continue
            if (placeGrassAt(newPosition))
                return

        }
    }

    private fun everyoneMoves() {
        allAnimals.forEach { animal -> if (animal.alive) animal.move() }
    }

    private fun everyoneEats() {
        elementsHashMap.values.forEach {
            if (it.second.isNotEmpty() && it.first.isNotEmpty()) {
                var nOfAnimalsToSplit = 1

                nOfLivingGrass -= 1

                for (i in it.first.size - 2 downTo 0) {
                    if (it.first[i].energy == it.first[i + 1].energy) {
                        nOfAnimalsToSplit += 1
                    }
                }
                val energyPerAnimal: Float = it.second[0].energy.div(nOfAnimalsToSplit)
                for (i in (it.first.size - 1) downTo it.first.size - nOfAnimalsToSplit) {
                    it.first[i].eat(energyPerAnimal)
                }
                it.second.removeAt(0)
                it.first.sortBy { it.energy }
            }
        }
    }

    private fun buryDeadBodies() {
        elementsHashMap.values.forEach {
            it.first.forEach { animal ->
                if (animal.energy <= 0) {
                    nOfLivingAnimals -= 1
                    animal.alive = false
                    animal.dayOfDeath = day
                }
            }
            elementsHashMap.values.forEach { it.first.removeIf { animal -> !animal.alive } }
        }
    }

    private fun slowlyKillAnimals() {
        elementsHashMap.values.forEach {
            it.first.forEach { it.oneDayCloserToDeath(dayCostEnergy) }
        }
    }


    private fun everyoneLoves() {
        elementsHashMap.values.forEach {
            if (it.first.size > 1) {
                var isAncestorOfObservedAnimal = false
                val strongestAnimal: Animal = it.first[it.first.size - 1]
                val prettiestAnimal: Animal = it.first[it.first.size - 2]
                val kidEnergy: Float = strongestAnimal.energy / 4F + prettiestAnimal.energy / 4F
                val kidGenes = strongestAnimal loves prettiestAnimal
                if (kidGenes != null) {

                    if (strongestAnimal == observedAnimal || prettiestAnimal == observedAnimal) {
                        observedAnimal?.observedNOfKids = observedAnimal?.observedNOfKids?.plus(1)!!
                        observedAnimal?.observedNofDescendants = observedAnimal?.observedNofDescendants?.plus(1)!!
                        isAncestorOfObservedAnimal = true
                    } else if (strongestAnimal.isDescendantOfObservedAnimal || prettiestAnimal.isDescendantOfObservedAnimal) {
                        observedAnimal?.observedNofDescendants = observedAnimal?.observedNofDescendants?.plus(1)!!
                        isAncestorOfObservedAnimal = true
                    }
                    val kid =
                        Animal(
                            nextAnimalName(),
                            strongestAnimal.position,
                            kidEnergy,
                            kidGenes,
                            minLoveEnergy,
                            isAncestorOfObservedAnimal,
                            this
                        )

                    strongestAnimal.increaseKidsCounter()
                    prettiestAnimal.increaseKidsCounter()

                    this.place(kid)
                }
                it.first.sortBy { it.energy }
            }
        }
    }

    private fun magicLove(): Boolean {
        val aliveAnimals = getAliveAnimals()
        if (aliveAnimals.size == 5) {
            for (j in 0..4) {
                for (i in 0..20) { // has 21 tries to find position that is free
                    val newPosition = getRandomPosition(upperLeft, lowerRight)
                    if (!isAnimalAt(newPosition) || i == 4) {
                        this.place(
                            Animal(
                                this.nextAnimalName(), newPosition, animalInitialEnergy, aliveAnimals[j].genes,
                                minLoveEnergy, false, this
                            )
                        )
                        break
                    }
                }
            }
            return true
        }
        return false
    }

    private fun getAliveAnimals(): List<Animal> {
        return allAnimals.filter { it.alive }
    }


    private fun placeGrassAt(position: Vector2d): Boolean {
        return if (elementsHashMap.containsKey(position)) {
            if (elementsHashMap[position]?.second?.isEmpty() == true) {
                elementsHashMap[position]?.second?.add(Grass(position, grassEnergy))
                nOfLivingGrass += 1
                true
            } else
                false
        } else {
            elementsHashMap[position] = Pair(mutableListOf(), mutableListOf())
            elementsHashMap[position]?.second?.add(Grass(position, grassEnergy))
            nOfLivingGrass += 1
            true
        }
    }

    private fun getRandomPosition(lowerLeft: Vector2d, upperRight: Vector2d) = Vector2d(
        (lowerLeft.x..upperRight.x).random(), (lowerLeft.y..upperRight.y).random()
    )

    private fun MutableList<Animal>.insertInSortedOrder(animal: Animal) {
        var indexToInsert = elementsHashMap[animal.position]?.first?.binarySearch(
            animal, EnergyComparator()
        )
        if (indexToInsert != null) {
            if (indexToInsert < 0)
                indexToInsert = (-indexToInsert) - 1
            this.add(indexToInsert, animal)
        } else
            throw IllegalStateException("Binary search returned null in insertInSortedOrder")
    }

    private fun growInitialPlantsOnMap() {
        var i = 0
        while (i != jungleNumberOfPlants) {
            if (placeGrassAt(getRandomPosition(jungleUpperLeft, jungleLowerRight)))
                i += 1
        }
        while (i != this.nOfPlants) {
            if (placeGrassAt(getRandomPosition(upperLeft, lowerRight)))
                i += 1
        }
    }

}