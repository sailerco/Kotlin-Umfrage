import kotlinx.browser.*
import kotlinx.coroutines.*
import kotlinx.html.dom.*
import kotlinx.html.js.div
import kotlinx.html.*
import kotlinx.html.js.onClickFunction

private val scope = MainScope()
fun showPollsTemplate(list: List<PollsModel>) {
    document.body!!.append.div {
        list.sortedByDescending(PollsModel::name).forEach { item ->
            div {
                h3(classes = "pollHeading") {
                    +"${item.name}"
                }
                p(classes = "desc") { +"${item.desc}" }
                button(classes = "list") {
                    onClickFunction = {
                        takePoll(item.id)
                    }
                    +"Take Poll"
                }
                button(classes = "list") {
                    onClickFunction = {
                        seeStats(item.id)
                    }
                    +"See Stats"
                }
            }
        }
    }
}

