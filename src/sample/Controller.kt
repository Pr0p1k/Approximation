package sample

import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.control.*
import java.lang.NumberFormatException
import java.math.BigDecimal
import java.math.RoundingMode

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
    lateinit var resultLabel: Label

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
            drawLines(points)
        }

    }

    private fun drawLines(points: ObservableList<Pair<Double, Double>>) {
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
            if (x > max) max = x
            sumX += x
            sumY += y
            sumXY += x * y
            sumSqrX += x * x
        }
        var a = (points.size * sumXY - sumX * sumY) / (points.size * sumSqrX - sumX * sumX)
        var b = (sumY - a * sumX) / points.size
        var line = XYChart.Series<Double, Double>()
        line.name = "Approximating 1"
        drawLine(min, max, a, b, line)
        resultLabel.text = "f1(x) = ${BigDecimal.valueOf(a).setScale(3, RoundingMode.FLOOR).toDouble()}x " +
                "+ ${BigDecimal.valueOf(b).setScale(3, RoundingMode.FLOOR).toDouble()}"
        var redundantI = 0
        var redundantResult = 0.0
        for (i in 0..(points.size - 1)) {
            val x = points[i].first
            val y = points[i].second
            val result = Math.abs(a * x + b - y)
            if (result > redundantResult) {
                redundantI = i
                redundantResult = result
            }
        }
        sumX = 0.0
        sumY = 0.0
        sumXY = 0.0
        sumSqrX = 0.0
        for (i in 0..(points.size - 1)) {
            if (i == redundantI) continue
            val x = points[i].first
            val y = points[i].second
            sumX += x
            sumY += y
            sumXY += x * y
            sumSqrX += x * x
        }
        a = ((points.size - 1) * sumXY - sumX * sumY) / ((points.size - 1) * sumSqrX - sumX * sumX)
        b = (sumY - a * sumX) / (points.size - 1)
        line = XYChart.Series()
        line.name = "Approximating 2"
        drawLine(min, max, a, b, line)
        resultLabel.text = resultLabel.text.plus(
                "\nf2(x) = ${BigDecimal.valueOf(a).setScale(3, RoundingMode.FLOOR).toDouble()}x" +
                " + ${BigDecimal.valueOf(b).setScale(3, RoundingMode.FLOOR).toDouble()}")
    }

    private fun drawLine(min: Double, max: Double, a: Double, b: Double, line: XYChart.Series<Double, Double>) {
        line.data.add(XYChart.Data(min, a * min + b))
        line.data.add(XYChart.Data(max, a * max + b))
        mainChart.data.add(line)
    }
}