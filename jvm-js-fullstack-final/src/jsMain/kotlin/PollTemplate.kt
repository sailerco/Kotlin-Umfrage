import kotlinx.browser.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.dom.*
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement

private val scope = MainScope()
var numberOfAnswers = 1
fun pollTemplate() {
    document.body!!.append.div {
        form(action = "/poll") {
            id="form"
            div {
                label {
                    htmlFor = "title"
                    +"Title"
                }
                br
                input(InputType.text, name = "title") { id = "title" }
            }
            div {
                label {
                    htmlFor = "description"
                    text("Description")
                }
                br
                input(InputType.text, name = "description") { id = "description" }
            }
            div() {
                id = "answers"
                answers(0)
                answers(1)
            }

            button(type = ButtonType.button, classes = "create") {
                onClickFunction = {
                    numberOfAnswers += 1
                    addAnswers(numberOfAnswers)
                }
                +"Add Answers"
            }
            br
            button(type = ButtonType.button, classes = "create") {
                onClickFunction = {
                    removeAnswers()
                    numberOfAnswers -= 1
                }
                +"Remove Answers"
            }
            br
            input(type = InputType.submit, classes = "create") {
                onClickFunction = {
                    it.preventDefault()
                    val name: HTMLInputElement = document.getElementById("title") as HTMLInputElement
                    val desc = document.getElementById("description") as HTMLInputElement
                    val answers = mutableMapOf<String, Int>()
                    for (i in 0..numberOfAnswers) {
                        val htmlAnswers: HTMLInputElement = document.getElementById(i.toString()) as HTMLInputElement
                        answers[htmlAnswers.value] = 0
                    }
                    val pollItem = PollsModel(name.value, desc.value, answers)
                    numberOfAnswers = 1
                    scope.launch {
                        val id = createPoll(pollItem)
                        takePoll(id)
                    }
                }
            }
        }
    }
}

fun DIV.answers(i: Int): Unit = div {
    label {
        htmlFor = "answer"
        +"Answer"
    }
    br
    input(InputType.text, name = "answer", classes = "answer") { id = i.toString() }
}

fun addAnswers(i: Int) {
    if (document.getElementById("answers")!!.childElementCount < 8) {
        document.getElementById("answers")!!.append {
            div {
                label {
                    htmlFor = "answer"
                    +"Answer"
                }
                br { }
                input(InputType.text, name = "answer", classes = "answer") { id = i.toString() }
            }
        }
    }
}

fun removeAnswers() {
    val answerDIV = document.getElementById("answers")
    if (answerDIV!!.childElementCount > 2) {
        answerDIV!!.lastChild?.let { it1 ->
            answerDIV!!.removeChild(
                it1
            )
        }
    }
}