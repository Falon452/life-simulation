package agh.ics.oop.classes

import javafx.scene.image.Image
import java.io.FileInputStream

class Desert {
    companion object {
        fun desertImage() = Image(FileInputStream("src/main/resources/blankDesert.png"))
    }
}