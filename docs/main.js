var AlertUtil = {
    alertMsg: function(e) {
        alert(e)
    },
    confirm: function(e) {
        return confirm(e)
    }
}
  , WlanPara = ""
  , ProgressTime = {
    REBOOT: 120,
    FIND_AP: 15,
    UPDATE_PARTIAL: 150,
    UPLOAD_FILE: 3,
    SEARCH_PLMN: 120,
    UPGRADE_CHECK: 60,
    REMOTE_UPGRADE: 240
}
  , seniorHide = {
    web_page_hide_set_0: 0,
    web_page_hide_set_1: 1,
    web_page_hide_set_2: 2,
    web_page_hide_set_3: 3,
    web_page_hide_set_4: 4,
    web_page_hide_set_6: 6
}
  , RequestCmd = {
    SYS_INFO: 0,
    WIRELESS_CONFIG: 2,
    NETWORK_CONFIG: 3,
    SYS_UPDATE: 5,
    SYS_REBOOT: 6,
    FIREWALL: 10,
    SET_DATETIME: 11,
    SMS_INFO: 12,
    SMS_SEND: 13,
    SMS_DELETE: 14,
    SMS_DELETE_ALL: 15,
    SMS_SETTING: 16,
    SYS_LOG: 17,
    FLOW_INFO: 18,
    MODULE_LOG: 19,
    APPLY_FILTER: 20,
    PORT_FILTER: 21,
    IP_FILTER: 22,
    MAC_FILTER: 23,
    IP_MAC_BINDING: 24,
    SPEED_LIMIT: 25,
    URL_FILTER: 26,
    OTHER_FILTER: 27,
    DEFAULT_FILTER: 28,
    URL_DEFAULT_FILTER: 29,
    MAC_DEFAULT_FILTER: 30,
    FIRWALL_ACL_FILTER: 31,
    IP_PORT_FILTER: 32,
    FIRWALL_UPNP: 33,
    DEVICE_VERSION_INFO: 43,
    LTE_AT: 45,
    NET_CONNECT: 48,
    NET_DISCONNECT: 49,
    GET_HTML: 50,
    MODEM_CMD: 51,
    INIT_PAGE: 80,
    GET_LTE_STATUS: 82,
    ARP_BINDING: 83,
    CHANGE_LANGUAGE: 97,
    CHANGE_USERNAME: 98,
    REBOOT: 99,
    LOGIN: 100,
    LOGOUT: 101,
    CHANGE_PASSWD: 102,
    FIND_AP: 103,
    GET_DEVICE_NAME: 104,
    UPDATE_PARTIAL: 106,
    RESTORE_DEFAULT: 112,
    GET_SYS_STATUS: 113,
    GET_PLMN_NUMBERS: 114,
    STATIC_LEASE: 115,
    SEARCH_PLMN: 116,
    GET_SMS_STATUS_REPORT: 125,
    SET_SMS_STATUS_REPORT: 126,
    MULTI_APN_INFO: 130,
    IMSI_PREFIX_LIST: 131,
    WPS_CONFIG: 132,
    ROUTER_INFO: 133,
    QUERY_SIM_STATUS: 134,
    GET_SUPPORT_BANDS: 157,
    LOCK_PHY_CELL: 160,
    LOCK_BAND: 161,
    ROUTER_TABLE: 164,
    TR069_REG: 166,
    PPTP_VPN: 167,
    NETWORK_TOOLS: 168,
    DDNS: 169,
    WRITE_TIME: 170,
    NETWORK_SERVICE: 172,
    GRE_VPN: 135,
    DNS_CONFIG: 175,
    CONFIG_EXPORT: 180,
    PPPOE_LOG: 183,
    CONFIG_UPDATE: 184,
    ONLINE_UPGRADE_AUTO: 196,
    REMOTE_UPGRADE: 198,
    CONFIG_LOADER: 199,
    UPNP: 202,
    FLOW_MONITORING: 203,
    GET_FILE_DATA: 204,
    SYS_INFO_NETWORK: 205,
    WAN_INFO: 206,
    DEVICE_INFO: 207,
    LAN_INFO: 208,
    WIFI_INFO: 209,
    WIFI5G_INFO: 210,
    WIRELESS5G_CONFIG: 211,
    TR069_CONFIG: 212,
    APN_CONFIG: 213,
    GET_DMZ: 214,
    FW_UPNP: 215,
    STATIC_ARP_BINDING: 216,
    ACL_FILTER: 217,
    NETWORKSET_MODE: 218,
    LOCK_PLMN: 219,
    DATA_ROAMING: 220,
    DDNS_DATA: 221,
    AUTO_DIAL: 222,
    DHCPCLIENT_INFO: 223,
    WIFI24G_LIST_INFO: 224,
    WIFI5G_LIST_INFO: 225,
    FLIGHT_MODE_INFO: 226,
    DATA_SWITCH: 227,
    SCCAN_PLMN: 228,
    INPUT_8198D_LOGIN: 229,
    WIRELESS_ADVANCE: 230,
    WIRELESS5G_ADVANCE: 231,
    GET_NEXT_LOGIN_TIME: 232,
    RESET_LOGIN_TIME: 233,
    SYS_SERVICE_SET: 234,
    DISPLAY_SOLUTION_MODE: 235,
    MODEM_LOG_SWITCH: 236,
    ADB_SWITCH: 237,
    DETECTION_VERSION_INFO: 238,
    UPGRADE_IMMEDIATELY: 239,
    SYS_AUTO_UPGRADE: 240,
    EXPORT_LOG: 241,
    WLAN_5G_TPC: 242,
    WPS_PIN_CODE: 243,
    OPEN_RANDOM_PIN: 244,
    REMOTE_PACKET_CAPTURE: 245,
    CHANGE_CARD_TYPE: 246,
    GET_ESIM_INFO: 247,
    APN_INFO_PROCESS: 248,
    RESET_ESIM_INFO: 249,
    DOWNLOAD_ESIM_INFO: 250,
    IP_PASSTHROUGH: 251,
    ONENET_SETTING: 252,
    L2TP_SETTING: 253,
    SLICE_CONIFIG: 254,
    SFP_IPA_SETTING: 255,
    DM_SETTING: 257,
    SET_ARP_AGING: 258,
    PPTP_SETTING: 259,
    GRE_SETTING: 260,
    ESIM_URL_SETTING: 261,
    COMMLOG_BACKUP_TIME: 262,
    TIME_TO_RESTART: 263,
    LTE_CALIBRATION: 266,
    NR_CALIBRATION: 267,
    TR069_LOG_SWITCH: 268,
    SET_HASH: 269,
    SEND_AT: 270,
    TELECOM_DM: 271,
    VPN_CONFIG: 272,
    GET_ENDC_INFO: 273,
    GET_MOUNT_INFO: 274,
    DDOS_PROTECTION: 275,
    YOCTO_LOG_SWITCH: 276,
    NETWORKMODE_SWITCH: 277,
    WIRELESS_MACFILTER: 278,
    L2TPV3_CONFIG: 279,
    IPSEC_CONFIG: 281,
    GET_NEIGHBOR_INFO: 282,
    GET_SIP_CONFIG: 283,
    SIP_ADVANCED_SETTING: 284,
    WAN_WLAN_MODE: 285,
    CELL_PUNISHMENT: 286,
    UE_CONFIG: 287,
    RESCAN_NETWORK: 288,
    MUTIL_NETWORK_CONFIG: 289,
    MUTIL_VLAN_CONFIG: 290,
    MULTI_WAN_INFO: 291,
    NR_RSRP_THRESHOLD: 293,
    VOICE_ADVANCE_CONFIG: 294,
    VOICE_BASIC_CONFIG: 295,
    VOLTE_CONFIG: 296,
    VOIP_ADVANCED_CONFIG: 297,
    VOIP_CALL_CIPHER: 298,
    MQTT_CONFIG: 299,
    BROADBAND_SETTING: 302,
    MESH_SETTING: 314,
    NETWORK_TOPOLOPY: 315,
    SYNC_CLIENT_TIME: 321,
    LOG_INFO: 322,
    SET_WPS_SUCCESS_FLAG: 325,
    UNORMAL_REBOOT_SWITCH: 328,
    VXLAN_SETTING: 332,
    FW_UPNP_STATUS: 333,
    REMOTE_WEBPORT_SETTING: 334,
    IPV6_REMOTElOGIN_CONTROL: 335,
    WAN_LINK_DETECT_SETTING: 336,
    TRAFFIC_LIMIT_SETTING: 337,
    LINKDM_SETTING: 339,
    UNLOCK_SIM: 340,
    PCI_LOCK_ENABLE: 341,
    PCI_SCAN: 342,
    SNMP_SETTING: 344,
    FW_ACCESS_CONTROL_SIMPLE: 345,
    USSD_SEND: 350,
    DST_SETTING: 351,
    AEP_CONFIG: 467,
    GETGLOBESTATUS: 1003,
    DIDO_SETTING: 353,
    SET_PHONE_BOOK: 360,
    GET_PHONE_BOOK: 361,
    DEL_PHONE_BOOK: 362,
    CHANGE_ANTENNA_STATUS: 363,
    EXPORT_HWSECURITY_LOG: 366,
    FW_SWITCH_SETTING: 367,
    SLEEP_SETTINGS: 371,
    CONTROLS_WARM: 372,
    FOTA_SETTINGS: 373,
    SIM_UNLOCK_RESET: 374,
    NFC_SETTING: 376,
    LIGHTS_OUT_SETTING: 378,
    INDUSTRY_NETWORK_SET: 379,
    MODBUS_SLAVE_SET: 380,
    MODBUS_SLAVE_MODULE_SET: 381,
    MODBUS_SLAVE_SERIAL_DATA_DEBUG: 387,
    GET_STATUS_BAR: 394,
    GET_L2TPV3_STATUS: 398,
    GET_SYSTEM_PORT: 400,
    GET_AROUND_SSID: 412,
    PARENT_MODEL: 391,
    PARENTAL_CONTROL_SPEED: 382,
    PARENTAL_CONTROL_URL: 383,
    PARENTAL_CONTROL_TIME: 385,
    SCG_SETTING: 418,
    SERIAL_SETTING: 388,
    MQTT_SETTING: 403,
    SERIAL_DATA_OPER: 404,
    TRAFFIC_SETTING: 405,
    TRAFFIC_INTERFACE_LIST: 406,
    TRAFFIC_DAY_STAT: 407,
    TRAFFIC_HOUR_STAT: 408,
    GET_DASHBOARD_INDEX: 401,
    UIS_MANAGEMENT_SETTING: 416,
    WIFI_INIT_STATUS: 417,
    REMOTE_CTRL_SET: 419,
    REMOTE_CTRL_ACTION: 420,
    WCDMA_CALIBRATION: 421,
    WIRELESS_CONFIG_OD: 423,
    WIRELESS_ADVANCE_OD: 424,
    VPN_ONLY_ONE: 425,
    SIP_ALG: 426,
    CONE_NAT_SETTINGS: 427,
    ACCELERATE_PORT_EXCLUDE: 428,
    GNSS_CONFIG: 429,
    TR069_CONFIG_BRIEF: 434,
    MAIN_APN_MOVE_MULTI_APN: 437,
    IP_PACK: 438,
    GET_BATTERY_INFO: 439,
    OTG_ADAPTION: 441,
    DHCP_IPV6_SETTING: 443,
    DIAL_LOG: 454,
    DEV_LOG: 455,
    DEV_LOG_EXPORT: 456,
    DIAL_LOG_EXPORT: 457,
    MULTI_IP_PASSTHROUGH: 458,
    TKLAN_CONFIG: 459,
    SMART_WIFI_SETTING: 460,
    DATA_SELF_REGISTRATION: 461,
    WIRELESS_SELECT: 463,
    JT808_SETTING: 469,
    FOTA_SETTINGS_NEW: 480,
    DEVICE_SELF_CHECK: 484,
    RESET_FIRST_LOGIN_FLAG: 499,
    FAKE_POWEROFF: 514,
    USB_TUNING: 518,
    NETWORK_TOOLS_NAT_LIST: 524,
    PROBE_SETTINGS: 525,
    BATTERY_CHARGE_ENABLE: 527,
    PING_PONG_TOGGLING: 530,
    USB_NETWORK_PROTOCOL_SETTING: 547,
    SYS_WORKING_MODE: 548,
    TA_LIMIT_SW: 551,
    ESIM_RT_OPERATE: 555,
    WEAK_CELL_LIMIT: 565,
    FW_LEVEL_SETTING: 566,
    WLAN_GUEST_AUTO_OFF: 567,
    EXPORT_FULLDUMP_LOG: 568,
    GETDEVICEINFO: 1001,
    BATTERYINFO: 1014,
    CPURATIOINFO: 1015,
    GET_TEMPERATURE_GRAPH_DATA: 1016,
    WIFIDOG_TOOL_MNGR: 22001,
    OPENVPN_SETTING: 21022,
    UPLOAD_FILE_JIO: 21024
}
  , RENOOTTYPE = {
    NORMAL_REBOOT: 1,
    CONFIG_CHANGE: 2,
    RESTORE_SETTING: 3,
    RESTORE_REBOOT_CANCEL: 4,
    UPDATE_REBOOT: 5
}
  , Url = {
    LOGIN: "/login.html",
    INDEX: "/index.html",
    PASSWD: "/html/changePasswd.html",
    DEFAULT_CGI: "/cgi-bin/http.cgi"
}
  , MenuItem = {
    DASHBOARD_INFO: {
        cmd: RequestCmd.GET_DASHBOARD_INDEX,
        url: "html/dashboard/dashboardIndex.html"
    },
    SYS_INFO: {
        cmd: RequestCmd.SYS_INFO,
        url: "html/info/sysInfo.html"
    },
    WAN_INFO: {
        cmd: RequestCmd.WAN_INFO,
        url: "html/info/wan_index.html"
    },
    DHCP_INFO: {
        cmd: RequestCmd.DHCP_INFO,
        url: "html/info/lan_index.html"
    },
    WIFI24G_INFO: {
        cmd: RequestCmd.WIFI_INFO,
        url: "html/info/wifi24g_index.html"
    },
    WIFI5G_INFO: {
        cmd: RequestCmd.WIFI_INFO,
        url: "html/info/wifi5g_index.html"
    },
    BATTERY_INFO: {
        cmd: RequestCmd.GET_BATTERY_INFO,
        url: "html/info/batteryInfo.html"
    },
    DEVICE_INFO: {
        cmd: RequestCmd.SYS_INFO,
        url: "html/info/deviceInfo.html"
    },
    CALIBRATION_INFO: {
        cmd: RequestCmd.LTE_CALIBRATION,
        url: "html/info/calibrationInfo.html"
    },
    TRAFFIC_SETTING: {
        cmd: RequestCmd.TRAFFIC_LIMIT_SETTING,
        url: "html/config/traffic_settingIndex.html"
    },
    SCG_SETTING: {
        cmd: RequestCmd.SCG_SETTING,
        url: "html/config/scgIndex.html"
    },
    ACCELERATE_PORT_EXCLUDE: {
        cmd: RequestCmd.ACCELERATE_PORT_EXCLUDE,
        url: "html/config/netPasIndex.html"
    },
    WIRELESS_SWITCH: {
        cmd: RequestCmd.WIRELESS5G_CONFIG,
        url: "html/config/wirelessSwitchIndex.html"
    },
    WIRELESS_SELECT: {
        cmd: RequestCmd.WIRELESS_SELECT,
        url: "html/config/wifiSelectIndex.html"
    },
    WIRELESS_CONFIG: {
        cmd: RequestCmd.WIRELESS_CONFIG,
        url: "html/config/wlan24gIndex.html"
    },
    WIRELESS_ADVANCE: {
        cmd: RequestCmd.WIRELESS_ADVANCE,
        url: "html/config/wlan24gAdvanceIndex.html"
    },
    WIRELESS_CONFIG_OD: {
        cmd: RequestCmd.WIRELESS_CONFIG_OD,
        url: "html/config/wlan24gODIndex.html"
    },
    WIRELESS_ADVANCE_OD: {
        cmd: RequestCmd.WIRELESS_ADVANCE_OD,
        url: "html/config/wlan24gAdvanceODIndex.html"
    },
    WIRELESS5G_CONFIG: {
        cmd: RequestCmd.WIRELESS5G_CONFIG,
        url: "html/config/wlan5gIndex.html"
    },
    WIRELESS5G_ADVANCE: {
        cmd: RequestCmd.WIRELESS5G_ADVANCE,
        url: "html/config/wlan5gAdvanceIndex.html"
    },
    WPS_CONFIG: {
        cmd: RequestCmd.WPS_CONFIG,
        url: "html/config/wirelessWpsSettingIndex.html"
    },
    WIRELESS_MACFILTER: {
        cmd: RequestCmd.WIRELESS_MACFILTER,
        url: "html/config/wifiListIndex.html"
    },
    NFC_SETTING: {
        cmd: RequestCmd.NFC_SETTING,
        url: "html/config/nfcIndex.html"
    },
    SMART_WIFI_SETTING: {
        cmd: RequestCmd.SMART_WIFI_SETTING,
        url: "html/config/smartwifiIndex.html"
    },
    WLAN_GUEST_AUTO_OFF: {
        cmd: RequestCmd.WLAN_GUEST_AUTO_OFF,
        url: "html/config/guestWifiAutoOffIndex.html"
    },
    NETWORK_CONFIG: {
        cmd: RequestCmd.NETWORK_CONFIG,
        url: "html/config/dhcpIndex.html"
    },
    VLAN_CONFIG: {
        cmd: RequestCmd.VLAN_CONFIG,
        url: "html/config/vlanIndex.html"
    },
    ROUTER_TABLE: {
        cmd: RequestCmd.ROUTER_TABLE,
        url: "html/advance/routeIndex.html"
    },
    CONTROLS_WARM: {
        cmd: RequestCmd.CONTROLS_WARM,
        url: "html/config/controlsWarmIndex.html"
    },
    IP_PASSTHROUGH: {
        cmd: RequestCmd.IP_PASSTHROUGH,
        url: "html/advance/ipPassIndex.html"
    },
    USSD_SET: {
        cmd: RequestCmd.USSD_SET,
        url: "html/manage/ussdIndex.html"
    },
    APN_CONFIG: {
        cmd: RequestCmd.APN_CONFIG,
        url: "html/config/apnConfigIndex.html"
    },
    PIN_CONFIG: {
        cmd: RequestCmd.WAN_INFO,
        url: "html/advance/pinSetIndex.html"
    },
    ESIM_MANAGE: {
        cmd: RequestCmd.WAN_INFO,
        url: "html/advance/esimIndex.html"
    },
    DISPLAY_SOLUTION_MODE: {
        cmd: RequestCmd.DISPLAY_SOLUTION_MODE,
        url: "html/advance/displaySolutionModeIndex.html"
    },
    SLICE_CONIFIG: {
        cmd: RequestCmd.SLICE_CONIFIG,
        url: "html/advance/sliceConfigurationIndex.html"
    },
    CELL_PUNISHMENT: {
        cmd: RequestCmd.CELL_PUNISHMENT,
        url: "html/advance/cellPunishmentIndex.html"
    },
    UE_CONFIG: {
        cmd: RequestCmd.UE_CONFIG,
        url: "html/advance/ueConfigIndex.html"
    },
    NETWORK_SET: {
        cmd: RequestCmd.WAN_INFO,
        url: "html/advance/networkSetIndex.html"
    },
    NETWORK_SERVICE: {
        cmd: RequestCmd.NETWORK_SERVICE,
        url: "html/config/networkServiceConfig.html"
    },
    GRE_VPN: {
        cmd: RequestCmd.GRE_VPN,
        url: "html/config/greVpn.html"
    },
    DDNS_CONFIG: {
        cmd: RequestCmd.NETWORK_SERVICE,
        url: "html/ddns/ddnsIndex.html"
    },
    PPTP_VPN: {
        cmd: RequestCmd.PPTP_VPN,
        url: "html/config/pptpVpn.html"
    },
    VOICE_CONFIG: {
        cmd: RequestCmd.VOICE_ADVANCE_CONFIG,
        url: "html/voice/voiceIndex.html"
    },
    SIP_CONFIG: {
        cmd: RequestCmd.GET_SIP_CONFIG,
        url: "html/config/voiceSettingIndex.html"
    },
    SPEED_TEST: {
        cmd: RequestCmd.SPEED_TEST,
        url: "html/config/speedTest.html"
    },
    TR069_SETTING: {
        cmd: RequestCmd.TR069_CONFIG,
        url: "html/advance/tr069Index.html"
    },
    SLEEP_SETTINGS: {
        cmd: RequestCmd.SLEEP_SETTINGS,
        url: "html/config/sleepSettingsIndex.html"
    },
    SNMP_SETTING: {
        cmd: RequestCmd.SNMP_SETTING,
        url: "html/advance/snmpIndex.html"
    },
    VPN_SETTING: {
        cmd: RequestCmd.L2TP_SETTING,
        url: "html/advance/vpnIndex.html"
    },
    ONENET_SETTING: {
        cmd: RequestCmd.ONENET_SETTING,
        url: "html/advance/onenetIndex.html"
    },
    MESH: {
        cmd: RequestCmd.MESH_SETTING,
        url: "html/manage/meshSettingIndex.html"
    },
    IP_PACK: {
        cmd: RequestCmd.IP_PACK,
        url: "html/advance/ipPack.html"
    },
    OTG_ADAPTION: {
        cmd: RequestCmd.OTG_ADAPTION,
        url: "html/advance/otgAdaption.html"
    },
    USB_TUNING: {
        cmd: RequestCmd.USB_TUNING,
        url: "html/advance/usbTuningIndex.html"
    },
    CHANGE_ANTENNA_STATUS: {
        cmd: RequestCmd.MESH_SETTING,
        url: "html/sys/antennaChange.html"
    },
    ADVANCED_CONFIG: {
        cmd: RequestCmd.DDNS_DATA,
        url: "html/advance/advancedConfig.html"
    },
    UNLOCK_SIM: {
        cmd: RequestCmd.UNLOCK_SIM,
        url: "html/advance/unlockIndex.html"
    },
    SIP_ALG: {
        cmd: RequestCmd.SIP_ALG,
        url: "html/config/sipAlg.html"
    },
    LTE_LOCK_FREQUENCY: {
        cmd: RequestCmd.LTE_AT,
        url: "html/config/lteLockFrequencyConfig.html"
    },
    LTE_LOCK_FREQUENCY_P500: {
        cmd: RequestCmd.LTE_AT,
        url: "html/config/lteLockFrequencyConfigP500.html"
    },
    LTE_LOCK_FREQUENCY_SIM5360: {
        cmd: RequestCmd.LTE_AT,
        url: "html/config/lteLockFrequencyConfigSim5360.html"
    },
    LTE_LOCK_FREQUENCY_ZTE: {
        cmd: RequestCmd.LTE_AT,
        url: "html/config/lteLockFrequencyConfigZTE.html"
    },
    LTE_AT: {
        cmd: RequestCmd.LTE_AT,
        url: "html/config/lteATConfig.html"
    },
    FW_RULE: {
        cmd: RequestCmd.PORT_FILTER,
        url: "html/firewall/ipFilterIndex.html"
    },
    URL_FILTER: {
        cmd: RequestCmd.URL_FILTER,
        url: "html/firewall/urlFilterIndex.html"
    },
    FW_MAC_FILTER: {
        cmd: RequestCmd.MAC_FILTER,
        url: "html/firewall/macIndex.html"
    },
    FW_IP_MAC_BINDING: {
        cmd: RequestCmd.IP_MAC_BINDING,
        url: "html/firewall/ipMacBindingIndex.html"
    },
    FW_URL_FILTER: {
        cmd: RequestCmd.URL_FILTER,
        url: "html/firewall/urlFilterIndex.html"
    },
    FW_PORT_FORWARD: {
        cmd: RequestCmd.OTHER_FILTER,
        url: "html/firewall/portForwardIndex.html"
    },
    FW_ACL_FILTER: {
        cmd: RequestCmd.ACL_FILTER,
        url: "html/firewall/aclFilterIndex.html"
    },
    FW_SPEED_LIMIT: {
        cmd: RequestCmd.SPEED_LIMIT,
        url: "html/firewall/speedLimitIndex.html"
    },
    FW_UPNP: {
        cmd: RequestCmd.FW_UPNP,
        url: "html/firewall/upnpIndex.html"
    },
    FW_DMZ: {
        cmd: RequestCmd.GET_DMZ,
        url: "html/firewall/dmzIndex.html"
    },
    FW_SWITCH: {
        cmd: RequestCmd.FW_SWITCH_SETTING,
        url: "html/firewall/fw_switch_setting.html"
    },
    FW_LEVEL: {
        cmd: RequestCmd.FW_SWITCH_SETTING,
        url: "html/firewall/fw_level_setting.html"
    },
    REMOTE_WEB_PORT_SETTING: {
        cmd: RequestCmd.GET_DMZ,
        url: "html/firewall/remoteWebPortSettingIndex.html"
    },
    SIMPLE_ACCESS_CONTROL: {
        cmd: RequestCmd.FW_ACCESS_CONTROL,
        url: "html/firewall/accessControlIndex.html"
    },
    PARENT_MODEL: {
        cmd: RequestCmd.PARENT_MODEL,
        url: "html/parent/parentControlIndex.html"
    },
    PARENTAL_CONTROL_URL: {
        cmd: RequestCmd.PARENTAL_CONTROL_URL,
        url: "html/parent/parentUrlIndex.html"
    },
    PARENTAL_CONTROL_TIME: {
        cmd: RequestCmd.PARENTAL_CONTROL_TIME,
        url: "html/parent/parentTimeIndex.html"
    },
    PARENTAL_CONTROL_SPEED: {
        cmd: RequestCmd.PARENTAL_CONTROL_SPEED,
        url: "html/parent/parentSpeedIndex.html"
    },
    SMS_RECEIVE: {
        cmd: RequestCmd.SMS_INFO,
        url: "html/sms/smsInbox.html"
    },
    SMS_SEND: {
        cmd: RequestCmd.SMS_INFO,
        url: "html/sms/smsOutbox.html"
    },
    SMS_SETTING: {
        cmd: RequestCmd.SMS_INFO,
        url: "html/sms/smsSetting.html"
    },
    PHONE_BOOK: {
        cmd: RequestCmd.GET_PHONE_BOOK,
        url: "html/phoneBook/phoneBook.html"
    },
    SYS_SET: {
        cmd: RequestCmd.CHANGE_PASSWD,
        url: "html/sys/sysConfigIndex.html"
    },
    TIME_TO_RESTART: {
        cmd: RequestCmd.TIME_TO_RESTART,
        url: "html/sys/timeToRestart.html"
    },
    INTELLIGENT_SETTING: {
        cmd: RequestCmd.TIME_TO_RESTART,
        url: "html/sys/intelligentSettingIndex.html"
    },
    SYS_CHECK: {
        cmd: RequestCmd.SYS_INFO,
        url: "html/check/checkIndex.html"
    },
    SYS_LOG: {
        cmd: RequestCmd.SYS_LOG,
        url: "html/sys/sysLogIndex.html"
    },
    EXPORT_LOG: {
        cmd: RequestCmd.EXPORT_LOG,
        url: "html/manage/exportLog.html"
    },
    LOG_INFO: {
        cmd: RequestCmd.LOG_INFO,
        url: "html/manage/logInfo.html"
    },
    MODULE_LOG: {
        cmd: RequestCmd.MODULE_LOG,
        url: "html/manage/moduleLog.html"
    },
    SYS_UPDATE: {
        cmd: RequestCmd.SYS_UPDATE,
        url: "html/update/sysUpdate.html"
    },
    SYS_AUTO_UPGRADE: {
        cmd: RequestCmd.SYS_AUTO_UPGRADE,
        url: "html/update/sysAutoUpdate.html"
    },
    CONFIG_UPDATE: {
        cmd: RequestCmd.CONFIG_UPDATE,
        url: "html/update/configUpdate.html"
    },
    CHECKING_STATUS: {
        cmd: RequestCmd.LTE_AT,
        url: "html/manage/checkingStatus.html"
    },
    SEND_AT: {
        cmd: RequestCmd.SEND_AT,
        url: "html/manage/sysAt.html"
    },
    NETWORK_TOOLS: {
        cmd: RequestCmd.NETWORK_TOOLS,
        url: "html/tools/toolsIndex.html"
    },
    DEVICE_SELF_CHECK: {
        cmd: RequestCmd.DEVICE_SELF_CHECK,
        url: "html/manage/deviceSelfCheck.html"
    },
    SYS_REBOOT: {
        cmd: RequestCmd.SYS_REBOOT,
        url: ""
    },
    DDOS_PROTECTION: {
        cmd: RequestCmd.DDOS_PROTECTION,
        url: "html/firewall/ddosIndex.html"
    },
    INDUSTRY_SETTING: {
        cmd: RequestCmd.INDUSTRY_SETTING,
        url: "html/industry/serialSettingIndex.html"
    },
    DIDO_SETTING: {
        cmd: RequestCmd.DIDO_SETTING,
        url: "html/industry/didoSettingIndex.html"
    },
    SERIAL_SETTING: {
        cmd: RequestCmd.SERIAL_SETTING,
        url: "html/industry/serialSettingIndex.html"
    },
    MQTT_SETTING: {
        cmd: RequestCmd.MQTT_SETTING,
        url: "html/industry/mqttSettingIndex.html"
    },
    TRAFFIC_STAT: {
        cmd: RequestCmd.TRAFFIC_STAT,
        url: "html/industry/trafficStatIndex.html"
    },
    GNSS_SETTING: {
        cmd: RequestCmd.GNSS_SETTING,
        url: "html/industry/GnssSettingIndex.html"
    },
    FOTA_SETTINGS: {
        cmd: RequestCmd.FOTA_SETTINGS_NEW,
        url: "html/update/fota.html"
    },
    CERTIFICATE_MARK: {
        cmd: RequestCmd.GETDEVICEINFO,
        url: "html/manage/certificationMark.html"
    }
}
  , SortDirection = {
    ASC: "asc",
    DESC: "desc"
}
  , UpdateType = {
    CLIENT: "CLIENT",
    SERVER: "SERVER"
}
  , Network = {
    LAN: "LAN",
    WAN: "WAN"
}
  , JSONMethod = {
    GET: "GET",
    POST: "POST"
}
  , ConvertUtil = {
    ip4ToNum: function(e) {
        var t, n, i = this.ip4ToBytes(e), a = "0x";
        for (n = 0; n < 4; n++)
            1 == (t = i[n].toString(16)).length && (t = "0" + t),
            a += t;
        return parseInt(a, 16)
    },
    ip4ToBytes: function(e) {
        var t = e.split(".")
          , n = t.length;
        if (4 != n)
            return null;
        for (var i, a = [], s = 0; s < n; s++) {
            if (i = parseInt(t[s], 10),
            isNaN(i) || i < 0 || i > 255)
                return null;
            a[s] = i
        }
        return a
    },
    parseSingalLevel: function(e) {
        var t = [-94, -80, -75, -70, -65]
          , n = t.length
          , i = parseInt(e, 10)
          , a = 1;
        if (!isNaN(i))
            if (i >= t[n - 1])
                a = n;
            else
                for (var s = 0; s < n; s++)
                    if (i < t[s]) {
                        a = s;
                        break
                    }
        return {
            level: a,
            desc: ["无信号", "非常差", "差", "一般", "好", "非常好"][a]
        }
    },
    parseHex: function(e) {
        if (!e)
            return "0000";
        for (var t = ["0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"], n = new StringBuilder, i = 0, a = e.length; i < a; i++)
            n.append(t[parseInt(e.charAt(i), 16)]);
        return n.toString()
    },
    parseFindAp: function(e, t) {
        var n = '"' + t + '":["'
          , i = e.indexOf(n);
        if (i > 0) {
            var a = e.substring(i + n.length);
            if ((i = a.indexOf('"]')) > 0) {
                for (var s = a.substring(0, i).split('","'), r = 0; r < s.length; r++)
                    s[r] = s[r].replaceQuote();
                return s
            }
        }
        return []
    },
    parseUptime: function(e, t) {
        var n = ""
          , i = "";
        return /^(.*)up(.*)\,\s*load\s*average\:(.*)$/.test(e) && ((n = RegExp.$2.replace("days", "day").replace("day", t.day).replace(":", t.hour)).indexOf("min") > 0 ? n = n.replace("min", t.min) : n += t.min,
        i = RegExp.$3),
        {
            runTime: n,
            runStatus: i
        }
    },
    parseUptime2: function(e) {
        if (!e)
            return "-";
        var t = Math.floor(e / 86400)
          , n = Math.floor((e - 24 * t * 3600) / 3600)
          , i = Math.floor((e - 24 * t * 3600 - 3600 * n) / 60)
          , a = e - 24 * t * 3600 - 3600 * n - 60 * i;
        return t > 0 ? t + DOC.unit.day + "," + n + ":" + i + ":" + a : n + ":" + i + ":" + a
    }
}
  , SysUtil = {
    deviceName: null,
    rebootMessage: PROMPT.tips.rebootMessage,
    defaultRebootSettings: {
        rebootType: RENOOTTYPE.CONFIG_CHANGE,
        msg: PROMPT.saving.success,
        callback: null,
        hideConfirm: !1
    },
    reboot: function(e) {
        var t = $.extend({}, SysUtil.defaultRebootSettings, e)
          , n = t.msg.trim()
          , i = n.charAt(n.length - 1);
        ".,?!:;。？：！，；".indexOf(i) < 0 && (n += ", "),
        SysUtil.rebootDevice(t.rebootType, n + SysUtil.rebootMessage, t.callback, t.hideConfirm)
    },
    reboot2: function(e) {
        var t = $.extend({}, SysUtil.defaultRebootSettings, e)
          , n = t.msg.trim()
          , i = n.charAt(n.length - 1);
        ".,?!:;。？：！，；".indexOf(i) < 0 && (n += ", "),
        SysUtil.rebootDevice2(t.rebootType, n + SysUtil.rebootMessage, t.callback, t.hideConfirm)
    },
    rebootDevice: function(e, t, n, i) {
        e || (e = RENOOTTYPE.CONFIG_CHANGE),
        t || (t = this.rebootMessage),
        i || fyConfirmMsg(t, function() {
            $.isFunction(n) && n(!0),
            fyAlertMsgLoading(PROMPT.status.rebooting + "," + CHECK.format.waiting),
            setTimeout(function() {
                Page.isReboot = "1",
                Page.postJSON({
                    json: {
                        cmd: RequestCmd.SYS_REBOOT,
                        rebootType: e,
                        method: JSONMethod.POST
                    },
                    success: function() {},
                    complete: function() {
                        setTimeout(function e() {
                            Page.postJSON({
                                json: {
                                    cmd: RequestCmd.GET_SYS_STATUS
                                },
                                success: function() {
                                    setTimeout(function() {
                                        location.href = Page.getUrl(Url.LOGIN)
                                    }, 3e3)
                                },
                                error: function() {
                                    setTimeout(e, 3e3)
                                }
                            })
                        }, 5e3)
                    }
                })
            }, 3e3)
        })
    },
    rebootDevice2: function(e, t, n, i) {
        if (e || (e = RENOOTTYPE.CONFIG_CHANGE),
        t || (t = this.rebootMessage),
        !i) {
            $.isFunction(n) && n(!0),
            fyAlertMsgLoading(PROMPT.status.rebooting + "," + CHECK.format.waiting),
            setTimeout(function() {
                Page.isReboot = "1",
                Page.postJSON({
                    json: {
                        cmd: RequestCmd.SYS_REBOOT,
                        rebootType: e,
                        method: JSONMethod.POST
                    },
                    success: function() {},
                    complete: function() {
                        setTimeout(function e() {
                            Page.postJSON({
                                json: {
                                    cmd: RequestCmd.GET_SYS_STATUS
                                },
                                success: function() {
                                    setTimeout(function() {
                                        location.href = Page.getUrl(Url.LOGIN)
                                    }, 3e3)
                                },
                                error: function() {
                                    setTimeout(e, 3e3)
                                }
                            })
                        }, 5e3)
                    }
                })
            }, 3e3)
        }
    },
    restoreRebootCancel: function() {
        fyAlertMsgLoading(PROMPT.status.rebooting + "," + CHECK.format.waiting),
        setTimeout(function() {
            Page.postJSON({
                json: {
                    cmd: RequestCmd.SYS_REBOOT,
                    rebootType: RENOOTTYPE.RESTORE_REBOOT_CANCEL,
                    method: JSONMethod.POST
                },
                success: function() {}
            })
        }, 1e3)
    },
    showProgress: function(e, t, n, i) {
        var a = $("#mask")
          , s = $("#progress_box")
          , r = $("#progress_status")
          , l = $("#progress_info")
          , o = document.documentElement.clientHeight
          , d = document.documentElement.clientWidth;
        a.height(o),
        a.show(),
        s.show(),
        SysUtil.setBoxStyle(s, d, o);
        var m, c = 1, u = 2, _ = 10 * e, T = $("#progress_bar").width();
        !function e() {
            n() ? c += parseInt((_ - c) / u) + 1 : (m = _ / c) >= 3 ? c += 3 : m >= 2 ? c += 2 : m > 1 && _ - c > u && c++,
            c <= _ ? (l.text(t + DOC.comma + PROMPT.status.progress + " " + parseInt(100 * c / _) + "%"),
            r.width(parseInt(T * c / _)),
            setTimeout(e, 250)) : i()
        }()
    },
    showProgress_2: function(e, t, n) {
        var i = $("#mask")
          , a = $("#progress_box")
          , s = $("#progress_status")
          , r = $("#progress_info")
          , l = document.documentElement.clientHeight
          , o = document.documentElement.clientWidth;
        i.height(l),
        i.show(),
        a.show(),
        SysUtil.setBoxStyle(a, o, l);
        var d = 0
          , m = $("#progress_bar").width();
        !function i() {
            d = t(),
            r.text(e + DOC.comma + PROMPT.status.progress + " " + d + "%"),
            s.width(parseInt(m * d / 100)),
            d < 100 ? setTimeout(i, 500) : n()
        }()
    },
    setBoxStyle: function(e, t, n) {
        e.css({
            left: parseInt(((t || document.documentElement.clientWidth) - e.width()) / 2, 10) + "px",
            top: parseInt(((n || document.documentElement.clientHeight) - e.height()) / 2, 10) + "px"
        })
    },
    parseJSON: function(e) {
        var t = e.indexOf("{");
        return t < 0 ? {} : (t > 0 && (e = e.substring(e.indexOf("{"))),
        JSON.parse(e))
    },
    getModule: function() {
        if (Page.imei && "" != Page.modelVersion && "NULL" != Page.modelVersion)
            return !0;
        var e = 0;
        return function t() {
            Page.postJSON({
                json: {
                    cmd: RequestCmd.DEVICE_VERSION_INFO
                },
                success: function(n) {
                    Page.module = n.module,
                    Page.modelVersion = n.modversion,
                    Page.imei = n.imei,
                    ("" == n.modversion || "NULL" == n.modversion || e++ < 20) && setTimeout(t, 1e4)
                }
            })
        }(),
        !1
    },
    processMsg: function(e) {
        "NO_AUTH" == e ? (AlertUtil.alertMsg(PROMPT.status.noAuth),
        location.href = Page.getUrl(Url.LOGIN)) : AlertUtil.alertMsg(e)
    },
    upload: function(e, t, n, i) {
        var a = String.format("{0}?cmd={1}&method=POST&sessionId={2}&language={3}&token={4}", Url.DEFAULT_CGI, RequestCmd.SYS_UPDATE, sessionStorage.sessionId, Page.language, sessionStorage.token)
          , s = null;
        e.ajaxSubmit({
            url: a,
            type: "POST",
            dataType: "json",
            beforeSubmit: function() {
                var e = t.val();
                if (0 == e.length)
                    return AlertUtil.alertMsg(CHECK.required.uploadFile),
                    !1;
                if (/[\\\/]/.test(e)) {
                    var n = e.match(/(.*)?[\\\/](.*)/);
                    e = n[2]
                }
                return confirm(PROMPT.confirm.uploadFile + e) ? (SysUtil.showProgress(ProgressTime.UPLOAD_FILE, PROMPT.status.uploading, function() {
                    return null != s
                }, function() {
                    s.success ? AlertUtil.alertMsg(PROMPT.status.uploadSuccess) : "object" == typeof s.message ? SysUtil.processMsg("Error") : SysUtil.processMsg(s.message),
                    $.isFunction(i) && i(e)
                }),
                !0) : (i(""),
                !1)
            },
            success: function(e, t) {
                s = e
            },
            error: function(e, t) {
                s = {
                    success: !1,
                    message: e
                }
            }
        })
    }
}
  , FormatUtil = {
    formatValue: function(e) {
        return e && "NULL" != e ? e : ""
    },
    formatField: function(e, t) {
        return t = t || "",
        "NULL" == e ? Page.isNULLToSpace ? "" : String.format('<span class="fail">{0}</span>', DOC.status.getFailed) : String.format("{0}{1}", e, t).replaceQuote()
    },
    formatNetState: function(e) {
        return e && "" != e.trim() && "NULL" != e ? this.formatField(e) : "-"
    },
    KB: 1,
    MB: 1024,
    GB: 1048576,
    formatByteSize: function(e) {
        return e < this.MB ? (e / this.KB).toFixed(2) + " KB" : e < this.GB ? (e / this.MB).toFixed(2) + " MB" : (e / this.GB).toFixed(2) + " GB"
    },
    formatMaskBit: function(e) {
        for (var t = e.split("."), n = 0, i = 0; i < 4 && "0" !== t[i]; i++)
            switch (t[i]) {
            case "255":
                n += 8;
                break;
            case "254":
                n += 7;
                break;
            case "252":
                n += 6;
                break;
            case "248":
                n += 5;
                break;
            case "240":
                n += 4;
                break;
            case "224":
                n += 3;
                break;
            case "192":
                n += 2;
                break;
            case "128":
                n += 1;
                break;
            default:
                n = 0
            }
        return n
    },
    ipChangeNum: function(e) {
        for (var t = "", n = e.split("."), i = 0; i < 4; i++) {
            for (var a = n[i], s = 3 - a.length, r = 0; r < s; r++)
                a = "0" + a;
            t += a
        }
        return parseInt(t)
    }
}
  , Page = {
    wlan5g_preferred: "0",
    networkMode: "1",
    logoPath: "",
    theme: "",
    priorityStrategy: "",
    aeraId: "",
    isTest: !1,
    isNULLToSpace: !1,
    connectStatus: !0,
    currentId: 0,
    sessionId: "",
    webPageFlag: "0",
    language: DOC.language || "cn",
    timer: null,
    getHash: "",
    onenet: "0",
    dm_platform: "0",
    signal_lvl: "",
    network_type_str: "",
    wifidog_wifi_sw: "0",
    isReboot: "0",
    getSupportlanguageList: [{
        value: "en",
        name: "English",
        index: 1
    }, {
        value: "cn",
        name: "中文",
        index: 0
    }, {
        value: "th",
        name: "ไทย",
        index: 2
    }, {
        value: "el",
        name: "Español",
        index: 3
    }, {
        value: "por",
        name: "Português",
        index: 4
    }, {
        value: "it",
        name: "Italiano",
        index: 5
    }, {
        value: "ar",
        name: "عربي",
        index: 6
    }, {
        value: "fr",
        name: "Français",
        index: 7
    }, {
        value: "in",
        name: "Indonesia",
        index: 14
    }, {
        value: "sl",
        name: "Slovenia",
        index: 15
    }, {
        value: "am",
        name: "አማርኛ",
        index: 16
    }],
    current_card_type: "0",
    esim_show: "0",
    build_type: !1,
    iad_ready_status: "0",
    real_device: "",
    idu_dev_type: "",
    smsSw: "0",
    level: "3",
    fw_switch: "1",
    isDowngrade: !1,
    allowCopyPaste: !1,
    dhcpPortList: ["LAN1", "LAN2", "LAN3", "LAN4", "LAN5", "LAN6", "LAN7", "LAN8", "2.4 GHz SSID1", "2.4 GHz SSID2", "2.4 GHz SSID3", "2.4 GHz SSID4", "2.4 GHz SSID5", "2.4 GHz SSID6", "2.4 GHz SSID7", "2.4 GHz SSID8", "5 GHz SSID1", "5 GHz SSID2", "5 GHz SSID3", "5 GHz SSID4", "5 GHz SSID5", "5 GHz SSID6", "5 GHz SSID7", "5 GHz SSID8"],
    downloadFile: function(e, t) {
        var n = new XMLHttpRequest;
        n.open("get", e),
        n.responseType = "blob",
        n.send(),
        n.onload = function() {
            if (200 === this.status || 304 === this.status) {
                var e = new FileReader;
                e.readAsDataURL(this.response),
                e.onload = function() {
                    const e = document.createElement("a");
                    e.style.display = "none",
                    e.href = this.result,
                    e.download = t,
                    document.body.appendChild(e),
                    e.click(),
                    document.body.removeChild(e)
                }
            }
        }
    },
    transSecondTime: function(e) {
        e = parseInt(e);
        var t = Math.floor(e / 3600)
          , n = Math.floor((e - 3600 * t) / 60)
          , i = e - 3600 * t - 60 * n
          , a = ["EG3141", "EG3310", "IOT002", "EG3121"].includes(Page.aeraId);
        return e > 3600 ? a ? t + DOC.unit.hour + " " + n + DOC.unit.min + " " + i + DOC.unit.second : t + ":" + n + ":" + i : a ? n + DOC.unit.min + " " + i + DOC.unit.second : n + ":" + i
    },
    requireChangeLang: function(e) {
        return Page.getSupportlanguageList.map(function(e) {
            return e.value
        }).indexOf(e) > -1 && "cn" != e
    },
    isChinese: function() {
        return "cn" == Page.language
    },
    alertTimes: 0,
    defaultAlertTimes: 100,
    $iframe: null,
    menuItem: null,
    module: "",
    itemHide: "0000",
    itemDisable: "0000",
    isItemHide: function(e) {
        var t = Page.itemHide
          , n = e.index;
        return n >= t.length ? e.emptyHide : t.charAt(t.length - 1 - n) == e.hideFlag
    },
    isSupported: function(e, t) {
        if (t) {
            var n = Page.supported || "0000";
            return !(e >= n.length) && "1" == n.charAt(n.length - 1 - e)
        }
        var i = Page.unsupported || "0000";
        return e >= i.length || "0" == i.charAt(i.length - 1 - e)
    },
    setMenu: function(e, t) {
        Page.isItemHide(t) ? $(e).detach() : $(e).show()
    },
    parseHex: function(e) {
        if (!e)
            return ["0000"];
        for (var t = ["0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"], n = [], i = 0, a = e.length; i < a; i++)
            n.push(t[parseInt(e.charAt(i), 16)]);
        return n
    },
    parseHexPageHide: function(e) {
        if (!e)
            return ["00"];
        for (var t = ["00", "01", "02", "03", "10", "11", "12", "13", "20", "21", "22", "23", "30", "31", "32", "33"], n = [], i = 0, a = e.length; i < a; i++)
            n.push(t[parseInt(e.charAt(i), 16)]);
        return n.join("").split("").reverse()
    },
    pageHideCheck: function(e) {
        var t = Page.webPageFlag;
        return "1" == t[e] ? "1" == Page.level || "2" == Page.level : "2" == t[e] ? "1" == Page.level || "2" == Page.level || "3" == Page.level : "3" == t[e] || "1" == Page.level
    },
    pageHideCheck2: function(e) {
        var t = Page.webPageFlag;
        return "1" == t[e] ? "1" == Page.level || "2" == Page.level : "2" == t[e] ? "1" == Page.level || "2" == Page.level || "3" == Page.level : "3" != t[e] || "1" == Page.level
    },
    pageHideCheck3: function(e) {
        var t = Page.webPageFlag;
        return "1" == t[e] || ("2" == t[e] ? "1" == Page.level || "2" == Page.level || "3" == Page.level : "3" == t[e] ? "1" == Page.level : "1" == Page.level || "2" == Page.level)
    },
    formatMAC: e => e ? e.match(/[0-9a-f]{2}/gi).join(":").toUpperCase() : e,
    createForm: function(e) {
        if (e && !(e.length <= 0)) {
            var t = document.createElement("form");
            $(t).attr("id", e[0]),
            $(t).appendTo("#" + e[1]),
            $("#" + e[2]).appendTo(t)
        }
    },
    createForm2: function(e) {
        if (e && !(e.length <= 0)) {
            var t = document.createElement("form");
            $(t).attr("id", e[0]),
            $(t).appendTo("#" + e[1]),
            $("#" + e[2]).appendTo(t)
        }
    },
    initPage: function(e) {
        var t = $("#header");
        Page.hasGetLogo || (Page.postJSON({
            json: {
                cmd: RequestCmd.INIT_PAGE
            },
            success: function(e) {
                Page.isShowRedCapNetwork = "1" == e.redcap_network_capability,
                Page.wifi_pwd_forbid = e.wifi_pwd_forbid,
                Page.wifi_ssid_forbid = e.wifi_ssid_forbid,
                Page.wlan5g_preferred = e.wlan5g_preferred,
                Page.build_type = "debug" == e.build_type || -1 != e.fwversion_type.indexOf("dbg") || -1 != e.fwversion_type.indexOf("debug"),
                Page.hasGetLogo = !0,
                Page.enableChannelOneToFour = "yes" == e.channelOneToFour,
                Page.disableChannelEnds = "yes" == e.hideChannel12And13,
                Page.dfsSwitch = "1" == e.dfsSwitch,
                Page.countryCode = e.countryCode,
                Page.roamingEnable = e.roamingEnable;
                var n = (e.web_logo_path || "").trim();
                if ("NULL" != n && n.length > 0) {
                    var i = String.format("url(/images/{0})", n);
                    $("<style>").prop("type", "text/css").html(`\n              #header::before {\n                content: '';\n                background-image: ${i};\n                background-repeat: no-repeat;\n\t\t\t\t\t\t\t  background-position: 0 center;\n\t\t\t\t\t\t\t  background-size: contain;\n                top: 4px;\n                left: 20px;\n                right: 0;\n                bottom: 4px;\n            \t\theight: auto;\n                position: absolute;\n              }\n            `).appendTo("head")
                } else
                    "1" == e.isLogoExists ? t.addClass("header_logo") : t.addClass("header_default");
                "NULL" == e.deviceType && (e.deviceType = ""),
                Page.displayedVersion = e.displayedVersion,
                "NULL" == Page.displayedVersion && (Page.displayedVersion = "")
            }
        }),
        e || Page.killSearchPlmn()),
        setTimeout( () => {
            var n = document.getElementsByTagName("body");
            n = n && n.length > 0 ? document.getElementsByTagName("body")[0].clientWidth : document.documentElement.clientWidth;
            var i = Math.max(n, 1e3)
              , a = Math.max(document.documentElement.clientHeight - 71 - 31, 450);
            $("#main").height(a),
            $("#left").height(a),
            $("#left_m").height(a - 30),
            Page.footerImg = "/images/mexico/home_footer.png";
            var s = $("#right");
            s.css({
                "padding-top": "15px",
                left: $("#left").width() + "px"
            });
            var r = i - (l = $("#left").width() + 1);
            if (s.width(r),
            s.height(a - 15),
            e) {
                var l = (i - 402) / 2 - 400;
                l = Math.max(l, 0),
                $("#login").css({
                    left: l + "px",
                    top: (a - 302) / 2 - 50 + "px"
                }),
                $("#check_box_left .check_info").css({
                    width: r / 2 - 40 + "px"
                }),
                $("#check_box_right .check_info").css({
                    width: r / 2 - 30 + "px"
                }),
                $("#device_check").css({
                    left: "20px",
                    width: r - 40 + "px",
                    height: a - 20 + "px"
                })
            }
            t.width(i),
            $("#footer").width(i);
            var o = $("#mask");
            o.is(":visible") && o.height(document.documentElement.clientHeight);
            var d = $("#info_box");
            d.is(":visible") && SysUtil.setBoxStyle(d, i, a),
            (d = $("#progress_box")).is(":visible") && SysUtil.setBoxStyle(d, i, a)
        }
        , 200)
    },
    setStripeTable: function(e) {
        var t = $(e || "table.detail");
        $("tr:odd", t).addClass("odd"),
        $("tr:even", t).addClass("even"),
        $("tr:first", t).removeClass("even")
    },
    getRandomParam: function() {
        return "_t=" + Math.floor(1e7 * Math.random())
    },
    getUrl: function(e) {
        return String.format("{0}?{1}", e, Page.getRandomParam())
    },
    getHtml: function(e, t, n) {
        Page.postJSON({
            returnHtml: !0,
            json: {
                cmd: RequestCmd.GET_HTML,
                url: e,
                subcmd: t || 0
            },
            success: function(e) {
                e.indexOf('"message":"NO_AUTH"') > 0 ? location.href = Page.getUrl(Url.LOGIN) : n(e)
            }
        })
    },
    getFileData: function(e, t, n) {
        Page.postJSON({
            returnHtml: !0,
            json: {
                cmd: RequestCmd.GET_FILE_DATA,
                url: e,
                subcmd: t || 0
            },
            success: function(e) {
                e.indexOf('"message":"NO_AUTH"') > 0 ? location.href = Page.getUrl(Url.LOGIN) : n(e)
            }
        })
    },
    writeTime: function(e) {
        Page.postJSON({
            json: {
                cmd: RequestCmd.WRITE_TIME,
                method: JSONMethod.POST,
                subcmd: e,
                time: (new Date).format("yyyy-mm-dd HH:MM")
            },
            success: function(e) {}
        })
    },
    postJSON: function(e, t) {
        var n, i = $.extend({}, Page.defaultSetting, e), a = i.json;
        if (a.method || (a.method = JSONMethod.GET),
        "POST" == a.method ? (n = "POST",
        a.token = sessionStorage.token) : n = "GET",
        a.language || (a.language = Page.language),
        a.sessionId || (a.sessionId = sessionStorage.sessionId || ""),
        !Page.isTest) {
            var s = i.$id;
            s && s.disable();
            var r = 0;
            "POST" == a.method && (r = void 0 != t ? t : 6e4);
            var l = !1;
            $.ajax({
                url: i.url,
                type: "POST",
                timeout: r,
                data: JSON.stringify(a),
                dataType: "text",
                beforeSend: function(e) {},
                success: function(e) {
                    try {
                        var t;
                        if (a.cmd == RequestCmd.FIND_AP && e.indexOf("ssids") > 0 ? ((t = {}).success = !0,
                        t.cmd = RequestCmd.FIND_AP,
                        t.macs = ConvertUtil.parseFindAp(e, "macs"),
                        t.ssids = ConvertUtil.parseFindAp(e, "ssids"),
                        t.frequencys = ConvertUtil.parseFindAp(e, "frequencys"),
                        t.singalLevels = ConvertUtil.parseFindAp(e, "singalLevels"),
                        t.encryptionKeys = ConvertUtil.parseFindAp(e, "encryptionKeys"),
                        t.encryptionModes = ConvertUtil.parseFindAp(e, "encryptionModes"),
                        t.groupCiphers = ConvertUtil.parseFindAp(e, "groupCiphers"),
                        t.pairwiseCiphers = ConvertUtil.parseFindAp(e, "pairwiseCiphers"),
                        t.authenticationSuites = ConvertUtil.parseFindAp(e, "authenticationSuites")) : a.cmd == RequestCmd.OPENVPN_SETTING && e ? ((t = SysUtil.parseJSON(e)).cmd = RequestCmd.OPENVPN_SETTING,
                        t.success = !0) : t = i.returnHtml ? e : SysUtil.parseJSON(e),
                        Window.isOther = !1,
                        i.returnHtml)
                            i.success(t);
                        else {
                            if (void 0 == t.cmd)
                                return;
                            if (t.success)
                                i.success(t);
                            else {
                                if (("NO_AUTH" == t.message || "LOGIN_TIMEOUT" == t.message) && "1" != Page.isReboot)
                                    return sessionStorage.clear(),
                                    Page.timer && _clearInterval(),
                                    Window.isOther = !0,
                                    "LOGIN_TIMEOUT" == t.message && AlertUtil.alertMsg(CHECK.tip.loginTimeout),
                                    void (IsPC() ? location.href = Page.getUrl(Url.LOGIN) : location.href = Page.getUrl("/mobile_web/login.html"));
                                $.isFunction(i.fail) ? i.fail(t) : t.message && "1" != Page.isReboot && ($(".fy-alert-box").is(":visible") && fyAlertDestory(),
                                setTimeout( () => {
                                    AlertUtil.alertMsg(t.message)
                                }
                                , 250))
                            }
                        }
                    } catch (e) {}
                },
                error: function(e, t, n) {
                    l = !0,
                    "timeout" != n && "timeout" != t || "1" == Page.isReboot || fyAlertMsgFail(PROMPT.status.requestTimeout),
                    $.isFunction(i.error) && i.error(e, t, n)
                },
                complete: function(e) {
                    if ("POST" != a.method || e.responseText || "6" == a.cmd || l || fyAlertMsgFail(PROMPT.status.fail),
                    "POST" == n) {
                        -1 === [269, 100, 101, 97].indexOf(a.cmd) && window.TIMER && resetTime()
                    }
                    i.complete(),
                    s && s.enable(),
                    l = !1
                }
            })
        }
    },
    postSyncJSON: function(e, t) {
        return new Promise(function(n, i) {
            var a, s = $.extend({}, Page.defaultSetting, e), r = s.json;
            if (r.method || (r.method = JSONMethod.GET),
            "POST" == r.method ? (a = "POST",
            r.token = sessionStorage.token) : a = "GET",
            r.language || (r.language = Page.language),
            r.sessionId || (r.sessionId = sessionStorage.sessionId || ""),
            !Page.isTest) {
                var l = s.$id;
                l && l.disable(),
                $.ajax({
                    url: s.url,
                    type: "POST",
                    timeout: t || s.timeout,
                    data: JSON.stringify(r),
                    dataType: "text",
                    beforeSend: function(e) {},
                    success: function(e) {
                        try {
                            var t;
                            if (r.cmd == RequestCmd.FIND_AP && e.indexOf("ssids") > 0 ? ((t = {}).success = !0,
                            t.cmd = RequestCmd.FIND_AP,
                            t.macs = ConvertUtil.parseFindAp(e, "macs"),
                            t.ssids = ConvertUtil.parseFindAp(e, "ssids"),
                            t.frequencys = ConvertUtil.parseFindAp(e, "frequencys"),
                            t.singalLevels = ConvertUtil.parseFindAp(e, "singalLevels"),
                            t.encryptionKeys = ConvertUtil.parseFindAp(e, "encryptionKeys"),
                            t.encryptionModes = ConvertUtil.parseFindAp(e, "encryptionModes"),
                            t.groupCiphers = ConvertUtil.parseFindAp(e, "groupCiphers"),
                            t.pairwiseCiphers = ConvertUtil.parseFindAp(e, "pairwiseCiphers"),
                            t.authenticationSuites = ConvertUtil.parseFindAp(e, "authenticationSuites")) : t = s.returnHtml ? e : SysUtil.parseJSON(e),
                            Window.isOther = !1,
                            s.returnHtml)
                                s.success(t);
                            else {
                                if (void 0 == t.cmd)
                                    return void i(t);
                                if (t.success)
                                    s.success(t),
                                    n(t);
                                else {
                                    if (("NO_AUTH" == t.message || "LOGIN_TIMEOUT" == t.message) && "1" != Page.isReboot)
                                        return sessionStorage.clear(),
                                        Page.timer && _clearInterval(),
                                        window.TIMEOUT2 && (clearTimeout(window.TIMEOUT2),
                                        window.TIMEOUT2 = null),
                                        window.WPS_TIMEOUT && (clearTimeout(window.WPS_TIMEOUT),
                                        window.WPS_TIMEOUT = null),
                                        Window.isOther = !0,
                                        "LOGIN_TIMEOUT" == t.message && AlertUtil.alertMsg(CHECK.tip.loginTimeout),
                                        location.href = Page.getUrl(Url.LOGIN),
                                        void i(t);
                                    $.isFunction(s.fail) ? s.fail(t) : t.message && "1" != Page.isReboot && ($(".fy-alert-box").is(":visible") && fyAlertDestory(),
                                    (t.message = "Illegal character input") || AlertUtil.alertMsg(t.message))
                                }
                                i(t)
                            }
                        } catch (e) {}
                    },
                    error: function(e, t, n) {
                        $.isFunction(s.error) && s.error(e, t, n),
                        i(n)
                    },
                    complete: function() {
                        "POST" == a && ("269" == r.cmd || "100" == r.cmd || "101" == r.cmd || "97" == r.cmd || resetTime()),
                        s.complete(),
                        l && l.enable(),
                        n()
                    }
                })
            }
        }
        )
    },
    defaultSetting: {
        url: Url.DEFAULT_CGI,
        timeout: 0,
        returnHtml: !1,
        success: function() {},
        complete: function() {}
    },
    killSearchPlmn: function() {},
    setHash: function(e) {
        window.btnStopClick && (window.btnStopClick(),
        window.btnStopClick = null),
        Page.postJSON({
            json: {
                cmd: RequestCmd.SET_HASH,
                method: JSONMethod.POST,
                setHash: e
            },
            success: function(e) {}
        })
    },
    getHash: function() {
        var e, t = {};
        return t.cmd = RequestCmd.SET_HASH,
        t.method = JSONMethod.GET,
        t.sessionId = sessionStorage.sessionId,
        $.ajax({
            url: Url.DEFAULT_CGI,
            type: "POST",
            data: JSON.stringify(t),
            dataType: "text",
            async: !1,
            success: function(t) {
                var n = SysUtil.parseJSON(t);
                e = n.setHash
            }
        }),
        e
    },
    destory: function() {
        Page.currentId++,
        Page.isSearchingPlmn && (Page.isSearchingPlmn = !1,
        Page.killSearchPlmn())
    },
    createTable: function(e, t, n, i, a, s) {
        var r = new StringBuilder;
        return r.append(Page.createTableHead(e, s)),
        i <= 0 || a <= 0 ? r.toString() : (Page.createTableBody(r, t, n, i, a, s),
        r.toString())
    },
    createTableHead: function(e, t) {
        return t = t ? "detail " + t : "detail",
        String.format('<div class="{0}">{1}</div>', t, e)
    },
    createTableBody: function(e, t, n, i, a, s) {
        s = s ? "detail " + s : "detail",
        e.append(String.format('<table class="{0}" cellspacing="0">', s));
        for (var r = 0, l = (Page.isChinese(),
        ":"), o = 0; o < i; o++) {
            o % a == 0 && e.append("<tr>"),
            e.append("<th>"),
            t[o] ? (e.append(t[o]),
            e.append(l),
            e.append("</th>")) : e.append("&nbsp;</th>");
            var d = !1;
            2 == a && o + 1 < i && !t[o + 1] ? (d = !0,
            e.append('<td colspan="3">')) : e.append("<td>"),
            0 == n[o].length ? e.append("&nbsp;</td>") : (e.append(n[o]),
            e.append("</td>")),
            r++,
            (d || r == a) && (e.append("</tr>"),
            r = 0),
            d && o++
        }
        if (0 != r) {
            for (o = r; o < a; o++)
                e.append("<th>&nbsp;</th><td>&nbsp;</td>");
            e.append("</tr>")
        }
        e.append("</table>")
    },
    sortAp: function(data, field, sortDirection) {
        for (var count = data.ssids.length, ssids = data.ssids, macs = data.macs, frequencys = data.frequencys, singalLevels = data.singalLevels, encryptionKeys = data.encryptionKeys, encryptionModes = data.encryptionModes, groupCiphers = data.groupCiphers, pairwiseCiphers = data.pairwiseCiphers, authenticationSuites = data.authenticationSuites, i = 0; i < count - 1; i++)
            singalLevels[i] = parseInt(singalLevels[i], 10);
        for (var fields = eval(field), isAsc = sortDirection == SortDirection.ASC, i = 0, min; i < count - 1; i++)
            for (var j = i + 1; j < count; j++)
                isAsc ? fields[j] < fields[i] && swapAll(i, j) : fields[j] > fields[i] && swapAll(i, j);
        function swap(e, t, n) {
            min = e[n],
            e[n] = e[t],
            e[t] = min
        }
        function swapAll(e, t) {
            swap(ssids, e, t),
            swap(macs, e, t),
            swap(frequencys, e, t),
            swap(singalLevels, e, t),
            swap(encryptionKeys, e, t),
            swap(encryptionModes, e, t),
            swap(groupCiphers, e, t),
            swap(pairwiseCiphers, e, t),
            swap(authenticationSuites, e, t)
        }
    },
    getDeviceName: function() {
        Page.postJSON({
            json: {
                cmd: RequestCmd.GET_DEVICE_NAME,
                method: JSONMethod.GET,
                sessionId: ""
            },
            success: function() {}
        })
    },
    render: function(e, t, n) {
        e || (e = "#child_container"),
        t || (t = "#child_template"),
        n || (n = DOC),
        $(e).html(_.template($(t).html(), n))
    },
    applyFilter: function() {
        Page.postJSON({
            json: {
                cmd: RequestCmd.APPLY_FILTER,
                method: JSONMethod.POST
            },
            success: function(e) {
                1 == e.success ? fyAlertMsgSuccess() : fyAlertMsgFail()
            }
        })
    }
}
  , CheckUtil = {
    isEmpty: function(e) {
        return !e || 0 == e.length || "NULL" == e
    },
    checkIp: function(e, t) {
        var n = /^(25[0-5]|2[0-4]\d|[0-1]?\d?\d)(\.(25[0-5]|2[0-4]\d|[0-1]?\d?\d)){3}$/;
        if ("IPV6" == t)
            return /^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(([0-9A-Fa-f]{1,4}:){0,5}:((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(::([0-9A-Fa-f]{1,4}:){0,5}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$/i.test(e);
        if (n.test(e))
            for (var i = e.split("."), a = 0; a < i.length; a++)
                if (i[a].length > 1 && "0" == i[a][0])
                    return !1;
        return n.test(e) && "0" != i[0] && i[0] < 224 && 127 != i[0] && "0" != i[3] && "255" != i[3]
    },
    checkIpRoute: function(e) {
        if ("0.0.0.0" == e)
            return !0;
        var t = /^(25[0-5]|2[0-4]\d|[0-1]?\d?\d)(\.(25[0-5]|2[0-4]\d|[0-1]?\d?\d)){3}$/;
        if (t.test(e))
            for (var n = e.split("."), i = 0; i < n.length; i++)
                if (n[i].length > 1 && "0" == n[i][0])
                    return !1;
        return t.test(e) && "0" != n[0] && n[0] < 224 && "255" != n[3]
    },
    checkIpRoute2: function(e) {
        if ("0.0.0.0" == e)
            return !0;
        var t = /^(25[0-5]|2[0-4]\d|[0-1]?\d?\d)(\.(25[0-5]|2[0-4]\d|[0-1]?\d?\d)){3}$/;
        if (t.test(e))
            for (var n = e.split("."), i = 0; i < n.length; i++)
                if (n[i].length > 1 && "0" == n[i][0] || "0" == n[3] || "255" == n[3])
                    return !1;
        return t.test(e) && "0" != n[0] && n[0] < 224 && "255" != n[3]
    },
    checkMac: function(e) {
        return /^([0-9a-f]{2}[\:\-]{0,1}){5}[0-9a-f]{2}$/i.test(e)
    },
    checkDUID: function(e) {
        return /^[0-9a-fA-F][0-9a-fA-F](\:[0-9a-fA-F][0-9a-fA-F]){1,}$/i.test(e)
    },
    checkInvalidMac: function(e) {
        return "1" != parseInt(e.split(":")[0], 16).toString(2).slice(-1) && -1 === ["FF:FF:FF:FF:FF:FF", "00:00:00:00:00:00", "01:00:00:00:00:00"].indexOf(e)
    },
    checkPort: function(e) {
        var t = parseInt(e, 10);
        return isNaN(t) || t < 0 || t > 65535 ? {
            isValid: !1
        } : {
            isValid: !0,
            port: t
        }
    },
    checkIpPort: function(e) {
        if (!/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\:(\d+)$/.test(e))
            return !1;
        var t = parseInt(RegExp.$5, 10);
        return !(isNaN(t) || t < 0 || t > 65535)
    },
    checkNumber: function(e) {
        return /^\d+$/.test(e)
    },
    checkHex: function(e) {
        return /^[0-9A-F]*$/gi.test(e)
    },
    checkASCII: function(e) {
        return /[\x00-\xff]/g.test(e)
    },
    checkAPNName: function(e) {
        return /^[0-9a-zA-Z!#$&()*\+,\-\.\/%:;<=>?@\[\]^_\{|\}~]*$/.test(e)
    },
    checkAPNName_right: function(e) {
        return !/^(rac|lac|sgsn|\.|.*\.\.|.*-\.|.*\.-|\s)/.test(e) && !/(\.gprs|\s)$/.test(e) && /^[\.a-zA-Z0-9- ]{1,64}$/.test(e) && "auto" != e.toLowerCase()
    },
    checkSSIDName: function(e) {
        return /^[0-9a-zA-Z!#$&()\s*\+,\-\.\/%:;<=>?@\[\]^_\{|\}~]*$/.test(e)
    },
    checkForm: function(e, t, n) {
        return e.validate({
            ignore: ".ignore",
            rules: t,
            messages: n
        }).form()
    },
    checkPwd: function(e) {
        return /^[\x00-\xff]{8,63}$/.test(e) || /^[0-9A-F]{64}$/.test(e)
    },
    checkeWepPwd68: function(e) {
        return /^[\x00-\xff]{5}$/.test(e) || /^[0-9A-F]{10}$/.test(e)
    },
    checkeWepPwd128: function(e) {
        return /^[\x00-\xff]{13}$/.test(e) || /^[0-9A-F]{26}$/.test(e)
    },
    checkNetSegment: function(e, t, n) {
        var i = ConvertUtil.ip4ToNum(e)
          , a = ConvertUtil.ip4ToNum(t);
        return (i & a) == (ConvertUtil.ip4ToNum(n) & a)
    },
    checkExeCmd: function(e) {
        return /.*/.test(e)
    },
    checkPlmn: function(e) {
        return /\D/.test(e)
    },
    checkMask: function(e) {
        return /^(254|252|248|240|224|192|128|0)\.0\.0\.0|255\.(254|252|248|240|224|192|128|0)\.0\.0|255\.255\.(254|252|248|240|224|192|128|0)\.0|255\.255\.255\.(255|254|252|248|240|224|192|128|0)$/.test(e)
    },
    checkRange: function(e, t, n) {
        var i = parseInt(e, 10);
        return !(isNaN(i) || i < t || i > n)
    },
    checkCN: function(e) {
        return /^[0-9a-zA-Z!#$*\+,\-\.%:=\?@\[\]\^_\{|\}~\\\/''""&()<>;`]+$/.test(e)
    },
    checkSSID: function(e) {
        var t = new RegExp(/\S$/)
          , n = new RegExp(/^\S/);
        return /^[\u4E00-\u9FA5A-Za-z0-9\^~%&,;=?$<>*'"\!+@#%()_:;|?./\-\[\]\{\\\} ]+$/.test(e) && t.test(e) && n.test(e) && -1 == e.indexOf("\\")
    }
}
  , CookieUtil = {
    getCookie: function(e) {
        var t = document.cookie;
        if (t && !(t.indexOf(e + "=") < 0))
            for (var n = t.split(";"), i = 0; i < n.length; i++) {
                var a = n[i].trim();
                if (0 == a.indexOf(e + "="))
                    return decodeURI(a.substring(e.length + 1))
            }
    },
    setCookie: function(e, t, n) {
        var i = String.format("{0}={1}", e, encodeURI(t));
        if (n) {
            if (n.expireHours) {
                var a = new Date;
                a.setTime(a.getTime() + 3600 * n.expireHours * 1e3),
                i += String.format("; expires={0}", a.toGMTString())
            }
            n.path && (i += String.format("; path={0}", n.path)),
            n.domain && (i += String.format("; domain={0}", n.domain)),
            n.secure && (i += "; true")
        }
        document.cookie = i
    },
    deleteCookie: function(e, t) {
        this.setCookie(e, "", t)
    }
}
  , DivUtil = {
    showDiv: function(e) {
        e.width(document.documentElement.clientWidth),
        e.height(document.documentElement.clientHeight),
        e.show()
    },
    clearDiv: function(e) {
        e.html(""),
        e.hide()
    },
    moveEvent: function(e, t) {
        var n = !!document.all
          , i = !0
          , a = n ? event.x : e.pageX
          , s = n ? event.y : e.pageY;
        offLeft = t.offset().left,
        offTop = t.offset().top,
        $(document).mousemove(function(e) {
            if (i) {
                var r = n ? event.x : e.pageX;
                r < 200 && (r = 200),
                r > document.documentElement.clientWidth && (r = document.documentElement.clientWidth);
                var l = n ? event.y : e.pageY;
                l < 100 && (l = 100),
                l > document.documentElement.clientHeight && (l = document.documentElement.clientHeight),
                t.css({
                    top: offTop - s + l - 100 + "px",
                    left: offLeft - a + r - 200 + "px"
                })
            }
        }),
        $(document).mouseup(function() {
            i = !1
        })
    }
}
  , StatusUtil = {
    formatSingalLevel: function(e, t, n) {
        var i = [DOC.ddl.signalLevelNone, DOC.ddl.signalLevel0, DOC.ddl.signalLevel1, DOC.ddl.signalLevel2, DOC.ddl.signalLevel3, DOC.ddl.signalLevel4, DOC.ddl.signalLevel5]
          , a = parseInt(e, 10);
        (isNaN(a) || a > Page.ledNumber || a < 0 || 0 == a) && (a = -1),
        -1 == a && t && (a = 0);
        var s = DOC.lte.signalLevel + DOC.colon + i[a + 1];
        return String.format("<div style='white-space:nowrap' style='white-space:nowrap' class=\"singal singal-{0} singal{1}\" title=\"{2}\">{3}</div>", Page.ledNumber, a + 1, s, n || "")
    },
    formatAtAssert: function(e, t) {
        var n = ""
          , i = "";
        return "1" == e || "1" == t ? (n = "invalid",
        i = PROMPT.status.abnormal) : (n = "normal",
        i = PROMPT.status.normal),
        String.format('<div class="{0}" title="{1}">{2}</div>', n, i, "AT")
    },
    formatSimInfo: function(e, t, n, i) {
        var a, s, r, l = parseInt(e, 10);
        return r = "1" == (n = n || "0") ? null == t || "" == t ? "SIM" : "SIM" + (parseInt(t) + 1) : "2" == n ? "SIM" : i && "110" != i ? "1" == t || "2" == t ? PROMPT.status.innerSIM + t : "SIM" : "1" == t ? PROMPT.status.innerSIM : "SIM",
        $("#simInfo").show(),
        0 == l ? (s = "invalid",
        a = DOC.status.noSim) : 1 == l ? (s = "normal",
        a = DOC.status.existSim) : (s = "invalid",
        a = DOC.status.noSim,
        $("#simInfo").hide()),
        String.format('<div class="{0}" title="{1}">{2}</div>', s, a, r)
    },
    formatWiFiInfo: function(e) {
        var t, n;
        return "0" == e ? (n = "unnormal",
        t = DOC.status.disabled) : "part" == e ? (n = "partnormal",
        t = DOC.status.wifiPartEnabled) : "2" == e ? (n = "abnormality",
        t = DOC.lbl.wifiAbnormality) : (n = Page.build_type ? "partnormal" : "normal",
        t = DOC.status.enabled),
        String.format('<div class="{0}" title="{1}">2.4 GHz Wi-Fi</div>', n, t)
    },
    format5gWiFiInfo: function(e) {
        var t, n;
        return "0" == e ? (n = "unnormal",
        t = DOC.status.disabled) : "part" == e ? (n = "partnormal",
        t = DOC.status.wifiPartEnabled) : "2" == e ? (n = "abnormality",
        t = DOC.lbl.wifiAbnormality) : (n = Page.build_type ? "partnormal" : "normal",
        t = DOC.status.enabled),
        String.format('<div class="{0}" title="{1}">5 GHz Wi-Fi</div>', n, t)
    },
    formatLanWanInfo: function(e, t) {
        var n = e.toLowerCase();
        "lan4/wan" == n && "2" != Page.networkMode && "5" != Page.networkMode ? n = "lan4" : "2" != Page.networkMode && "5" != Page.networkMode || e != "LAN" + t && "lan4/wan" != n || (n = "wan1",
        e = "WAN");
        var i, a = "#" + n;
        $(a).show(),
        i = DOC.status.connected,
        $(a).html(String.format('<div class="{0}" title="{1}">{2}</div>', "link", i, e))
    },
    hasNewSmsPrompt: !1,
    smsIntervalId: null,
    setSmsInfo: function(e) {
        var t, n, i = parseInt(e, 10);
        function a() {
            "IN0345" != Page.aeraId && $("#smsInfo").html(String.format('<div class="{0}" title="{1}">{1}</div>', n, t))
        }
        function s() {
            null != StatusUtil.smsIntervalId && clearInterval(StatusUtil.smsIntervalId)
        }
        if (1 == i ? (n = "newsms",
        t = DOC.status.newMessage) : (n = "normal",
        t = DOC.status.message),
        1 == i) {
            if (!StatusUtil.hasNewSmsPrompt) {
                StatusUtil.hasNewSmsPrompt = !0;
                var r = 0;
                StatusUtil.smsIntervalId = setInterval(function() {
                    n = r % 2 == 0 ? "newsms" : "newsms2",
                    a(),
                    r > 10 && s(),
                    r++
                }, 500)
            }
        } else
            StatusUtil.hasNewSmsPrompt = !1,
            s(),
            a()
    },
    getSysStatus: function() {
        var e = null;
        !function t() {
            Page.postJSON({
                json: {
                    cmd: RequestCmd.GET_SYS_STATUS,
                    method: JSONMethod.GET,
                    sessionId: ""
                },
                success: function(t) {
                    e = t,
                    Page.smartwifiSwich = "1" == t.smartwifiSwich
                },
                complete: function() {
                    var n, i, a, s = setTimeout(function() {
                        t(),
                        clearTimeout(s),
                        s = null
                    }, 1e4);
                    if (e.signal_lvl && ("IN0345" == Page.aeraId && (e.network_type_str = e.network_type_str.split("(")[0]),
                    a = StatusUtil.formatSingalLevel(e.signal_lvl, e.network_type_str, e.network_type_str)),
                    sessionStorage.signal_lvl = parseInt(e.signal_lvl) || 0,
                    sessionStorage.sim_info = "1" == e.sim_status ? 1 : 0,
                    sessionStorage.flowLimitFlag = "1" == e.flowLimitFlag ? DOC.lbl.flowLimit : "",
                    "ok" == e.flag ? $("#isShowRefresh").show() : $("#isShowRefresh").hide(),
                    "2" != Page.networkMode && ($("#netInfo").html(a),
                    "IN0345" != Page.aeraId && $("#simInfo").html(StatusUtil.formatSimInfo(e.sim_status, e.current_card_type, e.current_card_cfg_mode, e.sim_support_cap))),
                    "1" == e.lock_device_flag)
                        $("#simRemind").show(),
                        $("#simRemind").html(PROMPT.status.simRemindDevice);
                    else if ("1" == e.lock_plmn_flag)
                        $("#simRemind").show(),
                        $("#simRemind").html(PROMPT.status.simRemindPlmn);
                    else if ("1" == e.ipClash)
                        $("#simRemind").show(),
                        $("#simRemind").html(PROMPT.status.ipClash);
                    else if ("1" == e.roam_status) {
                        var r = "1" == e.roamingEnable ? PROMPT.status.simRemindRoam : PROMPT.status.simRemindRoamSwitch;
                        $("#simRemind").show(),
                        $("#simRemind").html(r)
                    } else
                        "AE0001" == Page.aeraId && "2" == Page.level && "1" == e.pci_arrears_enable ? ($("#simRemind").show(),
                        $("#simRemind").html(DOC.lbl.theCpeIsSuspended)) : "AE0001" == Page.aeraId && "2" == Page.level && "1" == e.pci_manual_enable ? ($("#simRemind").show(),
                        $("#simRemind").html(DOC.lbl.ecgiWarning_1)) : ("AE0001" == Page.aeraId && "2" == Page.level || "AE0001" != Page.aeraId) && "1" == e.pci_enable ? ($("#simRemind").show(),
                        $("#simRemind").html(DOC.lbl.ecgiWarning)) : $("#simRemind").hide();
                    n = "1" == e.wlan2g_switch_0 ? "1" : "0",
                    "0" == e.wifi24g_run_status && (n = "2"),
                    "1" === e.is_smartwifi_active_flag && (n = "0"),
                    i = "1" == e.wlan5g_switch_0 ? "1" : "0",
                    "0" == e.wifi5g_run_status && (i = "2"),
                    "2" === e.is_smartwifi_active_flag && (i = "0");
                    var l, o, d = e.wired_link_list;
                    if ("IN0345" != Page.aeraId) {
                        if (d) {
                            $("#lan1").hide(),
                            $("#lan2").hide(),
                            $("#lan3").hide(),
                            $("#lan4").hide();
                            for (var m = e.wanIndex || 3, c = 0; c < d.length; c++)
                                StatusUtil.formatLanWanInfo(d[c], +m + 1)
                        }
                        $("#wifiInfo").html(StatusUtil.formatWiFiInfo(n)),
                        $("#wifiInfo5g").html(StatusUtil.format5gWiFiInfo(i)),
                        isHide2(10) && "1" != Page.wifiType && $("#wifiInfo").hide(),
                        isHide2(10) && "2" != Page.wifiType && $("#wifiInfo5g").hide(),
                        $("#lteUsbInfo").html(StatusUtil.formatAtAssert(e.modem_assert, e.modem_at))
                    }
                    "1" == e.flightMode ? ($("#flightMode").show(),
                    $("#netInfo").hide()) : ($("#flightMode").hide(),
                    $("#netInfo").show()),
                    "1" == e.network_status ? "5" === e.wanMode || "7" === e.wanMode ? "1" === e.wanPrio || "0" === e.wanPrio ? (l = "1" === e.wanPrio ? "wireConn" : "mobileConn",
                    o = "1" === e.wanPrio ? DOC.lbl.dataWireOpen : DOC.lbl.dataMobileOpen) : "1" === e.wanStrategy || "0" === e.wanStrategy ? (l = "1" === e.wanStrategy ? "wireConn" : "mobileConn",
                    o = "1" === e.wanStrategy ? DOC.lbl.dataWireOpen : DOC.lbl.dataMobileOpen) : (l = "7" === e.wanMode ? "wireConn" : "mobileConn",
                    o = "7" === e.wanMode ? DOC.lbl.dataWireOpen : DOC.lbl.dataMobileOpen) : "2" === e.wanMode ? (l = "wireConn",
                    o = DOC.lbl.dataWireOpen) : (l = "mobileConn",
                    o = DOC.lbl.dataSwitchOpen) : "5" === e.wanMode || "7" === e.wanMode ? "1" === e.wanPrio || "0" === e.wanPrio ? (l = "1" === e.wanPrio ? "wireDisConn" : "mobileDisconn",
                    o = "1" === e.wanPrio ? DOC.lbl.dataWireClose : DOC.lbl.dataMobileClose) : "1" === e.wanStrategy || "0" === e.wanStrategy ? (l = "1" === e.wanStrategy ? "wireDisConn" : "mobileDisconn",
                    o = "1" === e.wanStrategy ? DOC.lbl.dataWireClose : DOC.lbl.dataMobileClose) : (l = "7" === e.wanMode ? "wireDisConn" : "mobileDisconn",
                    o = "7" === e.wanMode ? DOC.lbl.dataWireClose : DOC.lbl.dataMobileClose) : "2" === e.wanMode ? (l = "wireDisConn",
                    o = DOC.lbl.dataWireClose) : (l = "mobileDisconn",
                    o = DOC.lbl.dataSwitchClose),
                    $("#dataSwitch").html(String.format('<div class="{0}" title="{1}"></div>', l, o)),
                    $("#dataSwitch").show(),
                    isHide(19) && ("0" == e.voice_type && "4" == e.volteStatus ? "IO24057" == Page.aeraId ? $("#voiceInfo").text("VoLTE") : $("#voiceInfo").text("HD") : "1" == e.voice_type && "3" == e.voipStatus ? $("#voiceInfo").text("VoIP") : $("#voiceInfo").text("")),
                    "1" == e.batteryExist ? ($("#batteryExist").show(),
                    $("#batteryExistTitle").show(),
                    "1" == e.batteryCharge ? (l = "battery-status battery-charge",
                    o = e.batteryCapacity ? DOC.lbl.batteryCharge + e.batteryCapacity : "") : "2" == e.batteryCharge ? (l = "battery-status battery-charge-quickly",
                    o = e.batteryCapacity ? DOC.lbl.batteryChargeQuickly + DOC.colon + e.batteryCapacity : "") : parseInt(e.batteryLevel) > 0 && parseInt(e.batteryLevel) < 5 ? (l = "battery-status battery-level" + e.batteryLevel,
                    o = e.batteryCapacity ? DOC.lbl.batteryPower + e.batteryCapacity : "") : ($("#batteryExist").hide(),
                    $("#batteryExistTitle").hide(),
                    o = ""),
                    $("#batteryExist").html(String.format('<div class="{0}" title="{1}%"></div>', l, o)),
                    "1" != e.battery_abnormal_charging_detect || isHide2(25) ? "0" == e.sleepTime ? $("#batteryExistTitle").html(String.format('<div>{0}%<span style="margin-left:10px;color:red">{1}</span></div>', e.batteryCapacity, DOC.lbl.nerveSleepTip)) : $("#batteryExistTitle").html(String.format("<div>{0}%</div>", e.batteryCapacity)) : $("#batteryExistTitle").html(String.format('<div>{0}%<span style="margin-left:10px;color:red">{1}</span></div>', e.batteryCapacity, DOC.lbl.abnormalCharge))) : ($("#batteryExist").hide(),
                    $("#batteryExistTitle").hide())
                }
            })
        }()
    }
};
function getOpenInfo() {
    !IsPC() && isHide2(seniorHide.web_page_hide_set_6) && vant.Dialog.confirm({
        message: PROMPT.confirm.deviceInfo,
        confirmButtonText: DOC.btn.confirm,
        cancelButtonText: PROMPT.tips.cancel
    }).then(function() {
        window.location.href = Page.getUrl("/mobile_web/login.html")
    }).catch(function() {});
    var e = "-"
      , t = {};
    function n(t, n, i) {
        var a, s, r = [], l = [];
        r.push(DOC.lte.rsrp),
        r.push(DOC.lte.rssi),
        r.push(DOC.lte.rsrq),
        r.push(DOC.lte.sinr),
        r.push(DOC.lte.phyCellId),
        r.push(DOC.lte.freqPoint),
        l.push((t.RSRP ? t.RSRP + "dBm" : e) + "/" + (t.RSRP_5G ? t.RSRP_5G + "dBm" : e)),
        l.push((t.RSSI ? FormatUtil.formatField(t.RSSI, "dBm") : e) + "/" + (t.RSSI_5G ? FormatUtil.formatField(t.RSSI_5G, "dBm") : e)),
        l.push((t.RSRQ ? t.RSRQ + "dB" : e) + "/" + (t.RSRQ_5G ? t.RSRQ_5G + "dB" : e)),
        l.push((t.SINR && "-" != t.SINR ? t.SINR + "dB" : e) + "/" + (t.SINR_5G ? t.SINR_5G + "dB" : e)),
        l.push((t.PCI || e) + "/" + (t.PCI_5G || e)),
        l.push((t.FREQ ? t.FREQ : e) + "/" + (t.FREQ_5G ? t.FREQ_5G : e)),
        a = Page.createTable(DOC.title.lteInfoBasic, r, l, r.length, 1, "detail2"),
        $("#device_check").show(),
        $("#lte_info").html(a),
        Page.setStripeTable("#lte_info");
        var o = [];
        o.push(DOC.lbl.runtime),
        o.push(DOC.device.firmwareVersion),
        "SC001" === Page.aeraId && (o.push("IMEI"),
        o.push("IMSI"));
        var d = [];
        t.uptime ? d.push(ConvertUtil.parseUptime2(t.uptime) || e) : d.push(e),
        t.fake_version ? "mobot.css" == sessionStorage.theme ? d.push(FormatUtil.formatField("M" + t.fake_version) || e) : d.push(FormatUtil.formatField(t.fake_version) || e) : d.push(FormatUtil.formatField(e)),
        sessionStorage.fake_version = t.fake_version || "1.0.0",
        sessionStorage.real_fwversion = t.real_fwversion || "1.0.0",
        "SC001" === Page.aeraId && (d.push(FormatUtil.formatField(t.IMEI) || e),
        d.push(FormatUtil.formatField(t.IMSI) || e)),
        t.external_mcu_capabilty && "1" == t.external_mcu_capabilty && (o.push("MCU" + DOC.device.firmwareVersion),
        d.push(t.external_mcu_version || "-")),
        a = Page.createTable(DOC.title.router, o, d, o.length, 1, "detail2"),
        $("#router_info").html(a),
        Page.setStripeTable("#router_info");
        var m = [];
        m.push(DOC.net.ip),
        m.push(DOC.device.wanMac),
        m.push(DOC.net.dns1),
        m.push(DOC.net.dns2),
        m.push(DOC.net.ipv6),
        m.push(DOC.net.dns3),
        m.push(DOC.net.dns4);
        var c = [];
        c.push(FormatUtil.formatField(t.wan_ip || e)),
        c.push(FormatUtil.formatField(t.wan_mac ? t.wan_mac.match(/[0-9a-f]{2}/gi).join(":").toUpperCase() : e)),
        c.push(FormatUtil.formatField(t.wan_dns || e)),
        c.push(FormatUtil.formatField(t.wan_dns2 || e)),
        c.push(FormatUtil.formatField(t.wan_ipv6_ip || e)),
        c.push(FormatUtil.formatField(t.wan_ipv6_dns || e)),
        c.push(FormatUtil.formatField(t.wan_ipv6_dns2 || e));
        var u = [];
        u.push(DOC.net.ip),
        u.push(DOC.net.mask),
        u.push(DOC.lan.gateway),
        u.push(DOC.device.wanMac),
        u.push(DOC.net.dns1),
        u.push(DOC.net.dns2);
        var _ = [];
        _.push(FormatUtil.formatField(t.wired_wan_ip || e)),
        _.push(FormatUtil.formatField(t.wired_wan_netmask || e)),
        _.push(FormatUtil.formatField(t.wired_wan_gateway || e)),
        _.push(FormatUtil.formatField(t.wired_wan_mac ? t.wired_wan_mac.match(/[0-9a-f]{2}/gi).join(":").toUpperCase() : e)),
        _.push(FormatUtil.formatField(t.wired_wan_dns || e)),
        _.push(FormatUtil.formatField(t.wired_wan_dns2 || e));
        var T = [];
        T.push(DOC.net.ip),
        T.push(DOC.net.mask),
        T.push(DOC.lan.gateway),
        T.push(DOC.device.wanMac),
        T.push(DOC.net.dns1),
        T.push(DOC.net.dns2);
        var S = [];
        S.push(FormatUtil.formatField(t.wlan_wan_ip || e)),
        S.push(FormatUtil.formatField(t.wlan_wan_netmask || e)),
        S.push(FormatUtil.formatField(t.wlan_wan_gateway || e)),
        S.push(FormatUtil.formatField(t.wlan_wan_mac ? t.wlan_wan_mac.match(/[0-9a-f]{2}/gi).join(":").toUpperCase() : e)),
        S.push(FormatUtil.formatField(t.wlan_wan_dns || e)),
        S.push(FormatUtil.formatField(t.wlan_wan_dns2 || e)),
        a = "2" == Page.networkMode || "5" == Page.networkMode && "1" == Page.priorityStrategy ? Page.createTable(DOC.title.wiredWan, u, _, u.length, 1, "detail2") : "3" == Page.networkMode || "4" == Page.networkMode ? Page.createTable("3" == Page.networkMode ? DOC.title.wlan2g : DOC.title.wlan5g, T, S, T.length, 1, "detail2") : Page.createTable(DOC.title.wan, m, c, m.length, 1, "detail2"),
        $("#wan_info").html(a),
        Page.setStripeTable("#wan_info");
        var I = [];
        I.push(DOC.title.ICCID),
        I.push(DOC.title.gnssHwStatus),
        I.push(DOC.title.satelliteNnum),
        I.push(DOC.title.longitude),
        I.push(DOC.title.latitude),
        I.push(DOC.title.terPhoneNumber),
        I.push(DOC.title.terID),
        I.push(DOC.title.nciConnectStatus),
        I.push(DOC.title.tmConnectStatus);
        var E = [];
        E.push(t.ICCID || e),
        "0" == t.gnss_hw_status ? E.push(PROMPT.status.normal) : "1" == t.gnss_hw_status ? E.push(PROMPT.status.abnormal) : E.push(e),
        E.push(t.satellite_num || e),
        E.push(t.longitude || e),
        E.push(t.latitude || e),
        E.push(t.jt808_terminal_phone_number || e),
        E.push(t.jt808_terminal_id || e),
        "0" == t.jt808_nci_connect_status ? E.push(DOC.status.disconnected) : "1" == t.jt808_nci_connect_status ? E.push(DOC.status.connected) : E.push(e),
        "0" == t.jt808_tm_connect_status ? E.push(DOC.status.disconnected) : "1" == t.jt808_tm_connect_status ? E.push(DOC.status.connected) : E.push(e),
        t.tm_operate_info_capability && "1" == t.tm_operate_info_capability && (s = Page.createTable(DOC.title.omInfo, I, E, I.length, 1, "detail2")),
        $("#om_info").html(s),
        Page.setStripeTable("#om_info"),
        setTimeout(O, 1e4)
    }
    Page.isNULLToSpace = !0;
    var i = "$"
      , a = "@"
      , s = "Serving CellID"
      , r = "Physical CellID"
      , l = "Frequency Band"
      , o = "EARFCN/ARFCN"
      , d = "Downlink Bandwidth"
      , m = "RSRQ"
      , c = "RSRP"
      , u = "RSRP2"
      , _ = "RSSI"
      , T = "RSSI2"
      , S = "SINR"
      , I = "SINR2"
      , E = "TZTRANSMODE"
      , h = "TZTA"
      , f = "TZTXPOWER";
    function g(e) {
        Page.isOpenPage = !0,
        Page.getHtml(MenuItem.CLIENT_LIST.url, MenuItem.CLIENT_LIST.cmd, function(e) {
            $("#client_info_hidden").html(e)
        }),
        Page.postJSON({
            json: {
                method: JSONMethod.POST,
                cmd: RequestCmd.GET_LTE_STATUS
            },
            success: function(t) {
                for (var g = {}, O = "", C = t.message.split(i), p = 0; p < C.length; p++) {
                    var N = C[p].split(a);
                    if (2 == N.length)
                        switch (N[0]) {
                        case o:
                            g.freq = N[1];
                            break;
                        case l:
                            g.band = N[1];
                            break;
                        case c:
                            g.rsrp = N[1];
                            break;
                        case u:
                            g.rsrp2 = N[1];
                            break;
                        case m:
                            g.rsrq = N[1];
                            break;
                        case S:
                            g.sinr = N[1];
                            break;
                        case I:
                            g.sinr2 = N[1];
                            break;
                        case _:
                            g.rssi = N[1];
                            break;
                        case T:
                            g.rssi2 = N[1];
                            break;
                        case s:
                            O = parseInt(N[1], 10),
                            isNaN(O) ? (g.globalCellId = O,
                            g.enodeBId = O) : (g.globalCellId = O.toString(16).toUpperCase(),
                            Page.cellId = O % 256,
                            Page.enodeId = (O - Page.cellId) / 256,
                            g.enodeBId = String.format("{0}/{1}", Page.enodeId, Page.cellId));
                            break;
                        case r:
                            g.phyCellId = N[1];
                            break;
                        case d:
                            g.bandWidth = N[1];
                            break;
                        case E:
                            g.tm = N[1];
                            break;
                        case h:
                            g.tzta = N[1];
                            break;
                        case f:
                            g.txpower = N[1]
                        }
                }
                g,
                n(e)
            },
            fail: function() {
                n(e)
            },
            error: function() {
                n(e)
            }
        })
    }
    function O() {
        Page.postJSON({
            json: {
                cmd: RequestCmd.ROUTER_INFO
            },
            success: function(e) {
                t = e,
                n(e)
            },
            fail: function() {
                g(t)
            },
            error: function() {
                g(t)
            }
        })
    }
    O(),
    n(t)
}
function MenuHead(e, t, n) {
    this.css = e,
    this.text = t,
    this.bodys = []
}
function MenuBody(e, t) {
    this.id = e,
    this.text = t
}
MenuHead.prototype.push = function(e, t, n, i) {
    this.bodys.push(e)
}
;
var MenuUtil = {
    create: function(e) {
        for (var t, n, i, a = new StringBuilder, s = 0, r = e.length; s < r; s++) {
            t = e[s],
            a.append(String.format('<h3 class="{0}">{1}</h3>', t.css, t.text)),
            a.append(String.format('<ul class="{0}">', t.css));
            for (var l = 0, o = (i = t.bodys).length; l < o; l++)
                n = i[l],
                a.append(String.format('<li><a id="{0}">{1}</a></li>', n.id, n.text));
            a.append("</ul>")
        }
        return a.toString()
    }
};
function secondMenuClick(e, t) {
    $("#detail_container").pannel({
        items: e,
        id: t,
        init: function(e) {
            var n = window.location.hash.split("#");
            _clearInterval(),
            n.length > 2 && t == n[1] ? e.eq(n[2]).click() : e.eq(0).click()
        }
    })
}
function resetTime() {
    Page.postJSON({
        json: {
            cmd: RequestCmd.RESET_LOGIN_TIME,
            method: JSONMethod.GET
        },
        success: function(e) {
            sessionStorage.token = e.token
        }
    })
}
function _clearInterval() {
    window.TIMEOUT && (clearTimeout(window.TIMEOUT),
    window.TIMEOUT = null),
    window.TIMEOUT2 && (clearTimeout(window.TIMEOUT2),
    window.TIMEOUT2 = null,
    Page.postJSON({
        json: {
            cmd: RequestCmd.NETWORK_TOOLS,
            method: JSONMethod.POST,
            subcmd: 0,
            pingTimes: 0,
            url: ""
        }
    })),
    Page.timer && (clearInterval(Page.timer),
    Page.timer = null)
}
function errorCodeInfo(e, t) {
    var n = "";
    switch (e) {
    case 500:
        n = PROMPT.status.error500;
        break;
    case 501:
        n = PROMPT.status.error501;
        break;
    case 502:
        n = 1 == t ? PROMPT.status.error502PLNM : PROMPT.status.error502;
        break;
    case 503:
        n = PROMPT.status.error503;
        break;
    case 504:
        n = PROMPT.status.error504;
        break;
    case 505:
        n = PROMPT.status.error505;
        break;
    case 506:
        n = PROMPT.status.error506;
        break;
    case 507:
        n = PROMPT.status.error507;
        break;
    default:
        n = PROMPT.status.errorUnknow
    }
    return n
}
function fyAlertMsgLoading(e) {
    unFocus();
    var t = "";
    t = e || CHECK.format.waiting,
    fyAlert.alert({
        title: t,
        closeBtn: !1,
        animateType: 2,
        shadowClose: !1,
        area: ["400px", "150px"],
        direction: ["center", "center"],
        skin: "fyAlert-green",
        content: '<div class="el-loading-spinner" style="position: relative;margin-top: 10px"><svg viewBox="25 25 50 50" class="circular" style="height: 50px; width: 50px;"><circle cx="50" cy="50" r="20" fill="none" class="path"></circle></svg></div>'
    })
}
function fyAlertMsgSuccess(e) {
    try {
        fyAlert.destory()
    } catch (e) {}
    var t = "";
    t = e || PROMPT.status.success,
    fyAlert.alert({
        title: t,
        closeBtn: !1,
        animateType: 0,
        shadowClose: !1,
        area: ["400px", "auto"],
        direction: ["center", "center"],
        skin: "fyAlert-green",
        content: '<p style="text-align:center"><i style="font-size: 64px; color: #6abd12;" class="el-icon-circle-check"></i></p>',
        btns: {
            btns: function(e) {
                e.destory()
            }
        }
    })
}
function fyAlertMsgFail(e, t, n) {
    try {
        fyAlert.destory()
    } catch (e) {}
    var i = "";
    i = e || PROMPT.status.fail,
    null == n && (n = ""),
    fyAlert.alert({
        title: i,
        closeBtn: !1,
        animateType: 0,
        shadowClose: !1,
        area: ["400px", "auto"],
        direction: ["center", "center"],
        skin: "fyAlert-green",
        content: '<p style="text-align:center;font-size: 12px;font-weight:bold;"><i style="font-size: 64px; color: #ee1919;" class="el-icon-circle-close"></i><br/>' + n + "</p>",
        btns: {
            btns: function(e) {
                e.destory()
            }
        }
    })
}
function fyConfirmMsg2(e, t, n) {
    t && fyAlert.destory(),
    fyAlert.alert({
        title: DOC.lbl.prompt,
        closeBtn: !1,
        animateType: 0,
        shadowClose: !1,
        area: ["400px", "auto"],
        direction: ["center", "center"],
        skin: "fyAlert-green",
        content: e,
        btns: {
            btns: function(e) {
                e.destory(),
                n && n()
            }
        }
    })
}
function fyConfirmMsg(e, t) {
    fyAlert.alert({
        title: " ",
        closeBtn: !1,
        animateType: 0,
        shadowClose: !1,
        area: ["400px", "auto"],
        direction: ["center", "center"],
        skin: "fyAlert-green",
        content: e,
        btns: {
            btns: function(e) {
                e.destory(),
                t()
            },
            delete: function(e) {
                e.destory()
            }
        }
    }),
    document.activeElement.blur()
}
function fyConfirmMsg3(e, t, n) {
    fyAlert.alert({
        title: " ",
        closeBtn: !1,
        animateType: 0,
        shadowClose: !1,
        area: ["400px", "auto"],
        direction: ["center", "center"],
        skin: "fyAlert-green",
        content: e,
        btns: {
            btns: function(e) {
                e.destory(),
                t()
            },
            delete: function(e) {
                e.destory(),
                n()
            }
        }
    }),
    document.activeElement.blur()
}
function fyAlertMsgWarn(e, t) {
    t && fyAlert.destory();
    var n = '<p style="text-align:center;"><img src="../images/warning.png"></p>';
    e && (n = n + '<p style="overflow: hidden;max-height: 38px;text-align:center;">' + e + "</p>"),
    fyAlert.alert({
        title: PROMPT.status.warn,
        closeBtn: !1,
        animateType: 0,
        shadowClose: !1,
        area: ["400px", "auto"],
        direction: ["center", "center"],
        skin: "fyAlert-green",
        content: n,
        btns: {
            btns: function(e) {
                e.destory()
            }
        }
    })
}
function fyAlertMsgLoadText(e) {
    var t = '<p style="text-align:center;"><img src="../images/loading.gif"></p>';
    e && (t = t + '<p style="overflow: hidden;max-height: 38px;text-align:center;">' + e + "</p>"),
    fyAlert.alert({
        title: CHECK.format.waiting,
        closeBtn: !1,
        animateType: 0,
        shadowClose: !1,
        area: ["400px", "auto"],
        direction: ["center", "center"],
        skin: "fyAlert-green",
        content: t,
        btns: {
            btns: function(e) {
                e.destory()
            }
        }
    })
}
function fyAlertDestory() {
    fyAlert.destory()
}
function IsPC() {
    for (var e = navigator.userAgent, t = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"], n = !0, i = 0; i < t.length; i++)
        if (e.indexOf(t[i]) > 0) {
            n = !1;
            break
        }
    return n
}
function utf16to8(e) {
    var t, n, i, a;
    for (t = "",
    i = e.length,
    n = 0; n < i; n++)
        (a = e.charCodeAt(n)) >= 1 && a <= 127 ? t += e.charAt(n) : a > 2047 ? (t += String.fromCharCode(224 | a >> 12 & 15),
        t += String.fromCharCode(128 | a >> 6 & 63),
        t += String.fromCharCode(128 | a >> 0 & 63)) : (t += String.fromCharCode(192 | a >> 6 & 31),
        t += String.fromCharCode(128 | a >> 0 & 63));
    return t
}
function isHide(e) {
    return "1" != Page.hideNoAccount[e]
}
function isHide2(e) {
    return "1" == Page.hideNoAccount[e]
}
function isUtf16(e) {
    return e = e.replace(/[\ud800-\udbff][\udc00-\udfff]/g, function(e) {
        var t, n;
        return 2 === e.length ? (t = e.charCodeAt(0).toString(16),
        n = e.charCodeAt(1).toString(16),
        bq = (t + n).toUpperCase(),
        (t + n).toUpperCase()) : e
    })
}
function unFocus() {
    var e = document.createElement("button");
    e.style.position = "fixed",
    e.style.opacity = 0,
    document.body.appendChild(e),
    e.focus(),
    e.remove()
}
function ipFormat(e, t, n) {
    if (t) {
        if (/^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(t)) {
            for (var i = t.split("."), a = 0; a < i.length; a++)
                if (i[a].length > 1 && "0" == i[a][0] || i[a] > 255)
                    return void n(e.message);
            "0" === i[0] || "127" === i[0] || i[0] >= 224 || "0" === i[3] || "255" === i[3] ? (e.message = CHECK.format.ipRule,
            n(e.message)) : n()
        } else
            n(e.message)
    } else
        n()
}
function clatPrefix(e, t, n) {
    if (t) {
        var i = t.split("/")
          , a = /:/.test(i[0]) && i[0].match(/:/g).length < 8 && /::/.test(i[0]) ? 1 == i[0].match(/::/g).length && /^::$|^(::)?([\da-f]{1,4}(:|::))*[\da-f]{1,4}(:|::)?$/i.test(i[0]) : /^([\da-f]{1,4}:){7}[\da-f]{1,4}$/i.test(i[0])
          , s = [96, 64, 56, 48, 40, 32].indexOf(parseInt(i[1])) >= 0;
        a && s ? n() : n(e.message)
    } else
        n()
}
function formatFlowUnit(e, t=0) {
    if (!e || e < 0)
        return ["0.00", " ", "MB"];
    let n = (e * 1024 ** t).toFixed(2)
      , i = "MB";
    return n >= 1024 && n < 1048576 ? (n = (n / 1024).toFixed(2),
    i = "GB") : n >= 1048576 && (n = (n / 1048576).toFixed(2),
    i = "TB"),
    [n, " ", i]
}
var checkAPNName_right = function(e, t, n) {
    /^(rac|lac|sgsn|\.|.*\.\.|.*-\.|.*\.-|\s)/.test(t) || /(\.gprs|\s)$/.test(t) || !/^[\.a-zA-Z0-9- ]{1,64}$/.test(t) ? n(e.message) : n()
};
