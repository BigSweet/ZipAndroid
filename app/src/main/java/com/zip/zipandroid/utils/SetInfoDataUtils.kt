package com.zip.zipandroid.utils

object SetInfoDataUtils {


    fun getEduList(): List<String> {
        val list = arrayListOf<String>()
        list.add("Doctorate / phD")
        list.add("Master's Degree")
        list.add("Bachelor's Degree")
        list.add("Higher National Diploma(HND)")
        list.add("Ordinary National Diploma(OND)")
        list.add("National Certificate of Education(NCE)")
        list.add("Vocational / Technical Education")
        list.add("Vocational / Technical Education")
        list.add("Senior Secondary School / High School Graduate")
        list.add("Junior Secondary School")
        list.add("Primary School")
        list.add("No Formal Education")
        return list
    }

    fun getMarryList(): List<String> {
        val list = arrayListOf<String>()
        list.add("Widowed")
        list.add("Divorced")
        list.add("Married")
        list.add("Single")
        return list
    }

    fun getChildList(): List<String> {
        val list = arrayListOf<String>()
        list.add("1")
        list.add("2")
        list.add("3")
        list.add("4")
        list.add("5")
        list.add("6")
        list.add("7")
        list.add("More than 7")
        return list
    }

    fun getLan(): List<String> {
        val list = arrayListOf<String>()
        list.add("Yoruba")
        list.add("French")
        list.add("English")
        list.add("Hausa")
        list.add("Pidgin")
        list.add("Lgbo")
        list.add("Others")
        return list
    }
}