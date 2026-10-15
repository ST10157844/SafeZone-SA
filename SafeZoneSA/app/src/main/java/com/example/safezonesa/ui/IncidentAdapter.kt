package com.example.safezonesa.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safezonesa.R
import com.example.safezonesa.models.IncidentResponse

class IncidentAdapter(
    private var incidents: List<IncidentResponse>
) : RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder>() {

    class IncidentViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvIncidentType: TextView =
            itemView.findViewById(R.id.tvIncidentType)

        val tvDescription: TextView =
            itemView.findViewById(R.id.tvIncidentDescription)

        val tvLocation: TextView =
            itemView.findViewById(R.id.tvIncidentLocation)

        val tvStatus: TextView =
            itemView.findViewById(R.id.tvIncidentStatus)

        val tvPriority: TextView =
            itemView.findViewById(R.id.tvIncidentPriority)

        val tvDate: TextView =
            itemView.findViewById(R.id.tvIncidentDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IncidentViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_incident,
                parent,
                false
            )

        return IncidentViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: IncidentViewHolder,
        position: Int
    ) {

        val incident = incidents[position]

        holder.tvIncidentType.text =
            incident.type

        holder.tvDescription.text =
            incident.description

        holder.tvLocation.text =
            if (!incident.location.isNullOrBlank()) {

                incident.location

            } else {

                listOfNotNull(
                    incident.suburb,
                    incident.city
                )
                    .filter {
                        it.isNotBlank() &&
                                it != "Not specified"
                    }
                    .joinToString(", ")
                    .ifBlank {
                        "Location unavailable"
                    }
            }

        holder.tvStatus.text =
            incident.status
                .replace("_", " ")
                .replaceFirstChar {
                    if (it.isLowerCase()) {
                        it.titlecase()
                    } else {
                        it.toString()
                    }
                }

        holder.tvPriority.text =
            "Priority: ${
                incident.priority.replaceFirstChar {
                    if (it.isLowerCase()) {
                        it.titlecase()
                    } else {
                        it.toString()
                    }
                }
            }"

        holder.tvDate.text =
            incident.submittedAt
    }

    override fun getItemCount(): Int {
        return incidents.size
    }

    fun updateIncidents(
        newIncidents: List<IncidentResponse>
    ) {
        incidents = newIncidents
        notifyDataSetChanged()
    }
}