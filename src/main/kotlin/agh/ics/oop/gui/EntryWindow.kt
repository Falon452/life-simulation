package agh.ics.oop.gui

import agh.ics.oop.classes.PositionParser
import agh.ics.oop.classes.Vector2d
import agh.ics.oop.FoldableMap
import agh.ics.oop.interfaces.ISettings
import agh.ics.oop.interfaces.IWorldMap
import agh.ics.oop.maps.ConcreteMap
import agh.ics.oop.settings.DefaultSettings
import agh.ics.oop.SimulationEngine
import javafx.application.Application
import javafx.stage.Stage
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.scene.text.Text


class EntryWindow: Application(){
    private var width: Int? = null
    private var height: Int? = null
    private var nOfPlants: Int? = null
    private var grassEnergy: Float? = null
    private var minLoveEnergy: Float? = null
    private var animalEnergy: Float? = null
    private var dayCostEnergy: Float? = null
    private var initialPositions: List<Vector2d>? = null

    private lateinit var widthTF: TextField
    private lateinit var heightTF: TextField
    private lateinit var nOfPlantsTF: TextField
    private lateinit var grassEnergyTF: TextField
    private lateinit var minLoveEnergyTF: TextField
    private lateinit var dayCostEnergyTF: TextField
    private lateinit var animalEnergyTF: TextField
    private lateinit var initialPositionsTF: TextField

    private lateinit var widthErrorText: Text
    private lateinit var heightErrorText: Text
    private lateinit var nOfPlantsErrorText: Text
    private lateinit var grassEnergyErrorText: Text
    private lateinit var minLoveEnergyErrorText: Text
    private lateinit var animalEnergyErrorText: Text
    private lateinit var dayCostEnergyErrorText: Text
    private lateinit var initialPositionsErrorText: Text
    private lateinit var foldableErrorText: Text
    private lateinit var concreteErrorText: Text

    override fun start(window: Stage) {
        val fxmlLoader= FXMLLoader( EntryWindow::class.java.getResource("/entryWindow.fxml"))
        val root: Parent = fxmlLoader.load()
        val entryScene = Scene(root)

        widthTF = entryScene.lookup("#widthTF") as TextField
        heightTF = entryScene.lookup("#heightTF") as TextField
        nOfPlantsTF = entryScene.lookup("#nOfPlantsTF") as TextField
        grassEnergyTF = entryScene.lookup("#grassEnergyTF") as TextField
        minLoveEnergyTF = entryScene.lookup("#minLoveEnergyTF") as TextField
        dayCostEnergyTF = entryScene.lookup("#dayCostEnergyTF") as TextField
        animalEnergyTF = entryScene.lookup("#animalEnergyTF") as TextField
        initialPositionsTF = entryScene.lookup("#initialPositionsTF") as TextField
        val magicCheckBox = entryScene.lookup("#magicCheckBox") as CheckBox

        widthErrorText = entryScene.lookup("#widthErrorText") as Text
        heightErrorText = entryScene.lookup("#heightErrorText") as Text
        nOfPlantsErrorText = entryScene.lookup("#nOfPlantsErrorText") as Text
        grassEnergyErrorText = entryScene.lookup("#grassEnergyErrorText") as Text
        minLoveEnergyErrorText = entryScene.lookup("#minLoveEnergyErrorText") as Text
        animalEnergyErrorText = entryScene.lookup("#animalEnergyErrorText") as Text
        dayCostEnergyErrorText = entryScene.lookup("#dayCostEnergyErrorText") as Text
        initialPositionsErrorText = entryScene.lookup("#initialPositionsErrorText") as Text
        foldableErrorText = entryScene.lookup("#foldableErrorText") as Text
        concreteErrorText = entryScene.lookup("#concreteErrorText") as Text

        val defaultSettingsButton = entryScene.lookup("#btnDeafultSettings") as Button
        val foldableMapButton = entryScene.lookup("#btnFoldableMap") as Button
        val concreteMapButton = entryScene.lookup("#btnConcreteMap") as Button


        widthTF.setOnKeyTyped {
            width = setIntFieldOrProvideFeedback(widthTF.text.toString().toIntOrNull(), widthErrorText)
        }

        heightTF.setOnKeyTyped {
            height = setIntFieldOrProvideFeedback(heightTF.text.toString().toIntOrNull(), heightErrorText)
        }

        nOfPlantsTF.setOnKeyTyped {
            nOfPlants = setIntFieldOrProvideFeedback(nOfPlantsTF.text.toString().toIntOrNull(), nOfPlantsErrorText)
        }

        grassEnergyTF.setOnKeyTyped {
            grassEnergy = setFloatFieldOrProvideFeedback(grassEnergyTF.text.toString().toIntOrNull(), grassEnergyErrorText)
        }

        minLoveEnergyTF.setOnKeyTyped {
            minLoveEnergy = setFloatFieldOrProvideFeedback(minLoveEnergyTF.text.toString().toIntOrNull(), minLoveEnergyErrorText)
        }

        animalEnergyTF.setOnKeyTyped {
            animalEnergy = setFloatFieldOrProvideFeedback(animalEnergyTF.text.toString().toIntOrNull(), animalEnergyErrorText)
        }

        dayCostEnergyTF.setOnKeyTyped {
            dayCostEnergy = setFloatFieldOrProvideFeedback(dayCostEnergyTF.text.toString().toIntOrNull(), dayCostEnergyErrorText)
        }


        initialPositionsTF.setOnKeyTyped {
            setInitialPositionsOrProvideFeedback()
        }

        defaultSettingsButton.setOnAction {
            val settings = DefaultSettings()
            setSettings(settings)
            setAllTextFields(settings)
            clearAllErrorTexts()
        }

        foldableMapButton.setOnAction {
            if (allFieldsAreSet()) {
                startSimulation(
                    FoldableMap(width!!, height!!, nOfPlants!!, grassEnergy!!, dayCostEnergy!!,
                    minLoveEnergy!!, animalEnergy!!), magicCheckBox.isSelected)
                foldableErrorText.text = ""
            } else
                foldableErrorText.text = "You must set all fields to start simulation."
        }
        concreteMapButton.setOnAction {
            if (allFieldsAreSet()){
                startSimulation(
                    ConcreteMap(width!!, height!!, nOfPlants!!, grassEnergy!!, dayCostEnergy!!,
                    minLoveEnergy!!, animalEnergy!!), magicCheckBox.isSelected)
                concreteErrorText.text = ""
            } else
                concreteErrorText.text = "You must set all fields to start simulation."
        }

        window.title = "Life Simulation"
        window.scene = entryScene
        window.show()

    }

    private fun startSimulation(map: IWorldMap, magic: Boolean){
        val engine = SimulationEngine(width!!,height!!, nOfPlants!!, grassEnergy!!,
            minLoveEnergy!!, animalEnergy!!, initialPositions!!, magic, map)
        val engineThread = Thread(engine)
        engineThread.start()
    }

    private fun promptIfNotCorrect(input: Int?, prompt:Text): Boolean{
        if (input is Int){
            if (input > 0){
                prompt.text = ""
                return true
            }
        }
        prompt.text = "Must be integer greater than 0."
        return false
    }

    private fun setSettings(settings: ISettings){
        width = settings.width
        height = settings.height
        nOfPlants = settings.nOfPlants
        grassEnergy = settings.grassEnergy
        minLoveEnergy = settings.minLoveEnergy
        animalEnergy = settings.animalEnergy
        dayCostEnergy = settings.dayCostEnergy
        initialPositions = settings.initialPositions
    }

    private fun clearAllErrorTexts(){
        widthErrorText.text = ""
        heightErrorText.text = ""
        nOfPlantsErrorText.text = ""
        grassEnergyErrorText.text = ""
        minLoveEnergyErrorText.text = ""
        animalEnergyErrorText.text = ""
        dayCostEnergyErrorText.text = ""
        initialPositionsErrorText.text = ""
        foldableErrorText.text = ""
        concreteErrorText.text = ""
    }

    private fun allFieldsAreSet(): Boolean{
        return width != null && height != null && nOfPlants != null && grassEnergy != null &&
                grassEnergy != null && minLoveEnergy != null && animalEnergy != null &&
                initialPositions != null
    }

    private fun setAllTextFields(settings: ISettings){
        widthTF.text = "${settings.width}"
        heightTF.text = "${settings.height}"
        nOfPlantsTF.text = "${settings.nOfPlants}"
        grassEnergyTF.text = "${settings.width}"
        minLoveEnergyTF.text = "${settings.minLoveEnergy}"
        animalEnergyTF.text = "${settings.animalEnergy}"
        dayCostEnergyTF.text = "${settings.dayCostEnergy}"
        var positionsStr = ""
        settings.initialPositions.forEach { pos -> positionsStr += "$pos " }
        initialPositionsTF.text = positionsStr
    }

    private fun setFloatFieldOrProvideFeedback(inputDayCostEnergy: Int?, dayCostEnergyErrorText: Text): Float? {
        return if (promptIfNotCorrect(inputDayCostEnergy, dayCostEnergyErrorText))
            inputDayCostEnergy!!.toFloat() else null
    }

    private fun setIntFieldOrProvideFeedback(inputDayCostEnergy: Int?, dayCostEnergyErrorText: Text): Int? {
        return if (promptIfNotCorrect(inputDayCostEnergy, dayCostEnergyErrorText))
            inputDayCostEnergy!! else null
    }

    private fun setInitialPositionsOrProvideFeedback(){
        if (width == null || height == null)
            initialPositionsErrorText.text = "Please set width and height first."
        else {
            val input: String = initialPositionsTF.text.toString()
            val positions = PositionParser().parse(input)
            if (positions != null){
                val upperRight = Vector2d(height!! -1, width!! -1)
                var incorrectPosition = false
                for (i in positions.indices) {
                    if (!(positions[i] follows Vector2d(0, 0) && positions[i] precedes upperRight)){
                        incorrectPosition = true
                        break
                    }
                }
                if (incorrectPosition)
                    initialPositionsErrorText.text = "Positions must be between (0,0) and $upperRight"
                else {
                    initialPositions = positions
                    initialPositionsErrorText.text = ""
                }
            } else{
                val msg = "Correct format is: (1,2) (3,6) (4,6) ... . Make sure that there are no additional spaces"
                initialPositionsErrorText.text = msg
            }
        }
    }
}
