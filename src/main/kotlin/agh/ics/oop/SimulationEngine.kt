package agh.ics.oop

import agh.ics.oop.classes.Animal
import agh.ics.oop.classes.GenesGenerator
import agh.ics.oop.interfaces.IWorldMap
import agh.ics.oop.classes.Vector2d
import agh.ics.oop.gui.SimulationWindow

typealias BoolObserverFunc = (Boolean) -> Unit

class SimulationEngine(private val width: Int,
                       private val height: Int,
                       private val numberOfPlants: Int,
                       private val grassEnergy: Float,
                       private val minimumEnergyForLove: Float,
                       private val animalEnergy: Float,
                       private val initialPositions: List<Vector2d>,
                       private val magic: Boolean,
                       private val map: IWorldMap): Runnable {

    private val genesGenerator = GenesGenerator()
    private var running = false
    private var oneDayMode = false
    private val simulationWindow = if (map is FoldableMap) SimulationWindow("Foldable Map", width, height, map)
        else SimulationWindow("Concrete Map", width, height, map)


    init {
        initialPositions.forEach {position ->

            map.place(
                Animal(
                map.nextAnimalName(),
                position,
                animalEnergy,
                genesGenerator.getRandomGenes(),
                minimumEnergyForLove,
                false,
                map
                )
            ) }

        simulationWindow.addRunningObserver(this::runningObserverFunc)
        simulationWindow.addOneDayMoveObserver(this::oneDayModeObserverFunc)

        simulationWindow.display(false)

    }


    override fun run() {
        while (true) {
            while (running) {

                val magicHappened = map.dayCycle(magic)
                simulationWindow.display(magicHappened)
                Thread.sleep(250L)
            }
            if (oneDayMode){
                val magicHappened = map.dayCycle(magic)
                simulationWindow.display(magicHappened)
            }
            Thread.sleep(50L)

        }
    }

    private fun runningObserverFunc(new: Boolean){
        running = new
    }
    private fun oneDayModeObserverFunc(new: Boolean){
        oneDayMode = new
    }


}