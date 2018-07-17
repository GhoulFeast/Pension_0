package com.hq.kbase.network

import android.util.JsonToken
import okhttp3.*
import android.util.Xml
import com.google.gson.stream.JsonReader
import org.xmlpull.v1.XmlPullParser
import java.io.*
import okhttp3.RequestBody
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Cookie
import java.util.concurrent.TimeUnit


/**
 * Created by lenovo on 2017/11/14.
 */
class Submit {
    //可配置属性
    var url = ""
    var tag = ""
    var method = Method.GET
    var returnType = ReturnType.JSON
    var isDebug = true
    var downloadPath = System.currentTimeMillis().toString() + ".jpg"
    var outTime = 5L//单位为秒

    val _params: MutableMap<String, Any> = mutableMapOf()
    val _fileParams: MutableMap<String, String> = mutableMapOf()
    val _headers: MutableMap<String, String> = mutableMapOf()
    val _response: MutableMap<String, Any> = mutableMapOf()

    private var _start: () -> Unit = {}
    private var _success: () -> Unit = {}
    private var _fail: (String) -> Unit = {}

    private var isError = false
    var cookjar: CookieJar
    val cookieStore = HashMap<String, List<Cookie>>()//cookie缓存

    init {
        cookjar = object : CookieJar {
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookieStore.put(url.host(), cookies)
            }

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                val cookies = cookieStore[url.host()]
                return cookies ?: ArrayList()
            }
        }
    }


    fun run() {
        tryInit()

    }

    fun tryInit(): Unit { //检查配置单
        _params.clear()
        _response.clear()
        _fileParams.clear()
        _headers.clear()
        when (returnType) {
            ReturnType.JSON -> {
            }
            ReturnType.XML -> {
            }
        }
        if (isError) {
            return
        }
        if (url == "") return
        _start()

        when (method) {//分类请求
            Method.GET -> get()

            Method.POST -> post()

            Method.IMAGE -> upImage()

            Method.DOWNLOAD -> download()

            Method.FILE->upFile()
        }
    }

    fun start(start: () -> Unit): Unit {//检查参数
        _start = start


    }

    fun success(success: () -> Unit): Unit {
        _success = success
    }

    fun fail(fail: (failMsg: String) -> Unit): Unit {
        _fail = fail
    }

    fun get(): Unit {
        val okHttpClient = OkHttpClient.Builder().cookieJar(cookjar).connectTimeout(outTime, TimeUnit.SECONDS)

        for (p in _params) {
            url = url + p.key + "=" + p.value + "&"
        }
        url = url.substring(0, url.length - 1)
        val request = Request.Builder().url(url).build()
        val call = okHttpClient.build().newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                failCall(e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                successCall(response)
            }
        })
    }

    fun post(): Unit {
        val okHttpClient = OkHttpClient.Builder().cookieJar(cookjar).connectTimeout(outTime, TimeUnit.SECONDS)
        val build = FormBody.Builder()
        for (p in _params) {
            build.add(p.key, p.value.toString())
        }
        val body = build.build()
        val request = Request.Builder().url(url).post(body).build()
        val call = okHttpClient.build().newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                failCall(e.toString())

            }

            override fun onResponse(call: Call, response: Response) {
                successCall(response)
            }
        })
    }

    fun upImage(): Unit {
        val mOkHttpClient = OkHttpClient.Builder().cookieJar(cookjar).connectTimeout(outTime, TimeUnit.SECONDS)
        val build = MultipartBody.Builder().setType(MultipartBody.FORM)
        for (p in _params) {
            if (p.value is File) {
                build.addFormDataPart(p.key, (p.value as File).name, RequestBody.create(MediaType.parse("image/png"), p.value as File))
            } else {
                build.addFormDataPart(p.key, p.value.toString())
            }

        }
        val requestBody = build.build()

        val request = Request.Builder()
//                .header("Authorization", "Client-ID " + "...")
                .url(url)
                .post(requestBody)
                .build()

        mOkHttpClient.build().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                failCall(e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                successCall(response)
            }
        })
    }

    fun upFile(): Unit {
        val mOkHttpClient = OkHttpClient.Builder().cookieJar(cookjar).connectTimeout(outTime, TimeUnit.SECONDS)
        val build = MultipartBody.Builder().setType(MultipartBody.FORM)
        for (p in _params) {
            if (p.value is File) {
                build.addFormDataPart(p.key, (p.value as File).name, RequestBody.create(MediaType.parse("file/*"), p.value as File))
            } else {
                build.addFormDataPart(p.key, p.value.toString())
            }

        }
        val requestBody = build.build()

        val request = Request.Builder()
//                .header("Authorization", "Client-ID " + "...")
                .url(url)
                .post(requestBody)
                .build()

        mOkHttpClient.build().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                failCall(e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                successCall(response)
            }
        })
    }

    fun download(): Unit {
        val mOkHttpClient = OkHttpClient.Builder().cookieJar(cookjar).connectTimeout(outTime, TimeUnit.SECONDS)
        val request = Request.Builder().url(url).build()
        mOkHttpClient.build().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                failCall(e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val inputStream = response.body().byteStream()
                var fileOutputStream = FileOutputStream(File(downloadPath))
                val buffer = ByteArray(2048)
                var len = 0
                while (len != -1) {
                    fileOutputStream.write(buffer, 0, len)
                    len = inputStream.read(buffer)
                }
                fileOutputStream.flush()
                fileOutputStream.close()
            }
        })
    }


    fun failCall(failMsg: String): Unit {
        kotlin.run {
            _fail(failMsg)
        }
    }

    fun successCall(response: Response): Unit {
        kotlin.run {
            if (response.code() != 200) {
                _fail("请求失败:" + response.code())
                return
            }

            when (returnType) {
                ReturnType.JSON -> {
                    pullJson(response.body().string())
                }
                ReturnType.XML -> {
//                    val s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ROOT><RESULT><CODE>9999</CODE><POS><PO>1111</PO><PO>2222</PO></POS><CONTENT>java.lang.NullPointerException\ncom.aisino.heb.xlg.web.servlet.XlgServlet.doPost(XlgServlet.java:135)</CONTENT></RESULT></ROOT>".byteInputStream()
                    pullXML(response.body().byteStream())
//                    pullXML(s)
                }
                ReturnType.STRING -> {
                    _response.put(ReturnType.STRING.name, response.body().string())
                }
            }
            _success()
        }
    }

    //- 入参
    operator fun String.minus(value: String) {
        _params.put(this, value)
    }

    //- 入参
    operator fun String.minus(value: Long) {
        _params.put(this, value)
    }

    // ！ 简单取参 单key
    operator fun String.not(): String {
        return _response[this].toString()
    }

    // .. 复杂取参
    operator fun <E>String.rangeTo(tag: String): E {
        val c = _response[this] as MutableMap<String, Any>
        return c[tag] as E
    }


    fun pullJson(jsonData: String): Unit {
        val reader = JsonReader(StringReader(jsonData))
        reader.beginObject()
        while (reader.hasNext()) {
            val jName = reader.nextName()
            loopJson(jName, reader, _response)
        }
        reader.endObject()
    }


    fun loopJson(loopName: String, reader: JsonReader, target: MutableMap<String, Any>): Unit {
        when (reader.peek().name) {
            JsonToken.BEGIN_OBJECT.name -> {
                reader.beginObject()
                val bo: MutableMap<String, Any> = mutableMapOf()
                while (reader.hasNext()) {
                    val jName = reader.nextName()
                    loopJson(jName, reader, bo)
                }
                target.put(loopName, bo)
                reader.endObject()
            }
            JsonToken.BEGIN_ARRAY.name -> {
                reader.beginArray()
                val al = ArrayList<MutableMap<String, Any>>()
                while (reader.hasNext()) {
                    reader.beginObject()
                    val ba: MutableMap<String, Any> = mutableMapOf()
                    while (reader.hasNext()) {
                        loopJson(reader.nextName(), reader, ba)
                    }
                    al.add(ba)
                    reader.endObject()
                }
                reader.endArray()
                target.put(loopName, al)
            }
            else -> {
                target.put(loopName, reader.nextString())
            }
        }
    }


    fun pullXML(byteStream: InputStream): Unit {
        val parser = Xml.newPullParser()
        parser.setInput(byteStream, "UTF-8")
        var eventCode = parser.eventType
        var tagName = ""
        var tagText = ""
        while (eventCode != XmlPullParser.END_DOCUMENT) {
            when (eventCode) {
                XmlPullParser.START_DOCUMENT -> {

                }

                XmlPullParser.START_TAG -> {
                    tagName = parser.name
                }

                XmlPullParser.TEXT -> {
                    tagText = parser.text
                }
                XmlPullParser.END_TAG -> {// 结束标签，判断一个对象是否结束，结束后添加到集合中
                    if (_response.containsKey(tagName)) {
                        if (_response[tagName] is ArrayList<*>) {
                            (_response[tagName] as ArrayList<String>).add(tagText)
                        } else {
                            val al = ArrayList<String>()
                            al.add(_response[tagName].toString())
                            al.add(tagText)
                            _response.put(tagName, al)
                        }
                    } else {
                        _response.put(tagName, tagText)
                    }
                }
            }
            eventCode = parser.next() // 取下个标签
        }
    }

    fun <E> getAny(string: String): E? {
        return loopAny<E>(string,_response) as E
    }


    private fun <E> loopAny (key: String,target: MutableMap<String, Any>): E? {
        var result:E?
        if (target.containsKey(key)) {
            return target[key] as E
        }//第一层
        for (res in target) {
            if (res.value is MutableMap<*, *>) {
                result= loopAny<E>(key, res.value as MutableMap<String, Any>)
                if (result!=null){
                    return result
                }
            }
        }

        return null
    }
}
