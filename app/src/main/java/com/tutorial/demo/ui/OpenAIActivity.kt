package com.tutorial.demo.ui

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tutorial.demo.R
import com.tutorial.demo.data.openAI.ChatGPTReq
import com.tutorial.demo.data.openAI.ChatGPTRes
import com.tutorial.demo.data.openAI.Messages
import com.tutorial.demo.databinding.ActivityOpenAiActivityBinding
import com.tutorial.demo.network.ApiClient
import com.tutorial.demo.ui.adapter.OpenAIAdapter
import com.tutorial.demo.utils.Method
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenAIActivity : BaseActivity() {
    companion object {
        private const val TAG = "OpenAIActivity"
    }
    private val binding: ActivityOpenAiActivityBinding by lazy {
        ActivityOpenAiActivityBinding.inflate(layoutInflater)
    }

    private val openAIAdapter by lazy { OpenAIAdapter() }
    // 傳送用，需要紀錄每次回傳的資料
    private val sendMessages: MutableList<com.tutorial.demo.data.openAI.Messages> = mutableListOf()
    // 顯示用
    private val currentMessages: MutableList<com.tutorial.demo.data.openAI.Messages> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setActionBar()
        initRvList()
        setListener()
    }

    private fun setActionBar() {
        binding.actionbar.apply {
            tvTitle.text = getString(R.string.actionbar_open_ai)
            imgBack.setOnClickListener { finish() }
        }
    }

    private fun initRvList() {
        binding.rvList.apply {
            // 初始化RecyclerView
            layoutManager = LinearLayoutManager(this@OpenAIActivity, LinearLayoutManager.VERTICAL, false)
            adapter = openAIAdapter
        }.also {
            // 先清空
            sendMessages.clear()
            currentMessages.clear()
        }
    }

    private fun setListener() {
        binding.run {
            imgSend.setOnClickListener {
                val message = edContent.text.toString()
                if (message.isNotEmpty()) {
                    edContent.setText("")
                    // 定義要傳送的資料
                    val reqMessage =
                        com.tutorial.demo.data.openAI.Messages(role = "user", content = message)
                    // 加入到傳送用的資料列表
                    sendMessages.add(reqMessage)
                    // 加入到顯示用的資料列表
                    currentMessages.add(reqMessage)
                    // 先刷新列表
                    openAIAdapter.setterData(currentMessages)
                    rvList.scrollToPosition(currentMessages.size - 1)
                    // 呼叫API
                    ApiClient.openAI.sendChatGPT(com.tutorial.demo.data.openAI.ChatGPTReq(messages = sendMessages)).enqueue(object : Callback<com.tutorial.demo.data.openAI.ChatGPTRes> {
                        override fun onResponse(call: Call<com.tutorial.demo.data.openAI.ChatGPTRes>, response: Response<com.tutorial.demo.data.openAI.ChatGPTRes>) {
                            response.body()?.let { res ->
                                // 先儲存回傳的資料
                                res.choices.forEach { currentMessages.add(it.message) }
                                // 再儲存下次要傳送的資料
                                sendMessages.addAll(currentMessages)
                                // 刷新列表
                                openAIAdapter.setterData(currentMessages)
                                CoroutineScope(Dispatchers.Main).launch {
                                    rvList.scrollToPosition(currentMessages.size - 1)
                                }
                            }
                        }

                        override fun onFailure(call: Call<com.tutorial.demo.data.openAI.ChatGPTRes>, t: Throwable) {
                            t.printStackTrace()
                            Method.logE(TAG, "onFailure: ${t.message}")
                        }
                    })
                } else
                    Toast.makeText(applicationContext, getString(R.string.toast_not_empty), Toast.LENGTH_SHORT).show()
            }
        }
    }
}