$(document).ready(function() {
    if ("Microsoft Internet Explorer" == navigator.appName && parseInt(navigator.appVersion.split(";")[1].replace(/[ ]/g, "").replace("MSIE", "")) < 9)
        return AlertUtil.alertMsg(DOC.login.Browser),
        !1;
    document.oncontextmenu = function(e) {
        window.event && (e = window.event);
        try {
            var t = e.srcElement;
            return "INPUT" == t.tagName && "text" == t.type.toLowerCase() || "TEXTAREA" == t.tagName
        } catch (e) {
            return !1
        }
    }
    ,
    document.onkeydown = function() {
        if (window.event.ctrlKey && 85 == window.event.keyCode)
            return event.keyCode = 0,
            event.returnValue = !1,
            !1
    }
    ,
    Page.postJSON({
        json: {
            cmd: RequestCmd.INIT_PAGE
        },
        success: function(t) {
            sessionStorage.theme = t.user_web_theme,
            Page.theme = t.user_web_theme;
            var a = "";
            a = t.user_web_theme ? t.user_web_theme : "main.css";
            (["main.css", "main.ng0570.css", "greenTheme.css", "main.tot.css", "mobot.css", "airtel.css", "main.mtn.css", "redTheme.css", "main.langchao.css", "main.telcel.css", "main.SA0343.css", "main.EG0559.css", "main.SN0693.css", "main.BW001.css"].indexOf(t.user_web_theme) < 0 && (a = "main.css"),
            "AR" == t.language.toUpperCase() && (a = "main.EG0559.css" == t.user_web_theme ? "main.arab_EG0559.css" : "main.arab.css"),
            t.web_browser_title && (e.web_browser_title = t.web_browser_title),
            (o = document.createElement("link")).setAttribute("href", "css/" + a + "?t=" + Math.random()),
            o.setAttribute("type", "text/css"),
            o.setAttribute("rel", "stylesheet"),
            $("head")[0].appendChild(o),
            "logo_t3.png" == t.web_logo_path) && ((o = document.createElement("link")).setAttribute("href", "t3_favicon.ico"),
            o.setAttribute("type", "image/x-icon"),
            o.setAttribute("rel", "shortcut icon"),
            $("head")[0].appendChild(o));
            "logo_dito_home.png" == t.web_logo_path && ((o = document.createElement("link")).setAttribute("href", "/images/dido-favicon.png"),
            o.setAttribute("type", "image/x-icon"),
            o.setAttribute("rel", "shortcut icon"),
            $("head")[0].appendChild(o));
            "main.tot.css" == t.user_web_theme && ((o = document.createElement("link")).setAttribute("href", "/images/nn90j-whejs.png"),
            o.setAttribute("type", "image/x-icon"),
            o.setAttribute("rel", "shortcut icon"),
            $("head")[0].appendChild(o));
            var o;
            ["logo_mexico_custom.png", "logo_claro.svg", "logo_mexico_custom_wifi2go.png"].indexOf(t.web_logo_path) > -1 && ((o = document.createElement("link")).setAttribute("href", "/images/mexico/Q_logo.png"),
            o.setAttribute("type", "image/x-icon"),
            o.setAttribute("rel", "shortcut icon"),
            $("head")[0].appendChild(o))
        }
    });
    var e = new Vue({
        el: "#login_box",
        data: {
            network_operator: "",
            loginLan: "",
            colon: DOC.colon,
            loginUsername: "",
            loginPasswd: "",
            btnLogin: "",
            timerFlag: !1,
            login_times_text: "",
            loginTimesIsShow: !1,
            accountLock: "",
            isLoginLanguage: !1,
            isShowLogin: !1,
            isShowInfo: !0,
            languageSelectValue: "",
            languageSelectList: [],
            languageSelectShow: !1,
            autoUpgradeName: "",
            showAutoUp: !1,
            autoUpgrade: !1,
            passType: "password",
            passType1: "password",
            web_browser_title: "",
            checkbox: !1,
            usernameVal: "",
            passwordVal: "",
            refresh: DOC.btn.refresh,
            passError: !1,
            flowLimitFlag: "",
            showclaroLogin: !1,
            antennaStatus: "",
            showAntenaInfo: !1,
            copyright: "",
            isMexicoLogo: !1,
            loginTimer: null
        },
        methods: {
            validateClick: function() {
                this.passwordVal.length > 63 ? this.passError = !0 : this.passError = !1
            },
            createTime: function(t) {
                clearInterval(this.loginTimer);
                let a = Number(t);
                this.loginTimer = setInterval(function() {
                    --a < 0 ? (clearInterval(e.loginTimer),
                    fyAlert.destory()) : $("#loginCountdown").text(CHECK.format.attempts3 + Page.transSecondTime(a.toString()))
                }, 1e3)
            },
            initPage: function() {
                function t() {
                    Page.initPage(!0),
                    $(window).resize(function() {
                        Page.initPage(!0)
                    });
                    -1 === ["logo_mexico_custom.png", "logo_claro.svg", "logo_mexico_custom_wifi2go.png"].indexOf(Page.logoPath) ? document.title = e.web_browser_title || DOC.head : document.title = "Quamtum Connect 2",
                    "logo_claro.svg" == Page.logoPath && $("#claro_food").css("display", "flex"),
                    e.isMexicoLogo = Page.logoPath.indexOf("logo_mexico_custom") > -1,
                    e.loginLan = DOC.login.language,
                    e.loginUsername = DOC.login.username,
                    e.loginPasswd = DOC.login.passwd,
                    e.btnLogin = DOC.btn.login,
                    e.accountLock = CHECK.format.accountLock,
                    e.isLoginLanguage = !0,
                    e.languageSelectValue = Page.language,
                    e.autoUpgradeName = DOC.btn.loginAuto;
                    var t = $("#username").focus()
                      , a = $("#passwd")
                      , o = $("#btnLogin")
                      , n = (o.val(),
                    sessionStorage.getItem("login_times") || 3);
                    switch ((parseInt(n, 10) < 1 || 3 == parseInt(n, 10)) && e.getNextText(),
                    o.click(function() {
                        Page.postJSON({
                            json: {
                                cmd: RequestCmd.GET_NEXT_LOGIN_TIME,
                                method: JSONMethod.GET,
                                sessionId: ""
                            },
                            success: function(n) {
                                var s = n.token;
                                localStorage.setItem("flag1", "false"),
                                localStorage.setItem("index1", "0-0");
                                var i = t.val()
                                  , r = a.val();
                                if ("" == i)
                                    return AlertUtil.alertMsg(CHECK.required.username),
                                    t.focus(),
                                    !1;
                                if ("" == r)
                                    return AlertUtil.alertMsg(CHECK.required.passwd),
                                    a.focus(),
                                    !1;
                                o.disable();
                                Page.postJSON({
                                    json: {
                                        cmd: RequestCmd.LOGIN,
                                        method: JSONMethod.POST,
                                        sessionId: Md5.md5(Math.random().toString()) + Md5.md5(Math.random().toString()),
                                        username: i,
                                        passwd: sha256_digest(s + r),
                                        isAutoUpgrade: e.autoUpgrade ? "1" : "0"
                                    },
                                    success: function(t) {
                                        "fail" == t.login_fail || "fail" == t.login_fail2 ? ("fail" == t.login_fail && parseInt(t.login_times, 10) % 3 != 0 && ("EG3141" == Page.aeraId || "EG3310" == Page.aeraId || "IOT002" == Page.aeraId || "EG3121" == Page.aeraId ? fyConfirmMsg2(String.format("<span class=\"fail\" style='font-size:16px;'>{0}{1}</span>", CHECK.format.attempts, 3 - parseInt(t.login_times, 10) % 3)) : fyConfirmMsg2(String.format("<span class=\"fail\" style='font-size:16px;'>{0}{1}</span>", CHECK.format.attempts2, t.login_times))),
                                        "fail" != t.login_fail2 && t.login_times % 3 != 0 || (fyConfirmMsg2(String.format('<span class="fail" id="loginCountdown" style=\'font-size:16px;\'>{0}{1}</span>', CHECK.format.attempts3, Page.transSecondTime(t.login_time)), !1, function() {
                                            clearInterval(e.loginTimer)
                                        }),
                                        e.createTime(t.login_time))) : (t.success,
                                        sessionStorage.sessionId = t.sessionId,
                                        sessionStorage.canLogin = t.AUTH,
                                        sessionStorage.level = t.user_level,
                                        location.href = Page.getUrl("index.html"),
                                        e.checkbox ? (localStorage.setItem("usernameValue", e.usernameVal),
                                        localStorage.setItem("passwordValue", Base64.encode(e.passwordVal))) : (localStorage.setItem("usernameValue", ""),
                                        localStorage.setItem("passwordValue", "")))
                                    },
                                    complete: function() {
                                        o.enable()
                                    }
                                })
                            }
                        })
                    }),
                    t.keydown(function(e) {
                        13 == e.which && a.focus()
                    }),
                    a.keydown(function(e) {
                        13 == e.which && (o.attr("disabled") || 0 != $(".fy-alert-shadow").length || o.click())
                    }),
                    sessionStorage.canLogin = "",
                    getOpenInfo(),
                    e.getCopyright(),
                    StatusUtil.getSysStatus(),
                    e.antennaStatus) {
                    case "all":
                        e.antennaStatus = DOC.antenna.allDirection;
                        break;
                    case "front":
                        e.antennaStatus = DOC.antenna.frontDirection;
                        break;
                    case "back":
                        e.antennaStatus = DOC.antenna.backDirection
                    }
                    "1" == sessionStorage.antenna_status_display && (e.showAntenaInfo = !0)
                }
                Page.postJSON({
                    json: {
                        cmd: RequestCmd.INIT_PAGE
                    },
                    success: function(a) {
                        var o = Page.parseHex(a.hide_no_account);
                        if (o.length < 2)
                            var n = o[0].split("").reverse().join("");
                        else
                            n = o.join("").split("").reverse().join("");
                        Page.hideNoAccount = n,
                        isHide2(seniorHide.web_page_hide_set_4) ? (e.isShowInfo = !1,
                        $("#login_template").show(),
                        $("#username").focus()) : (e.isShowInfo = !0,
                        e.isShowLogin = !0);
                        var s = a.language.toLowerCase();
                        Page.logoPath = a.web_logo_path,
                        Page.networkMode = a.network_mode,
                        Page.priorityStrategy = a.priorityStrategy,
                        Page.aeraId = a.aeraId,
                        Page.allowCopyPaste = "1" == a.allowCopyPaste || "SC001" == a.aeraId,
                        Page.titleModel = a.board_type,
                        Page.od_wifi_status = a.od_wifi_status || "",
                        Page.wifiType = a.wifiType,
                        Page.ledNumber = ["3", "4", "5"].indexOf(a.ledNumber) > -1 ? parseInt(a.ledNumber) : 5;
                        var i = [];
                        a.language_support ? (i = parseInt(a.language_support, 16).toString(2).split("").reverse(),
                        e.languageSelectList = Page.getSupportlanguageList.filter(e => "1" == i[e.index])) : e.languageSelectList = Page.getSupportlanguageList;
                        (-1 === ["1", "3", "5", "7", "B", "F"].indexOf(a.id_wifi_status) || isHide2(10) && "1" != Page.wifiType) && $("#wifiInfo").hide(),
                        "1" == Page.od_wifi_status && Page.pageHideCheck3(357) && $("#wifiInfo").show(),
                        -1 === ["2", "3", "7", "A", "B", "F"].indexOf(a.id_wifi_status) && $("#wifiInfo5g").detach(),
                        isHide2(10) && "2" != Page.wifiType && $("#wifiInfo5g").hide(),
                        "1" == a.language_show ? e.languageSelectShow = !0 : e.languageSelectShow = !1,
                        "1" == a.showAutoUpgrade ? e.showAutoUp = !0 : e.showAutoUp = !1,
                        e.autoUpgrade = "1" == a.isAutoUpgrade,
                        a.web_browser_title && (e.web_browser_title = a.web_browser_title),
                        sessionStorage.antenna_status_display = a.antenna_status_display,
                        e.antennaStatus = a.antenna_status || "-",
                        Page.requireChangeLang(s) ? (Page.language = s,
                        $("script[src^='js/language/']").remove(),
                        $.getScript("js/language/" + s + ".js", t)) : t();
                        var r = a.id_wifi_status || "";
                        isHide(33) && (Page.supportWifi2g = ["1", "3", "5", "7", "B", "F"].indexOf(r) > -1),
                        isHide(34) && (Page.supportWifi5g = ["2", "3", "7", "A", "B", "F"].indexOf(r) > -1)
                    }
                })
            },
            getCurrentNet: function() {
                Page.postJSON({
                    json: {
                        cmd: RequestCmd.GET_SYS_STATUS
                    },
                    success: function(t) {
                        var a = t.network_type_str;
                        "MTN-NG" == t.network_operator && (e.currentNet = "4G" == a ? "LTE MTN-NG" : "3G" == a ? "3G MTN-NG" : "2G" == a ? "2G MTN-NG" : ""),
                        t.network_operator && "EG0559" == Page.aeraId ? (e.network_operator = t.network_operator,
                        $("#networkOperator").show()) : $("#networkOperator").hide()
                    }
                })
            },
            claro_user_enter: function(e) {
                13 == e.keyCode && $("#claro_passwd").focus()
            },
            claro_passwd_enter: function(e) {
                13 == e.keyCode && $("#btnLogin").click()
            },
            cloar_btn_click: function() {
                $("#btnLogin").click()
            },
            changeLanguageSelect: function() {
                Page.postJSON({
                    json: {
                        cmd: RequestCmd.CHANGE_LANGUAGE,
                        method: JSONMethod.POST,
                        sessionId: "",
                        languageSelect: e.languageSelectValue
                    },
                    success: function(e) {},
                    complete: function() {
                        var e = location.href;
                        e.indexOf("#") > -1 && (e = e.substring(0, e.indexOf("#"))),
                        e.indexOf("?") > -1 && (e = e.substring(0, e.indexOf("?"))),
                        location.href = Page.getUrl(e)
                    }
                })
            },
            getNextText: function() {
                Page.postJSON({
                    json: {
                        cmd: RequestCmd.GET_NEXT_LOGIN_TIME,
                        method: JSONMethod.GET,
                        sessionId: ""
                    },
                    success: function(t) {
                        if ("0" == t.buffer) {
                            var a = 180 - t.netx_login_time;
                            e.login_times_text = String.format("<span class=\"fail\" style='font-size:16px;'>{0}{1}</span>", e.accountLock, a),
                            e.loginTimesIsShow = !(a < 1)
                        }
                    }
                })
            },
            clickLogin: function() {
                this.isShowLogin = !0,
                this.isShowInfo = !1,
                $("#login_template").show(),
                $("#username").focus()
            },
            changePage: function() {
                location.reload()
            },
            showPsw: function() {
                "password" == this.passType ? this.passType = "text" : this.passType = "password"
            },
            getCopyright() {
                var e = this;
                $.ajax({
                    url: "copyright",
                    success: function(t) {
                        (t = JSON.parse(t)).copyright && (e.copyright = DOC.copyright.format(t.copyright))
                    },
                    error: function() {
                        var t = new Date;
                        e.copyright = DOC.copyright.format("2002-" + t.getFullYear())
                    }
                })
            }
        },
        mounted: function() {
            !function() {
                var e = document.cookie.match(/[^ =;]+(?=\=)/g);
                if (e)
                    for (var t = e.length; t--; )
                        document.cookie = e[t] + "=0;expires=" + new Date(0).toUTCString()
            }(),
            this.getCurrentNet(),
            setInterval(function() {
                e.getCurrentNet()
            }, 5e3),
            this.flowLimitFlag = window.sessionStorage.flowLimitFlag,
            setInterval(function() {
                e.flowLimitFlag = window.sessionStorage.flowLimitFlag
            }, 2e3),
            this.usernameVal = localStorage.getItem("usernameValue") || "",
            localStorage.getItem("passwordValue") ? this.passwordVal = Base64.decode(localStorage.getItem("passwordValue")) : this.passwordVal = "",
            this.usernameVal && this.passwordVal ? this.checkbox = !0 : this.checkbox = !1,
            this.getNextText()
        },
        created: function() {
            this.initPage()
        }
    })
});
