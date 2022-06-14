import csstype.HtmlAttributes
import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.statement.*
import io.ktor.client.call.*
import kotlinx.browser.window
import kotlin.js.json

val endpoint = window.location.origin // only needed until https://youtrack.jetbrains.com/issue/KTOR-453 is resolved

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun getPolls(): List<PollsModel> {
    return jsonClient.get(endpoint + PollsModel.path)
}

suspend fun getPoll(pollItem: Int): PollsModel {
    return jsonClient.get<PollsModel>(endpoint + PollsModel.path + "/${pollItem}")
}

suspend fun createPoll(polls: PollsModel): Int {
    return jsonClient.post<Int>(endpoint + PollsModel.path) {
        contentType(ContentType.Application.Json)
        body = polls
    }
}

suspend fun updatePoll(polls: PollsModel) {
    jsonClient.put<Unit>(endpoint + PollsModel.path) {
        contentType(ContentType.Application.Json)
        body = polls
    }
}

suspend fun deletePolls(shoppingListItem: PollsModel) {
    jsonClient.delete<Unit>(endpoint + PollsModel.path + "/${HtmlAttributes.id}")
}
