package example.com.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import kotlin.math.ceil
import kotlin.math.min


fun splitIntoPackets(data: ByteArray, packetSize: Int): List<ByteArray> {
    val packets: MutableList<ByteArray> = ArrayList()
    val totalPackets = ceil(data.size.toDouble() / packetSize).toInt()

    for (i in 0 until totalPackets) {
        val start = i * packetSize
        val length = min(packetSize.toDouble(), (data.size - start).toDouble()).toInt()
        val packet = ByteArray(length)
        System.arraycopy(data, start, packet, 0, length)
        packets.add(packet)
    }

    return packets
}

fun Application.configureSockets() {
    // TODO try send image via bitmap
//    val imageFile = File("src/main/resources/images/test_image.jpg")
//    val image = ImageIO.read(imageFile)
//    // Преобразование изображения в массив байтов
//    val baos = ByteArrayOutputStream()
//    ImageIO.write(image, "bmp", baos)
//    val imageInByte = splitIntoPackets(baos.toByteArray(), 2000)


    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        webSocket("/ws") { // websocketSession
            wsSessionsConnections.add(this)
            try {
                for (frame in incoming) {
                    println(frame.data)
                }
            } finally {
                // Удаляем соединение при закрытии
                wsSessionsConnections.remove(this)
            }

        }
    }
}

val wsSessionsConnections = mutableSetOf<DefaultWebSocketSession>()



