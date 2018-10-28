package sample;

import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.*
import java.lang.Exception
import java.lang.NumberFormatException

class Controller {

    @FXML
    lateinit var addButton: Button

    @FXML
    lateinit var pointsList: ListView<Pair<Double, Double>>

    @FXML
    lateinit var pointX: TextField

    @FXML
    lateinit var pointY: TextField

    @FXML
    lateinit var errorLabel: Label

    @FXML
    lateinit var drawButton: Button

    @FXML
    lateinit var mainChart: LineChart<Double, Double>

    fun initialize() {
        val points = pointsList.items
        addButton.setOnAction {
            try {
                val x = pointX.characters.toString().toDouble()
                val y = pointY.characters.toString().toDouble()
                points.add(Pair(x, y))
                errorLabel.text = ""
            } catch (ex: NumberFormatException) {
                errorLabel.text = "Point coordinates aren't correct"
            }
        }

        drawButton.setOnAction {
            mainChart.data.clear()
            val series = XYChart.Series<Double, Double>()
            series.name = "Original function"
            for (point in points) {
                series.data.add(XYChart.Data(point.first, point.second))
            }
            mainChart.data.add(series)
            var sumX = 0.0
            var sumY = 0.0
            var sumXY = 0.0
            var sumSqrX = 0.0
            var max = Double.MIN_VALUE
            var min = Double.MAX_VALUE
            for (i in 0..(points.size - 1)) {
                val x = points[i].first
                val y = points[i].second
                if (x < min) min = x
                println(min)
                if (x > max) max = x
                sumX += x
                sumY += y
                sumXY += x * y
                sumSqrX += x * x
            }
            val a = (points.size * sumXY - sumX * sumY) / (points.size * sumSqrX - sumX * sumX)
            val b = (sumY - a * sumX) / points.size
            val line = XYChart.Series<Double, Double>()
            line.name = "Approximating 1"
            line.data.add(XYChart.Data(min, a * min + b))
            line.data.add(XYChart.Data(max, a * max + b))
            mainChart.data.add(line)
            
            //TODO calculation of the second damn line
            //TODO drawing of damn approximating lines
        }

    }
}