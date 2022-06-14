import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.*
import org.litote.kmongo.reactivestreams.KMongo
import kotlinx.serialization.Serializable

@Serializable
val client = KMongo.createClient().coroutine
val database = client.getDatabase("Polls")
val collection = database.getCollection<PollsModel>()
fun main() {
    embeddedServer(Netty, 9090) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Put)
            method(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }
        routing {
            get("/") {
                call.respondText(
                    this::class.java.classLoader.getResource("index.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            static("/") {
                resources("")
            }
            route("poll"){
                get("/{id}") {
                    println("get 1 poll")
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    val item = collection.findOne(PollsModel::id eq id)!!
                    call.respond(item)
                }
            }
            route(PollsModel.path) {
                get {
                    println("get polls")
                    call.respond(collection.find().toList())
                }
                put {
                    println("update poll")
                    val model = call.receive<PollsModel>()
                    collection.updateOne(PollsModel::id eq model.id, model)
                    call.respond(HttpStatusCode.OK)
                }
                post {
                    println("create poll")
                    val model = call.receive<PollsModel>()
                    collection.insertOne(model)
                    call.respond(model.id)
                }
                get("/{id}") {
                    println("get 1 poll")
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    val item = collection.findOne(PollsModel::id eq id)!!
                    call.respond(item)
                }
                delete("/{id}") {
                    println("delete poll")
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    collection.deleteOne(PollsModel::id eq id)
                    call.respond(HttpStatusCode.OK)
                }

            }
        }
    }.start(wait = true)
}