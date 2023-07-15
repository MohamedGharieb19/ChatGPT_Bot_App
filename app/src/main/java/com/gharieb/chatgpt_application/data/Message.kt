package com.gharieb.chatgpt_application.data

data class Message(
    val message:String,
    val sentBy:String,
    val time: String
){
    companion object{
        const val SENT_BY_ME = "sent_me"
        const val SENT_BY_BOT = "sent_bot"
    }
}
