package agh.ics.oop.gui

import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.stage.Stage

class AlertMagicHappened {
    fun display(magicHappenedCounter: Int){
        val window = Stage()

        when ((1..3).random()){
            1 -> window.title = "Yennefer of Vengerberg"
            2 -> window.title = "Triss Nerigold"
            3 -> window.title = "Cirilla Fiona Elen Riannon"
        }

        window.minWidth = 250.0

        val label = Label("Magic happened!!! Magic Counter: $magicHappenedCounter")
        val layout = VBox(10.0)
        layout.children.add(label)
        layout.alignment = Pos.CENTER

        val scene = Scene(layout)
        window.scene = scene
        window.show()
    }
}