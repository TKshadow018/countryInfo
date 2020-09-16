package tk.a3labgo.countryinfo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tk.a3labgo.countryinfo.models.KeyValue
import tk.a3labgo.countryinfo.R
import kotlin.collections.ArrayList

class SpecificAdapter(private val context: Context) : RecyclerView.Adapter<SpecificAdapter.ViewHolder>() {
    private var visibleItems: List<KeyValue>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.specific_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.title.text = visibleItems.get(i).key
        viewHolder.desc.text = visibleItems.get(i).value
        viewHolder.itemView.setOnClickListener{
            if (visibleItems.get(i).key == context.getText(R.string.map)){

            }
        }
    }

    override fun getItemCount(): Int {
        return visibleItems.size
    }

    fun setSpecificList(visibleItems: ArrayList<KeyValue>) {
        this.visibleItems = visibleItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.itemTitle)
        val desc: TextView = itemView.findViewById(R.id.itemDescription)
    }

    init {
        visibleItems = ArrayList()
    }
}