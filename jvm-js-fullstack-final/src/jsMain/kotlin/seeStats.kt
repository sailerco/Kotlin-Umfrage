import kotlinx.browser.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.dom.*
import kotlinx.html.js.div
import kotlin.math.roundToInt


private val scope = MainScope()
var w = 310
fun seeStats(pollID: Int) = scope.launch {
    val data: PollsModel = getPoll(pollID)
    document.body!!.lastChild?.let { it1 -> document.body!!.removeChild(it1) }
    document.body!!.append.div {
        h3(classes = "pollHeading") { +"Stats: ${data.name}" }
        p(classes = "desc") { +"${data.desc}" }
        div {
            val cal = calcTotal(data)
            data.answers.forEach { item ->
                div {
                    div(classes = "stats") {
                        val percent = (item.value.toDouble() * 100.0) / cal
                        val roundoff = (percent*100).roundToInt()/100.0
                        val width = (roundoff/100)*w
                        if (width > 50)
                            style = """width:${(roundoff/100)*w}px"""
                        else
                            style = """width:${(roundoff/100)*w}px; color:#2F4670"""
                        +"${item.key} ${roundoff}%"
                    }
                }
            }
        }
    }
}

fun calcTotal(model: PollsModel): Double {
    var total = 0.0
    model.answers.forEach { item ->
        total += item.value.toDouble()
    }
    return total
}
