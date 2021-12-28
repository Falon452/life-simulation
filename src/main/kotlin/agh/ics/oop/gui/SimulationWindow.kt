package agh.ics.oop.gui

import agh.ics.oop.classes.Animal
import agh.ics.oop.classes.Vector2d
import agh.ics.oop.interfaces.IWorldMap
import agh.ics.oop.enumClasses.MoveDirection
import agh.ics.oop.statistics.StatsWriter


import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.chart.BarChart
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.scene.layout.*
import javafx.scene.text.Text
import javafx.stage.Stage
import kotlin.properties.Delegates

typealias BoolObserverFunc = (Boolean) -> Unit

class SimulationWindow(title: String, private val  width: Int,private val height: Int, private val map: IWorldMap){

    private val window = Stage()
    private var scene: Scene

    private lateinit var runningObserver: BoolObserverFunc
    private lateinit var oneDayModeObserver: BoolObserverFunc

    private var running by Delegates.observable(false){_, _, new -> runningObserver(new) }
    private var oneDayMode by Delegates.observable(false){_, _, new -> oneDayModeObserver(new) }

    private var magicCounter = 0
    private var day = 0.0

    private var aliveAnimalsChart: LineChartWindow? = null
    private var nOfGrassChart: LineChartWindow? = null
    private var genotypesModeChart:GenotypeModeChartWindow? = null
    private var avgEnergyChart: LineChartWindow? = null
    private var avgLifetimeChart: LineChartWindow? = null
    private var avgNOfKidsChart: LineChartWindow? = null
    private var observedAnimal: Animal? = null

    init {
        val fxmlLoader = FXMLLoader(SimulationWindow::class.java.getResource("/simulationWindow.fxml"))
        val root: Parent = fxmlLoader.load()
        scene = Scene(root, 1300.0, 800.0)

        val startButton = scene.lookup("#btnStart") as Button
        val stopButton = scene.lookup("#btnStop") as Button
        val oneDayButton = scene.lookup("#btnOneDay") as Button
        val saveButton = scene.lookup("#saveButton") as Button

        startButton.setOnAction {
            running = true
        }
        stopButton.setOnAction {
            running = false
        }
        oneDayButton.setOnAction {
            oneDayMode = true
            Thread.sleep(55)
            oneDayMode = false
        }
        saveButton.setOnAction {
            if (filenameTextField.text.isNotEmpty() &&
                (aliveAnimalsChart != null || nOfGrassChart != null || genotypesModeChart != null ||
                        avgEnergyChart != null || avgLifetimeChart != null || avgNOfKidsChart != null)){

                StatsWriter(
                    aliveAnimalsChart?.series, nOfGrassChart?.series, genotypesModeChart?.series,
                    avgEnergyChart?.series, avgLifetimeChart?.series, avgNOfKidsChart?.series
                ).write(filenameTextField.text)
            }
        }

        window.title = title
        window.scene = scene
        window.show()
    }

    private var tilePane: TilePane = scene.lookup("#tilePane") as TilePane
    private val aliveAnimalsCheckBox = scene.lookup("#aliveAnimalsCheckBox") as CheckBox
    private val nOfGrassCheckBox = scene.lookup("#nOfGrassCheckBox") as CheckBox
    private val genotypesModeCheckBox = scene.lookup("#genotypesModeCheckBox") as CheckBox
    private val avgEnergyCheckBox = scene.lookup("#avgEnergyCheckBox") as CheckBox
    private val avgLifetimeCheckBox = scene.lookup("#avgLifetimeCheckBox") as CheckBox
    private val avgNOfKidsCheckBox = scene.lookup("#avgNOfKidsCheckBox") as CheckBox
    private val rightVBox = scene.lookup("#rightVBox") as VBox
    private val dayNumberText = scene.lookup("#dayNumberText") as Text
    private val filenameTextField = scene.lookup("#filenameTextField") as TextField

    init {
        tilePane.prefRows = height
        tilePane.prefColumns = width
        tilePane.minWidth = 48.0 * width
        tilePane.minHeight = 48.0 * height
        tilePane.maxHeight = 48.0 * height
        tilePane.maxWidth = 48.0 * width
        tilePane.prefTileHeight = 48.0
        tilePane.prefTileWidth = 48.0

        aliveAnimalsCheckBox.setOnMouseClicked {
            aliveAnimalsHandler()
        }
        nOfGrassCheckBox.setOnMouseClicked {
            nOfGrassHandler()
        }
        genotypesModeCheckBox.setOnMouseClicked {
            genotypesModeHandler()
        }
        avgLifetimeCheckBox.setOnMouseClicked {
            avgLifetimeHandler()
        }
        avgNOfKidsCheckBox.setOnMouseClicked {
            avgNOfKidsHandler()
        }
        avgEnergyCheckBox.setOnMouseClicked {
            avgEnergyHandler()
        }
    }

    fun display(magicHappened: Boolean) {
        day += 1

        Platform.runLater {
            dayNumberText.text = "Day: ${day.toInt()}"

            aliveAnimalsHandler()
            nOfGrassHandler()
            genotypesModeHandler()
            avgLifetimeHandler()
            avgNOfKidsHandler()
            avgEnergyHandler()

            if (magicHappened){
                magicCounter += 1
                AlertMagicHappened().display(magicCounter)
            }

            tilePane.children.clear()

            for (i in 0 until height) {
                for (j in 0 until width) {
                    tilePane.children.add(BlankView().imgView)
                }
            }

            for (i in 0 until height) {
                for (j in 0 until width) {
                    val position = Vector2d(i, j)
                    val elements = map.objectAt(position)
                    val gridPane = ElementBox(elements, map.positionInJungle(position)).grid

                    if (elements?.first?.isNotEmpty() == true){
                        when(elements.first.size) {
                            1 -> gridPane.children[0].setOnMouseClicked { setAnimalToObserve(elements.first[0]) }
                            2 -> {
                                gridPane.children[0].setOnMouseClicked { setAnimalToObserve(elements.first[0]) }
                                gridPane.children[1].setOnMouseClicked { setAnimalToObserve(elements.first[1]) }
                            }
                            3 -> {
                                gridPane.children[0].setOnMouseClicked { setAnimalToObserve(elements.first[0]) }
                                gridPane.children[1].setOnMouseClicked { setAnimalToObserve(elements.first[1]) }
                                gridPane.children[2].setOnMouseClicked { setAnimalToObserve(elements.first[2]) }
                            }
                            4 -> {
                                gridPane.children[0].setOnMouseClicked { setAnimalToObserve(elements.first[0]) }
                                gridPane.children[1].setOnMouseClicked { setAnimalToObserve(elements.first[1]) }
                                gridPane.children[2].setOnMouseClicked { setAnimalToObserve(elements.first[2]) }
                                gridPane.children[3].setOnMouseClicked { setAnimalToObserve(elements.first[3]) }
                            }
                        }
                    }

                    tilePane.children[i * width + j] =  gridPane
                }
            }

            updateObservedAnimal()

            window.scene = scene
            window.show()
        }

        Platform.runLater { System.gc() }
    }

    fun addRunningObserver(observer: BoolObserverFunc) {
        runningObserver = observer
    }
    fun addOneDayMoveObserver(observer: BoolObserverFunc){
        oneDayModeObserver = observer
    }

    private fun setAnimalToObserve(animal: Animal){
        map.setAnimalToObserve(animal)
        observedAnimal = animal

        Platform.runLater {
            rightVBox.children.clear()

            val xAxis = CategoryAxis(
                FXCollections.observableArrayList(listOf(
                    MoveDirection.FORWARD.toString(), MoveDirection.LIL_RIGHT.toString(),
                    MoveDirection.RIGHT.toString(), MoveDirection.MAX_RIGHT.toString(),
                    MoveDirection.BACKWARD.toString(), MoveDirection.MAX_LEFT.toString(),
                    MoveDirection.LEFT.toString(), MoveDirection.LIL_LEFT.toString())))
            val yAxis = NumberAxis(0.0, 32.0, 1.0)
            val series = XYChart.Series<String, Number>()
            val genesChart = BarChart(xAxis,yAxis)

            observedAnimal?.genesCounter?.forEach { (gene, value) ->
                series.data.add(XYChart.Data(gene.toString(), value))}

            genesChart.data.add(series)

            rightVBox.children.add(genesChart)
            rightVBox.children.add(Text())
            rightVBox.children.add(Text())
            rightVBox.children.add(Text())
            rightVBox.minWidth = 300.0
        }

    }

    private fun updateObservedAnimal(){
        if (observedAnimal != null){
            (rightVBox.children[1] as Text).text = "Kids: "+observedAnimal?.observedNOfKids.toString()
            (rightVBox.children[2] as Text).text = "Descendants: "+observedAnimal?.observedNofDescendants.toString()
            if (observedAnimal?.alive == false && (rightVBox.children[3] as Text).text.isEmpty())
                (rightVBox.children[3] as Text).text = "Day of death: ${day.toInt()}"
        }
    }

    private fun aliveAnimalsHandler(){
        if (aliveAnimalsCheckBox.isSelected){
            if (aliveAnimalsChart == null)
                aliveAnimalsChart = LineChartWindow(
                    "Number Of All Alive Animals",
                    "Day",
                    "Alive animals")
            aliveAnimalsChart?.addEntry(day, map.getNOfLivingAnimals())
            aliveAnimalsChart?.display()
        } else
            aliveAnimalsChart = null
    }

    private fun nOfGrassHandler(){
        if (nOfGrassCheckBox.isSelected) {
            if (nOfGrassChart == null)
                nOfGrassChart = LineChartWindow(
                    "Number Of Grass",
                    "Day",
                    "Number of grass"
                )
            nOfGrassChart?.addEntry(day, map.getNOfGrasses())
            nOfGrassChart?.display()
        } else
            nOfGrassChart = null
    }

    private fun genotypesModeHandler(){
        if (genotypesModeCheckBox.isSelected) {
            if (genotypesModeChart == null)
                genotypesModeChart = GenotypeModeChartWindow(
                    "Genotypes Mode",
                    "Day",
                    "Genotypes mode"
                )
            val genotypeMode = map.getGenotypesMode()
            if (genotypeMode != null) {
                genotypesModeChart?.addEntry(day, genotypeMode)
                genotypesModeChart?.display()
            }
        } else
            genotypesModeChart = null
    }

    private fun avgEnergyHandler(){
        if (avgEnergyCheckBox.isSelected){
            if (avgEnergyChart == null)
                avgEnergyChart = LineChartWindow(
                    "Average Animal Energy",
                    "Day",
                    "Average animal energy")
            avgEnergyChart?.addEntry(day, map.getAvgAnimalEnergy())
            avgEnergyChart?.display()
        } else
            avgEnergyChart = null
    }

    private fun avgLifetimeHandler(){
        if (avgLifetimeCheckBox.isSelected){
            if (avgLifetimeChart == null)
                avgLifetimeChart = LineChartWindow(
                    "Average Animal Lifetime",
                    "Day",
                    "Average animal lifetime")
            map.getAvgLifetime()?.let { avgLifetimeChart?.addEntry(day, it) }
            avgLifetimeChart?.display()
        } else
            avgLifetimeChart = null
    }

    private fun avgNOfKidsHandler(){
        if (avgNOfKidsCheckBox.isSelected){
            if (avgNOfKidsChart == null)
                avgNOfKidsChart = LineChartWindow(
                    "Average Number Of Kids For Living Animals",
                    "Day",
                    "Average number of kids for living animals")
            avgNOfKidsChart?.addEntry(day, map.getAvgNumberOfKids())
            avgNOfKidsChart?.display()
        } else
            avgNOfKidsChart = null
    }

}