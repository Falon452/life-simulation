package agh.ics.oop.gui

import agh.ics.oop.classes.Desert
import javafx.scene.image.ImageView

class BlankView {
    val imgView = ImageView(Desert.desertImage())
    init{
        imgView.fitWidth = 48.0
        imgView.fitHeight = 48.0
    }
}