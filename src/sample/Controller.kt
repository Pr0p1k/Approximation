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
    lateinit var mainChart: LineChart<String, Double>

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
            val series = XYChart.Series<String, Double>()
            for (point in points) {
                series.data.add(XYChart.Data<String, Double>(point.first.toString(), point.second))
            }
            mainChart.data.add(series)
            var sumX = 0.0
            var sumY = 0.0
            var sumXY = 0.0
            var sumSqrX = 0.0
            var max = Double.NEGATIVE_INFINITY
            var min = Double.POSITIVE_INFINITY
            for (i in 1..points.size) {
                val x = points[i].first
                val y = points[i].second
                if (x > max) max = x
                else if (x < min) min = x
                sumX += x
                sumY += y
                sumXY += x * y
                sumSqrX += x * x
            }
            val a = (points.size * sumXY - sumX * sumY) / (points.size * sumSqrX - sumX * sumX)
            val b = (sumY - a * sumX) / points.size
            val line = XYChart.Series<String, Double>()
            line.data.add(XYChart.Data(min.toString(), a * min + b))
            line.data.add(XYChart.Data(max.toString(), a * max + b))
            //TODO calculation of the second damn line
            //TODO drawing of damn approximating lines
        }

    }
}