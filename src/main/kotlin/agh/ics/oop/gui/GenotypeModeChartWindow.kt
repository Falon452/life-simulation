package agh.ics.oop.gui

import agh.ics.oop.enumClasses.MoveDirection
import agh.ics.oop.enumClasses.MoveDirection.*
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.scene.Scene
import javafx.scene.chart.*
import javafx.scene.layout.Pane
import javafx.stage.Stage
import kotlin.math.max


class GenotypeModeChartWindow(
    chartTitle: String,
    xAxisTitle: String,
    yAxisTitle: String,
){

    private val window = Stage()
    private val paneView = Pane()
    private val scene = Scene(paneView)
    private val xAxis = NumberAxis(0.0, 5.0, 1.0)
    private val yAxis = CategoryAxis(
        FXCollections.observableArrayList(listOf(
        FORWARD.toString(), LIL_RIGHT.toString(), RIGHT.toString(), MAX_RIGHT.toString(),
        BACKWARD.toString(), MAX_LEFT.toString(), LEFT.toString(), LIL_LEFT.toString())))
    private val chart = ScatterChart(xAxis, yAxis)
    val series = XYChart.Series<Number, String>()

    private var xUpperBound = 5.0


    init{
        xAxis.label = xAxisTitle
        yAxis.label = yAxisTitle
        chart.title = chartTitle
        window.title = chartTitle
    }

    fun display(){
        Platform.runLater {
            paneView.children.clear()
            xUpperBound = max((series.data.last().xValue).toDouble(), xUpperBound)
            xAxis.upperBound = xUpperBound
            xAxis.lowerBound = series.data.first().xValue.toDouble()
            val chart = LineChart(xAxis, yAxis)
            chart.data.add(series)

            paneView.children.add(chart)

            window.scene = scene
            window.show()
        }
        Platform.runLater { System.gc() }
    }

    fun addEntry(x: Number, y: MoveDirection){
        Platform.runLater {
            series.data.add(XYChart.Data(x, y.toString()))
        }
    }
}