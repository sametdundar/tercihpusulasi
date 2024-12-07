package com.tercihpusulasi.tercihpusulasi.roomdatabase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
    val admin: Boolean = false,
    val exam_scores: Map<String, Double> = mapOf(),
    val name: String = "",
    val phoneNumber: String = "",
    val preferences: List<Preference> = listOf()
): Parcelable

@Parcelize
data class Preference(
    val priority: Int= 0,
    val program_id: Int=40
): Parcelable


@Parcelize
data class University(
    val city: String = "",
    val id: Int = 0,
    val name: String = "",
    val programs: List<Program> = listOf()
): Parcelable

@Parcelize
data class Program(
    val id: Int = 0,
    val language: String = "",
    val last_year_min_score: Double = 0.0,
    val name: String = "",
    val quota: Int = 0,
    val score_type: String = ""
): Parcelable