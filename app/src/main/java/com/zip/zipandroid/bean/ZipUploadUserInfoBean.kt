package com.zip.zipandroid.bean

import com.google.gson.annotations.SerializedName

data class ZipUploadUserInfoBean(
    @SerializedName("accountName")
    @JvmField
    var sunanAsusu: String,
    @SerializedName("age")
    @JvmField
    var shekarun: Int? = -1,
    @SerializedName("bankId")
    @JvmField
    var idBanki: String,
    @SerializedName("birthDate")
    @JvmField
    var kwananHaihuwa: Long,
    @SerializedName("birthDateStr")
    @JvmField
    var kwananHaihuwaSiga: String,
    @SerializedName("cardName")
    @JvmField
    var sunanKatin: String,
    @SerializedName("cardNo")
    @JvmField
    var lambarKatin: String,
    @SerializedName("cardType")
    @JvmField
    var nauInKatin: Int? = -1,
    @SerializedName("certEmail")
    @JvmField
    var imelTabbaci: Int? = -1,
    @SerializedName("channel")
    @JvmField
    var tashoshi: String,
    @SerializedName("childrens")
    @JvmField
    var yara: Int? = -1,
    @SerializedName("companyDistrict")
    @JvmField
    var gundumarKamfani: String,
    @SerializedName("payDay")
    @JvmField
    var kwananBiya: String,
    @SerializedName("companyLocation")
    @JvmField
    var wurinKamfani: String,
    @SerializedName("companyName")
    @JvmField
    var sunanKamfani: String,
    @SerializedName("creditAuthorized")
    @JvmField
    var bashiHalatta: Int? = -1,
    @SerializedName("custId")
    @JvmField
    var idCustomer: Long,
    @SerializedName("degree")
    @JvmField
    var digiri: Int? = -1,
    @SerializedName("doubleLoan")
    @JvmField
    var bashiBiyu: Int? = -1,
    @SerializedName("emergencyContactPerson")
    @JvmField
    var mutuminGaggawa: String,
    @SerializedName("fatherName")
    @JvmField
    var sunanUba: String,
    @SerializedName("firstName")
    @JvmField
    var sunanFarko: String,
    @SerializedName("identity")
    @JvmField
    var ainihin: String,
    @SerializedName("identityImg")
    @JvmField
    var hotonAinihin: String,
    @SerializedName("income")
    @JvmField
    var kudinShiga: String,
    @SerializedName("lengthOfUnemployment")
    @JvmField
    var tsawonRashinAiki: String,
    @SerializedName("timeWorkBegins")
    @JvmField
    var lokacinFarawaAiki: Long,
    @SerializedName("ontherIncome")
    @JvmField
    var sauranKudinShiga: Int? = -1,
    @SerializedName("industry")
    @JvmField
    var masanaAntu: Int? = -1,
    @SerializedName("employmentStatus")
    @JvmField
    var matsayinAiki: Int? = -1,
    @SerializedName("industryName")
    @JvmField
    var sunanMasanaAntu: String,
    @SerializedName("lastName")
    @JvmField
    var sunanKarshe: String,
    @SerializedName("marry")
    @JvmField
    var aure: Int? = -1,
    @SerializedName("mbEmail")
    @JvmField
    var imelMB: String,
    @SerializedName("mbPhone")
    @JvmField
    var wayarMB: String,
    @SerializedName("mbStatus")
    @JvmField
    var matsayinMB: Int? = -1,
    @SerializedName("language")
    @JvmField
    var harshe: Int? = -1,
    @SerializedName("mid")
    @JvmField
    var matsakaici: Long,
    @SerializedName("midName")
    @JvmField
    var sunanTsakiya: String,
    @SerializedName("motherName")
    @JvmField
    var sunanUwa: String,
    @SerializedName("nowAddress")
    @JvmField
    var adireshinYanzu: String,
    @SerializedName("postalInfo")
    @JvmField
    var bayaninPosta: String,
    @SerializedName("questions")
    @JvmField
    var tambayoyi: List<UploadUserCreditListBeanItem>,
    @SerializedName("realname")
    @JvmField
    var sunanGaskiya: String,
    @SerializedName("region")
    @JvmField
    var yankin: String,
    @SerializedName("sex")
    @JvmField
    var jimaI: String,
    @SerializedName("taxNumber")
    @JvmField
    var lambarHaraji: String,
    @SerializedName("workQuestions")
    @JvmField
    var tambayoyinAiki: List<UploadWorkQuestion>,
    @SerializedName("workType")
    @JvmField
    var nauInAiki: String,
)

data class UploadUserCreditListBeanItem(
    @SerializedName("answer")
    @JvmField
    var amsa: String,
    @SerializedName("questionIndex")
    @JvmField
    var fihirisarTambaya: String,
    @SerializedName("questionvarue")
    @JvmField
    var darajarTambaya: String,
)

data class UploadWorkQuestion(
    @SerializedName("ansCode")
    @JvmField
    var lambarAmsa: String,
    @SerializedName("answer")
    @JvmField
    var amsa: String,
    @SerializedName("quesCode")
    @JvmField
    var lambarTambaya: String,
)

