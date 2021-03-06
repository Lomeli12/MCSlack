package net.lomeli.mcslack.core.helper

import com.google.common.base.Strings
import com.google.gson.Gson
import net.lomeli.mcslack.MCSlack
import net.lomeli.mcslack.core.lib.SlackAuthTest
import net.lomeli.mcslack.core.lib.SlackChannelInfoResponse
import net.lomeli.mcslack.core.lib.SlackUserInfoResponse
import net.lomeli.mcslack.core.lib.SlackMessage
import net.minecraft.entity.player.EntityPlayer
import org.apache.http.*
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.params.HttpParams
import java.io.InputStreamReader
import java.net.URLEncoder
import java.util.*

object SlackPostHelper {
    private var gson: Gson = Gson()

    fun sendSlackMessage(name: String, icon: String, message: String) {
        if (!MCSlack.running) return
        val httpClient = HttpClientBuilder.create().build()
        try {
            val request = HttpPost(MCSlack.modConfig!!.incomingHook)
            val msg = SlackMessage(name, message, icon)
            val json = URLEncoder.encode(gson.toJson(msg), "UTF-8")
            val param = StringEntity("payload=${json}")
            request.addHeader("content-type", "application/x-www-form-urlencoded")
            request.entity = param
            val response = httpClient.execute(request)
        } catch (ex: Exception) {
            MCSlack.logger.logError("Something happened while sending message!")
            ex.printStackTrace()
        } finally {
            httpClient.close()
        }
    }

    fun sendPlayerMessage(player: String, message: String) {
        sendSlackMessage(player, "https://mcapi.ca/avatar/2d/$player/100/false", message)
    }

    fun sendPlayerMessage(player: EntityPlayer, message: String) {
        sendPlayerMessage(player.displayNameString, message)
    }

    fun sendBotMessage(message: String) {
        sendSlackMessage(MCSlack.modConfig!!.botName, MCSlack.modConfig!!.botIcon, message)
    }

    fun canSendAPIRequests(): Boolean {
        if (MCSlack.running && !Strings.isNullOrEmpty(MCSlack.modConfig?.apiKey)) {
            var url = "https://slack.com/api/auth.test?token=${MCSlack.modConfig?.apiKey}"
            val httpClient = HttpClientBuilder.create().build()
            var response: CloseableHttpResponse = DummyResponse()
            try {
                val request = HttpPost(url)
                response = httpClient.execute(request);
            } catch (ex: Exception) {
                return false;
            } finally {
                var result = false
                if (response != null) {
                    val test = gson.fromJson(InputStreamReader(response.entity.content), SlackAuthTest::class.java)
                    if (test != null)
                        result = test.ok
                }
                httpClient.close()
                return result
            }
        }
        return false
    }

    fun getUserName(id: String):String {
        if (MCSlack.running && !Strings.isNullOrEmpty(MCSlack.modConfig?.apiKey)) {
            var url = "https://slack.com/api/users.info?token=${MCSlack.modConfig?.apiKey}&user=$id"
            val httpClient = HttpClientBuilder.create().build()
            var response: CloseableHttpResponse = DummyResponse()
            try {
                val request = HttpPost(url)
                response = httpClient.execute(request);
            } catch (ex: Exception) {
                return id;
            } finally {
                var name = id
                if (response != null) {
                    val test = gson.fromJson(InputStreamReader(response.entity.content), SlackUserInfoResponse::class.java)
                    if (test != null && test.user != null)
                        name = test.user.name
                }
                httpClient.close()
                return name
            }
        }
        return id;
    }

    fun getChannelName(id: String):String {
        if (MCSlack.running && !Strings.isNullOrEmpty(MCSlack.modConfig?.apiKey)) {
            var url = "https://slack.com/api/channels.info?token=${MCSlack.modConfig?.apiKey}&channel=$id"
            val httpClient = HttpClientBuilder.create().build()
            var response: CloseableHttpResponse = DummyResponse()
            try {
                val request = HttpPost(url)
                response = httpClient.execute(request);
            } catch (ex: Exception) {
                return id;
            } finally {
                var name = id
                if (response != null) {
                    val test = gson.fromJson(InputStreamReader(response.entity.content), SlackChannelInfoResponse::class.java)
                    if (test != null && test.channel != null)
                        name = test.channel.name
                }
                httpClient.close()
                return name
            }
        }
        return id;
    }

    private class DummyResponse : CloseableHttpResponse {
        override fun getParams(): HttpParams? {
            throw UnsupportedOperationException()
        }

        override fun removeHeader(header: Header?) {
            throw UnsupportedOperationException()
        }

        override fun getLocale(): Locale? {
            throw UnsupportedOperationException()
        }

        override fun getProtocolVersion(): ProtocolVersion? {
            throw UnsupportedOperationException()
        }

        override fun setParams(params: HttpParams?) {
            throw UnsupportedOperationException()
        }

        override fun setLocale(loc: Locale?) {
            throw UnsupportedOperationException()
        }

        override fun setStatusCode(code: Int) {
            throw UnsupportedOperationException()
        }

        override fun getHeaders(name: String?): Array<out Header>? {
            throw UnsupportedOperationException()
        }

        override fun setReasonPhrase(reason: String?) {
            throw UnsupportedOperationException()
        }

        override fun addHeader(header: Header?) {
            throw UnsupportedOperationException()
        }

        override fun addHeader(name: String?, value: String?) {
            throw UnsupportedOperationException()
        }

        override fun headerIterator(): HeaderIterator? {
            throw UnsupportedOperationException()
        }

        override fun headerIterator(name: String?): HeaderIterator? {
            throw UnsupportedOperationException()
        }

        override fun getFirstHeader(name: String?): Header? {
            throw UnsupportedOperationException()
        }

        override fun close() {
            throw UnsupportedOperationException()
        }

        override fun getStatusLine(): StatusLine? {
            throw UnsupportedOperationException()
        }

        override fun removeHeaders(name: String?) {
            throw UnsupportedOperationException()
        }

        override fun setHeaders(headers: Array<out Header>?) {
            throw UnsupportedOperationException()
        }

        override fun setEntity(entity: HttpEntity?) {
            throw UnsupportedOperationException()
        }

        override fun getEntity(): HttpEntity? {
            throw UnsupportedOperationException()
        }

        override fun getLastHeader(name: String?): Header? {
            throw UnsupportedOperationException()
        }

        override fun containsHeader(name: String?): Boolean {
            throw UnsupportedOperationException()
        }

        override fun setStatusLine(statusline: StatusLine?) {
            throw UnsupportedOperationException()
        }

        override fun setStatusLine(ver: ProtocolVersion?, code: Int) {
            throw UnsupportedOperationException()
        }

        override fun setStatusLine(ver: ProtocolVersion?, code: Int, reason: String?) {
            throw UnsupportedOperationException()
        }

        override fun setHeader(header: Header?) {
            throw UnsupportedOperationException()
        }

        override fun setHeader(name: String?, value: String?) {
            throw UnsupportedOperationException()
        }

        override fun getAllHeaders(): Array<out Header>? {
            throw UnsupportedOperationException()
        }
    }
}