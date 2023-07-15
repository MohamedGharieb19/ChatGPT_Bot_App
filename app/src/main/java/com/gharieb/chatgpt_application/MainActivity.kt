package com.gharieb.chatgpt_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gharieb.chatgpt_application.adapters.MessageAdapter
import com.gharieb.chatgpt_application.data.Message
import com.gharieb.chatgpt_application.databinding.ActivityMainBinding
import com.gharieb.chatgpt_application.viewModels.ChatGptViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var chatGptViewModel: ChatGptViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatGptViewModel = ViewModelProvider(this).get(ChatGptViewModel::class.java)

        setupRecyclerView()

        binding.sendBtn.setOnClickListener{
            sendQuestion()
        }

    }
    private fun setupRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        binding.recyclerView.layoutManager = layoutManager

        chatGptViewModel.messageList.observe(this){ messages ->
            val adapter = MessageAdapter(messages)
            binding.recyclerView.adapter = adapter
        }
    }
    private fun sendQuestion(){
        val question = binding.messageEditText.text.toString()
        chatGptViewModel.addToChat(question,Message.SENT_BY_ME,chatGptViewModel.getCurrentTime())
        binding.messageEditText.setText("")
        chatGptViewModel.callApi(question)
    }
}