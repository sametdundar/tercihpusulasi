package com.tercihpusulasi.tercihpusulasi.roomdatabase


data class Student(
    val admin: Boolean = false,
    val exam_scores: Map<String, Double> = mapOf(),
    val name: String = "",
    val phoneNumber: String = "",
    val preferences: List<Preference> = listOf()
)

data class Preference(
    val priority: Int= 0,
    val program_id: Int=40
)

data class University(
    val city: String,
    val id: Int,
    val name: String,
    val programs: List<Program>
)

data class Program(
    val id: Int = 0,
    val language: String = "",
    val lastYearMinScore: Double = 0.0,
    val name: String = "",
    val quota: Int = 50,
    val scoreType: String = ""
)