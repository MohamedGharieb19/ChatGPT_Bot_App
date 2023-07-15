package com.gharieb.chatgpt_application.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gharieb.chatgpt_application.data.CompletionRequest
import com.gharieb.chatgpt_application.data.CompletionResponse
import com.gharieb.chatgpt_application.data.Message
import com.gharieb.chatgpt_application.api.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatGptViewModel:ViewModel() {
    private val _messageList = MutableLiveData<MutableList<Message>>()
    val messageList: LiveData<MutableList<Message>> get() = _messageList

    init {
        _messageList.value = mutableListOf()
    }

    fun addToChat(message: String, sentBy: String, time:String){
        val currentList = _messageList.value ?: mutableListOf()
        currentList.add(Message(message,sentBy,time))
        _messageList.postValue(currentList)
    }

    fun addResponse(response:String){
        _messageList.value?.removeAt(
            _messageList.value?.size?.minus(1) ?: 0
        )
        addToChat(response,Message.SENT_BY_BOT,getCurrentTime())
    }

    fun callApi(question: String){
        addToChat("Typing...",Message.SENT_BY_BOT,getCurrentTime())

        val completionRequest = CompletionRequest(
            model = "text-davinci-003",
            prompt = question,
            max_tokens = 4000
        )

        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getCompletion(completionRequest)
                handleApiResponse(response)

            }catch (e: SocketTimeoutException){
                addResponse("Timeout $e")
            }
        }
    }

    private suspend fun handleApiResponse(response: Response<CompletionResponse>) {
        withContext(Dispatchers.Main){
            if (response.isSuccessful){
                response.body()?.let { completionResponse ->
                    val result = completionResponse.choices.firstOrNull()?.text
                    if (result != null){
                        addResponse(result.trim())
                    }else {
                        addResponse("No choices found")
                    }
                }
            }else {
                addResponse("Failed to get response ${response.code()}")
            }
        }
    }

    fun getCurrentTime(): String {
        return SimpleDateFormat("hh mm a", Locale.getDefault()).format(Date())
    }

}