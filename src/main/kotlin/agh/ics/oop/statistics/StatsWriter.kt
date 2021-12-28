package agh.ics.oop.statistics


import javafx.scene.chart.XYChart
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.BufferedWriter
import java.io.FileWriter




class StatsWriter(
    aliveAnimalsSeries: XYChart.Series<Number, Number>? = null,
    nOfGrassSeries: XYChart.Series<Number, Number>? = null,
    genotypesModeSeries: XYChart.Series<Number, String>? = null,
    avgEnergySeries: XYChart.Series<Number, Number>? = null,
    avgLifeTimeSeries: XYChart.Series<Number, Number>? = null,
    avgNOfKidsSeries: XYChart.Series<Number, Number>? = null,
){
    data class ChartStats(
        val day: Int?,
        var aliveAnimals: Int? = null,
        var nOfGrass: Int? = null,
        var genotypesMode: String? = null,
        var avgEnergy: Float? = null,
        var avgLifeTime: Float? = null,
        var avgNOfKids: Float? = null
    )

    private val firstDay: Int = listOf(
        if (aliveAnimalsSeries != null ) aliveAnimalsSeries.data.first().xValue else 10e4,
        if (nOfGrassSeries != null ) nOfGrassSeries.data.first().xValue else 10e4,
        if (genotypesModeSeries != null ) genotypesModeSeries.data.first().xValue else 10e4,
        if (avgEnergySeries != null ) avgEnergySeries.data.first().xValue else 10e4,
        if (avgLifeTimeSeries != null ) avgLifeTimeSeries.data.first().xValue else 10e4,
        if (avgNOfKidsSeries != null ) avgNOfKidsSeries.data.first().xValue else 10e4,
    ).minOf {it.toInt() }

    private val lastDay: Int = listOf(
        if (aliveAnimalsSeries != null ) aliveAnimalsSeries.data.last().xValue else 1,
        if (nOfGrassSeries != null ) nOfGrassSeries.data.last().xValue else 1,
        if (genotypesModeSeries != null ) genotypesModeSeries.data.last().xValue else 1,
        if (avgEnergySeries != null ) avgEnergySeries.data.last().xValue else 1,
        if (avgLifeTimeSeries != null ) avgLifeTimeSeries.data.last().xValue else 1,
        if (avgNOfKidsSeries != null ) avgNOfKidsSeries.data.last().xValue else 1,
    ).maxOf {it.toInt() }


    private val allEntries = mutableListOf<ChartStats>()

    init {
        for (day in firstDay..lastDay)
            allEntries.add(ChartStats(day = day))

        aliveAnimalsSeries?.data?.forEach {
            val index = (it.xValue.toInt() -  firstDay)
            allEntries[index].aliveAnimals = it.yValue.toInt()
        }

        nOfGrassSeries?.data?.forEach {
            val index = (it.xValue.toInt() -  firstDay)
            allEntries[index].nOfGrass = it.yValue.toInt()
        }

        genotypesModeSeries?.data?.forEach {
            val index = (it.xValue.toInt() -  firstDay)
            allEntries[index].genotypesMode = it.yValue
        }

        avgEnergySeries?.data?.forEach {
            val index = (it.xValue.toInt() -  firstDay)
            allEntries[index].avgEnergy = it.yValue.toFloat()
        }

        avgLifeTimeSeries?.data?.forEach {
            val index = (it.xValue.toInt() -  firstDay)
            allEntries[index].avgLifeTime = it.yValue.toFloat()
        }

        avgNOfKidsSeries?.data?.forEach {
            val index = (it.xValue.toInt() -  firstDay)
            allEntries[index].avgNOfKids = it.yValue.toFloat()
        }
    }

    fun write(filename: String) {
        val file = FileWriter(filename)
        val writer = BufferedWriter(file)
        val csvPrinter = CSVPrinter(
            writer, CSVFormat.DEFAULT.withHeader(
                "Day", "aliveAnimals", "nOfGrass", "genotypesMode", "avgEnergy", "avgLifeTime", "avgNOfKids"
            )
        )

        allEntries.forEach {
            val statsData = listOf(
                it.day, it.aliveAnimals, it.nOfGrass, it.genotypesMode, it.avgEnergy, it.avgLifeTime, it.avgNOfKids
            )
            csvPrinter.printRecord(statsData)
        }

        csvPrinter.flush()
        csvPrinter.close()
    }
}
