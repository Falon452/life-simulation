package agh.ics.oop.gui

import agh.ics.oop.classes.Animal
import agh.ics.oop.classes.Grass
import agh.ics.oop.enumClasses.MapDirection.*
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import java.io.FileInputStream

class ElementBox(elements: Pair<MutableList<Animal>, MutableList<Grass>>?, inJungle: Boolean) {
    val grid = GridPane()
    private val fontSize = 8.0
    private val font = Font(fontSize)

    init {
        grid.minWidth = 48.0
        grid.maxWidth = 48.0
        grid.minHeight = 48.0
        grid.maxHeight = 48.0

        if (inJungle)
            grid.background = Background(BackgroundFill(Color.FORESTGREEN, null, null))
        else
            grid.background = Background(BackgroundImage(Image(FileInputStream("src/main/resources/blankDesert.png")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT))

        elements?.first?.reversed()?.forEachIndexed { index, animal ->
            val imgView: ImageView = when (animal.orientation){
                NORTH -> {
                    ImageView(Animal.northImage())
                }
                NORTH_EAST -> {
                    ImageView(Animal.northEastImage())
                }
                EAST -> {
                    ImageView(Animal.eastImage())
                }
                SOUTH_EAST ->{
                    ImageView(Animal.southEastImage())
                }
                SOUTH -> {
                    ImageView(Animal.southImage())
                }
                SOUTH_WEST -> {
                    ImageView(Animal.southWestImage())
                }
                WEST -> {
                    ImageView(Animal.westImage())
                }
                NORTH_WEST -> {
                    ImageView(Animal.northWestImage())
                }
            }

            val energyLabel = Label((animal.energy.toInt()).toString())
            energyLabel.font = font

            val vBox = VBox()
            vBox.minWidth = 12.0
            vBox.maxWidth = 12.0
            vBox.maxHeight = 24.0
            vBox.minHeight = 24.0
            vBox.children.add(imgView)
            vBox.children.add(energyLabel)
            grid.add(vBox, index, 0)
        }

        elements?.second?.forEach { _ ->
            val imgView = ImageView(Grass.grassImage())
            imgView.fitHeight = 12.0
            imgView.fitWidth = 48.0

            val fillUpSpaceLabel = Label("")
            fillUpSpaceLabel.minWidth = 48.0
            fillUpSpaceLabel.maxWidth = 48.0
            fillUpSpaceLabel.minHeight = 36.0
            fillUpSpaceLabel.maxHeight = 36.0

            if (grid.rowCount == 0){
                grid.add(fillUpSpaceLabel, 0, 0)
            }

            grid.add(imgView, 0, 1)
        }
    }

}