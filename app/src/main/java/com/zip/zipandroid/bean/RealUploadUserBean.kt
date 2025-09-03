package com.zip.zipandroid.bean
import com.google.gson.annotations.SerializedName

class RealUploadUserBean(
    @SerializedName(value = "sunanAsusu", alternate = ["accountName"])
    var accountName: String,

    @SerializedName(value = "shekarun", alternate = ["age"])
    val age: Int? = -1,

    @SerializedName(value = "idBanki", alternate = ["bankId"])
    var bankId: String,

    @SerializedName(value = "kwananHaihuwa", alternate = ["birthDate"])
    val birthDate: Long,

    @SerializedName(value = "kwananHaihuwaSiga", alternate = ["birthDateStr"])
    val birthDateStr: String,

    @SerializedName(value = "sunanKatin", alternate = ["cardName"])
    val cardName: String,

    @SerializedName(value = "lambarKatin", alternate = ["cardNo"])
    var cardNo: String,

    @SerializedName(value = "nauInKatin", alternate = ["cardType"])
    val cardType: Int? = -1,

    @SerializedName(value = "imelTabbaci", alternate = ["certEmail"])
    val certEmail: Int? = -1,

    @SerializedName(value = "tashoshi", alternate = ["channel"])
    val channel: String,

    @SerializedName(value = "yara", alternate = ["childrens"])
    val childrens: Int? = -1,

    @SerializedName(value = "gundumarKamfani", alternate = ["companyDistrict"])
    val companyDistrict: String,

    @SerializedName(value = "kwananBiya", alternate = ["payDay"])
    val payDay: String,

    @SerializedName(value = "wurinKamfani", alternate = ["companyLocation"])
    val companyLocation: String,

    @SerializedName(value = "sunanKamfani", alternate = ["companyName"])
    val companyName: String,

    @SerializedName(value = "bashiHalatta", alternate = ["creditAuthorized"])
    val creditAuthorized: Int? = -1,

    @SerializedName(value = "idCustomer", alternate = ["custId"])
    val custId: Long,

    @SerializedName(value = "digiri", alternate = ["degree"])
    val degree: Int? = -1,

    @SerializedName(value = "bashiBiyu", alternate = ["doubleLoan"])
    val doubleLoan: Int? = -1,

    @SerializedName(value = "mutuminGaggawa", alternate = ["emergencyContactPerson"])
    val emergencyContactPerson: String,

    @SerializedName(value = "sunanUba", alternate = ["fatherName"])
    val fatherName: String,

    @SerializedName(value = "sunanFarko", alternate = ["firstName"])
    val firstName: String,

    @SerializedName(value = "ainihin", alternate = ["identity"])
    val identity: String,

    @SerializedName(value = "hotonAinihin", alternate = ["identityImg"])
    val identityImg: String,

    @SerializedName(value = "kudinShiga", alternate = ["income"])
    val income: String,

    @SerializedName(value = "tsawonRashinAiki", alternate = ["lengthOfUnemployment"])
    val lengthOfUnemployment: String,

    @SerializedName(value = "lokacinFarawaAiki", alternate = ["timeWorkBegins"])
    val timeWorkBegins: Long,

    @SerializedName(value = "sauranKudinShiga", alternate = ["ontherIncome"])
    val ontherIncome: Int? = -1,

    @SerializedName(value = "masanaAntu", alternate = ["industry"])
    val industry: Int? = -1,

    @SerializedName(value = "matsayinAiki", alternate = ["employmentStatus"])
    val employmentStatus: Int? = -1,

    @SerializedName(value = "sunanMasanaAntu", alternate = ["industryName"])
    val industryName: String,

    @SerializedName(value = "sunanKarshe", alternate = ["lastName"])
    val lastName: String,

    @SerializedName(value = "aure", alternate = ["marry"])
    val marry: Int? = -1,

    @SerializedName(value = "imelMB", alternate = ["mbEmail"])
    val mbEmail: String,

    @SerializedName(value = "wayarMB", alternate = ["mbPhone"])
    val mbPhone: String,

    @SerializedName(value = "matsayinMB", alternate = ["mbStatus"])
    val mbStatus: Int? = -1,

    @SerializedName(value = "harshe", alternate = ["language"])
    val language: Int? = -1,

    @SerializedName(value = "matsakaici", alternate = ["mid"])
    val mid: Long,

    @SerializedName(value = "sunanTsakiya", alternate = ["midName"])
    val midName: String,

    @SerializedName(value = "sunanUwa", alternate = ["motherName"])
    val motherName: String,

    @SerializedName(value = "adireshinYanzu", alternate = ["nowAddress"])
    val nowAddress: String,

    @SerializedName(value = "bayaninPosta", alternate = ["postalInfo"])
    val postalInfo: String,

    @SerializedName(value = "tambayoyi", alternate = ["questions"])
    val questions: List<RealUploadUserCreditListBeanItem>,

    @SerializedName(value = "sunanGaskiya", alternate = ["realname"])
    val realname: String,

    @SerializedName(value = "yankin", alternate = ["region"])
    val region: String,

    @SerializedName(value = "jimaI", alternate = ["sex"])
    val sex: String,

    @SerializedName(value = "lambarHaraji", alternate = ["taxNumber"])
    val taxNumber: String,

    @SerializedName(value = "tambayoyinAiki", alternate = ["workQuestions"])
    val workQuestions: List<RealUploadWorkQuestion>,

    @SerializedName(value = "nauInAiki", alternate = ["workType"])
    val workType: String,

    var emergentContacts: List<UploadContractBean>?,
)

data class RealUploadUserCreditListBeanItem(
    @SerializedName(value = "amsa", alternate = ["answer"])
    val answer: String,

    @SerializedName(value = "fihirisarTambaya", alternate = ["questionIndex"])
    val questionIndex: String,

    @SerializedName(value = "darajarTambaya", alternate = ["questionValue"])
    val questionValue: String
)

data class RealUploadWorkQuestion(
    @SerializedName(value = "lambarAmsa", alternate = ["ansCode"])
    val ansCode: String,

    @SerializedName(value = "amsa", alternate = ["answer"])
    val answer: String,

    @SerializedName(value = "lambarTambaya", alternate = ["quesCode"])
    val quesCode: String
)
