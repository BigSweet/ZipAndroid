package com.zip.zipandroid.bean

class ZipProductPeriod : ArrayList<ZipProductPeriodItem>()

data class ZipProductPeriodItem(
    val period: Int,
    val periodStages: List<PeriodStage>
)

data class PeriodStage(
    val did: String,
    val stage: Int
)