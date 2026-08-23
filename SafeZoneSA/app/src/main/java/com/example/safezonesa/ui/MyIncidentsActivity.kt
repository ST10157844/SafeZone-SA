package com.example.safezonesa.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safezonesa.R
import com.example.safezonesa.models.Incident

class MyIncidentsActivity : AppCompatActivity() {

    // Sample data matching the dashboard look
    private val sampleIncidents = listOf(
        Incident(1,  "Robbery",              "Armed robbery at corner shop on Main St.",          "Johannesburg CBD",  "Under Review",  "2 hrs ago"),
        Incident(2,  "Vandalism",            "Graffiti on park walls near school entrance.",      "Soweto",            "Resolved",      "1 day ago"),
        Incident(3,  "Suspicious Activity",  "Unknown individuals casing vehicles on Oak Ave.",   "Sandton",           "Investigating", "3 hrs ago"),
        Incident(4,  "Fire Hazard",          "Illegal burning in open lot near Bree Street.",     "Braamfontein",      "Under Review",  "5 hrs ago"),
        Incident(5,  "Road Accident",        "Minor collision at N1 on-ramp, no injuries.",       "Midrand",           "Resolved",      "2 days ago")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_incidents)

        val btnBack = findViewById<Button>(R.id.btnBack)
        val recycler = findViewById<RecyclerView>(R.id.recyclerIncidents)

        btnBack.setOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = IncidentAdapter(sampleIncidents)
    }

    // ── Inline RecyclerView Adapter ─────────────────────────────────────────
    inner class IncidentAdapter(private val items: List<Incident>) :
        RecyclerView.Adapter<IncidentAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvType:        TextView = view.findViewById(R.id.tvType)
            val tvStatus:      TextView = view.findViewById(R.id.tvStatus)
            val tvDescription: TextView = view.findViewById(R.id.tvDescription)
            val tvRef:         TextView = view.findViewById(R.id.tvRef)
            val tvTime:        TextView = view.findViewById(R.id.tvTime)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_incident, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val inc = items[position]

            holder.tvType.text        = inc.type
            holder.tvDescription.text = inc.description
            holder.tvRef.text         = "REF #SZ-${1000 + (inc.incidentID ?: 0)}"
            holder.tvTime.text        = inc.time ?: ""

            // Status badge colour
            when (inc.status) {
                "Resolved" -> {
                    holder.tvStatus.text = inc.status
                    holder.tvStatus.setTextColor(Color.parseColor("#00D68F"))
                    holder.tvStatus.setBackgroundResource(R.drawable.bg_badge_resolved)
                }
                "Investigating" -> {
                    holder.tvStatus.text = inc.status
                    holder.tvStatus.setTextColor(Color.parseColor("#00C2FF"))
                    holder.tvStatus.setBackgroundResource(R.drawable.bg_badge_investigating)
                }
                else -> {
                    holder.tvStatus.text = "Under Review"
                    holder.tvStatus.setTextColor(Color.parseColor("#FFAA00"))
                    holder.tvStatus.setBackgroundResource(R.drawable.bg_badge_review)
                }
            }
        }
    }
}
