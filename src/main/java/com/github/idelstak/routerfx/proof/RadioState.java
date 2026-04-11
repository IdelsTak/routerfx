package com.github.idelstak.routerfx.proof;

public record RadioState(
  String networkOperator,
  String networkTypeStr,
  String rsrp,
  String rssi,
  String rsrq,
  String sinr,
  String currentBand,
  String bandwidth,
  String flowDl,
  String flowUl,
  String onlineTime,
  String onlineDuration) {

}
