import kotlinx.browser.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.dom.*
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement


private val scope = MainScope()
var number = 0
fun takePoll(pollID: Int) = scope.launch {
    val data: PollsModel = getPoll(pollID)
    document.body!!.lastChild?.let { it1 -> document.body!!.removeChild(it1) }
    document.body!!.append.div {
        form {
            h3(classes = "pollHeading") { +"${data.name}" }
            p(classes = "desc") { +"${data.desc}" }
            data.answers.forEach { item ->
                input(InputType.radio) {
                    name = "poll"
                    id = "$number"
                    value = "${item.key}"
                    number += 1
                }
                label {
                    htmlFor = "${item.key}"
                    +"${item.key}"
                }
                br
            }
            input(type = InputType.submit, classes = "create") {
                onClickFunction = {
                    it.preventDefault()
                    for (i in 0 until number) {
                        val answer: HTMLInputElement = document.getElementById(i.toString()) as HTMLInputElement
                        if (answer.checked) {
                            var answerValue = data.answers[answer.value]
                            if (answerValue != null) {
                                data.answers[answer.value] = answerValue + 1
                            }
                            number = 0
                            scope.launch {
                                updatePoll(data)
                                seeStats(data.id)
                            }
                        }
                    }
                }
                +"Submit"
            }
        }
    }

}

