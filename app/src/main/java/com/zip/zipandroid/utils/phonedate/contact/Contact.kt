package com.zip.zipandroid.utils.phonedate.contact

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.util.*

class Contact : Serializable {
    class ContactItem(var label: String, var value: String) :
        Serializable

    @JvmField
    var phones: ArrayList<ContactItem>
    @JvmField
    var emails: ArrayList<ContactItem>
    @JvmField
    var displayName: String? = null
    @JvmField
    var firstName: String? = null
    @JvmField
    var lastName: String? = null
    @JvmField
    var middleName: String? = null
    @JvmField
    var postalAddresses: ArrayList<ContactItem>
    @JvmField
    var companyName: String? = null
    @JvmField
    var jobTitle: String? = null
    @JvmField
    var photoURI: String? = null
    fun toJSON(repaceRiskCharacters: Boolean): String {
        return toJSONObject(repaceRiskCharacters).toString()
    }

    fun toJSONObject(repaceRiskCharacters: Boolean): JSONObject {
        val json = JSONObject()
        try {
            json.put(
                "nickName",
                if (repaceRiskCharacters) repaceRiskCharacters(displayName) else displayName
            )
            json.put(
                "firstName",
                if (repaceRiskCharacters) repaceRiskCharacters(firstName) else firstName
            )
            json.put(
                "lastName",
                if (repaceRiskCharacters) repaceRiskCharacters(lastName) else lastName
            )
            json.put(
                "middleName",
                if (repaceRiskCharacters) repaceRiskCharacters(middleName) else middleName
            )
            json.put(
                "companyName",
                if (repaceRiskCharacters) repaceRiskCharacters(companyName) else companyName
            )
            json.put(
                "jobTitle",
                if (repaceRiskCharacters) repaceRiskCharacters(jobTitle) else jobTitle
            )
            //            json.put("photoURI", photoURI);
            json.put(
                "postalAddresses",
                jsonArayFromArray(postalAddresses, repaceRiskCharacters)
            )
            json.put("phones", jsonArayFromArray(phones, repaceRiskCharacters))
            json.put("emails", jsonArayFromArray(emails, repaceRiskCharacters))
        } catch (e: JSONException) {
        }
        return json
    }

    @Throws(JSONException::class)
    private fun jsonArayFromArray(
        array: ArrayList<ContactItem>,
        repaceRiskCharacters: Boolean
    ): JSONArray {
        val jsonArray = JSONArray()
        for (i in array.indices) {
            val item = JSONObject()
            val contact = array[i]
            item.put(
                "label",
                if (repaceRiskCharacters) repaceRiskCharacters(contact.label) else contact.label
            )
            item.put(
                "value",
                if (repaceRiskCharacters) repaceRiskCharacters(contact.value) else contact.value
            )
            jsonArray.put(item)
        }
        return jsonArray
    }

    companion object {
        fun toJSONArrayFromArray(
            contacts: Array<Contact>,
            repaceRiskCharacters: Boolean
        ): JSONArray {
            val jsonArray = JSONArray()
            for (i in contacts.indices) {
                val contact = contacts[i]
                jsonArray.put(contact.toJSONObject(repaceRiskCharacters))
            }
            return jsonArray
        }

        fun toJSONStringFromArray(
            contacts: Array<Contact>,
            repaceRiskCharacters: Boolean
        ): String {
            return toJSONArrayFromArray(contacts, repaceRiskCharacters).toString()
        }

        private val riskCharactersToBeReplaced: HashMap<String?, String?> =
            object : HashMap<String?, String?>() {
                init {
                    put("'", String.format("\\u%04X", "'".codePointAt(0)))
                    put(";", String.format("\\u%04X", ";".codePointAt(0)))
                    put("<", String.format("\\u%04X", "<".codePointAt(0)))
                    put(">", String.format("\\u%04X", ">".codePointAt(0)))
                    put("!", String.format("\\u%04X", "!".codePointAt(0)))
                }
            }

        private fun repaceRiskCharacters(str: String?): String? {
            if (str == null || str.isEmpty()) {
                return str
            }
            var resultStr: String = str
            for (s in riskCharactersToBeReplaced.keys) {
                resultStr = resultStr.replace("$s,","${riskCharactersToBeReplaced[s]}")
            }
            return resultStr
        }
    }

    init {
        phones = ArrayList()
        emails = ArrayList()
        postalAddresses = ArrayList()
    }
}