package agh.ics.oop.classes


import agh.ics.oop.interfaces.IElement
import javafx.scene.image.Image
import java.io.FileInputStream


class Grass(initPosition: Vector2d, energy: Float): IElement {
    companion object{
        fun grassImage() = Image(FileInputStream("src/main/resources/grass.png"))
    }

    override val position = initPosition
    override var energy: Float = energy

    override fun toString(): String {
        return "*"
    }
}