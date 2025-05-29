package org.example.workflow

import com.fasterxml.jackson.annotation.JsonProperty
import temporalencryption.dataconverterdemo.annotationbased.TemporalEncrypted
import java.math.BigDecimal
import java.util.Date
import java.util.UUID

data class WorkflowPayload(
    @JsonProperty("uuid") val uuid: UUID,
    @JsonProperty("string") @TemporalEncrypted val string: String,
    @JsonProperty("int") val int: Int,
    @JsonProperty("listOfStrings") val listOfStrings: List<String>,
    @JsonProperty("bigDecimal") val bigDecimal: BigDecimal,
    @JsonProperty("date") val date: Date
)