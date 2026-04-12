package com.github.idelstak.routerfx.shared.value;

public record CommonDashboard(
  String networkType,
  String sim,
  String at,
  String wifi24,
  String wifi5,
  String lan,
  String rsrp,
  String rssi,
  String rsrq,
  String sinr,
  String pci,
  String earfcn,
  String ip,
  String wanMac,
  String primaryDns,
  String secondaryDns,
  String ipv6,
  String primaryIpv6Dns,
  String secondaryIpv6Dns,
  String runningTime,
  String firmwareVersion,
  String antennaStatus) {

}
