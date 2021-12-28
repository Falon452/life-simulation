package agh.ics.oop.gui

import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.layout.Pane
import javafx.stage.Stage
import kotlin.math.max


class LineChartWindow(private val chartTitle: String,
                      private val xAxisTitle: String,
                      private val yAxisTitle: String,){

    private val window = Stage()
    private val paneView = Pane()
    private val scene = Scene(paneView)

    private val xAxis = NumberAxis(1.0, 5.0, 1.0)
    private val yAxis = NumberAxis(0.0, 5.0, 1.0)
    private val chart = LineChart(xAxis, yAxis)
    val series = XYChart.Series<Number, Number>()

    private var xUpperBound = 5.0
    private var yUpperBound = 5.0

    init{
        xAxis.label = xAxisTitle
        yAxis.label = yAxisTitle
        chart.title = chartTitle
        window.title = chartTitle
    }

    fun display(){
        Platform.runLater {
            paneView.children.clear()
            if (series.data.size > 0) {
                xUpperBound = max((series.data.last().xValue).toDouble() + 1, xUpperBound)
                yUpperBound = max((series.data.last().yValue).toDouble() + 2, yUpperBound)
                xAxis.lowerBound = series.data.first().xValue as Double
                xAxis.upperBound = xUpperBound
                yAxis.upperBound = yUpperBound
            }

            val chart = LineChart(xAxis, yAxis)
            chart.data.add(series)
            paneView.children.add(chart)

            window.scene = scene
            window.show()
        }
        Platform.runLater { System.gc() }
    }

    fun addEntry(x: Number, y: Number){
        Platform.runLater {
            series.data.add(XYChart.Data(x, y))
        }
    }
}