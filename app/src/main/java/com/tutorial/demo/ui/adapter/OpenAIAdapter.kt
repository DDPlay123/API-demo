package com.tutorial.demo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.demo.data.openAI.Messages
import com.tutorial.demo.databinding.ItemReceivedMessageBinding
import com.tutorial.demo.databinding.ItemSentMessageBinding

class OpenAIAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val SENT = 0
        private const val RECEIVED = 1
    }

    private var messages: MutableList<com.tutorial.demo.data.openAI.Messages> = ArrayList()

    /**
     * 設定資料
     */
    fun setterData(messages: MutableList<com.tutorial.demo.data.openAI.Messages>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].role == "user")
            SENT
        else
            RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == SENT)
            SentViewHolder(ItemSentMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        else
            ReceivedViewHolder(ItemReceivedMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SentViewHolder)
            holder.bind()
        else if (holder is ReceivedViewHolder)
            holder.bind()
    }

    override fun getItemCount(): Int = messages.size

    inner class SentViewHolder(private val binding: ItemSentMessageBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind() { binding.tvMessage.text = messages[adapterPosition].content }
    }

    inner class ReceivedViewHolder(private val binding: ItemReceivedMessageBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind() { binding.tvMessage.text = messages[adapterPosition].content }
    }
}