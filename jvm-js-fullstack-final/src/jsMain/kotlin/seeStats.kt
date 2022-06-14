import kotlinx.browser.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.dom.*
import kotlinx.html.js.div


private val scope = MainScope()
fun seeStats(pollID: Int) = scope.launch {
    val data: PollsModel = getPoll(pollID)
    document.body!!.lastChild?.let { it1 -> document.body!!.removeChild(it1) }
    document.body!!.append.div {
        p {
            +"Stats"
        }
        div {
            val cal = calcTotal(data)
            data.answers.forEach { item ->
                val percent = (item.value * 100) / cal
                +"${item.key} ${percent}%"
                br
            }
        }
    }
}

fun calcTotal(model: PollsModel): Int {
    var total = 0
    model.answers.forEach { item ->
        total += item.value
    }
    return total
}
