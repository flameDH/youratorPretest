var dataHelper = {
    get: function (url, header, callback) {
        var getData = null;
        $.ajax({
            url: url,
            type: 'GET',
            dataType: "Json",
            contentType: "application/json; charset=utf-8",
            async: false,
            headers: header,
            success: function (data) {
                getData = data;
                if (jQuery.isFunction(callback)) {
                    callback(data);
                }


            },
            error: function (data) {
                getData = data;
                if (jQuery.isFunction(callback)) {
                    callback(data);
                }

            }

        });

        return getData;

    },
    post: function (url, header, reqData, callback) {


        $.ajax({
            url: url,
            type: 'POST',
            dataType: "Json",
            async: false,
            headers: header,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(reqData),
            success: function (data) {
                getData = data;
                if (jQuery.isFunction(callback)) {
                    callback(data);
                }
            },
            error: function (data) {
                getData = data;
                if (jQuery.isFunction(callback)) {
                    callback(data);
                }

            }

        });
        return getData;

    },
    form_post: function (url, header, object, callback) {
        var self = this;
        var getData = null;
        $.ajax({
            url: url,
            headers: header,
            data: object,
            async: false,
            processData: false,
            contentType: false,
            type: 'POST',
            success: function (data) {
                getData = data;
                if (jQuery.isFunction(callback)) {
                    callback(data);
                }
            },
            error: function (data) {
                getData = data;
                if (jQuery.isFunction(callback)) {
                    callback(data);
                }
            }
        });
        return getData;
    },
    async_post: function (url, header, object, callback) {
        $.ajax({
            url: url,
            headers: header,
            type: "POST",
            data: JSON.stringify(object),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                callback(data);
            },
            error: function (data) {
                callback(data);
            }
        });
    },
    async_get: function (url, header, callback) {
        $.ajax({
            url: url,
            headers: header,
            type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                callback(data);
            },
            error: function (data) {
                callback(data);
            }
        });
    },
    async_form_post: function (url, header, object, callback) {
        $.ajax({
            url: url,
            headers: header,
            data: object,
            processData: false,
            contentType: false,
            type: 'POST',
            success: function (data) {
                callback(data);
            },
            error: function (data) {
                callback(data);
            }
        });
    },
    dealError: function (data) {
        var errCode = data.ERR;
        var illegalArg = data.ILLEGALARGUMENT;
        var errMsg;
        switch (errCode) {
            case 0:
                errMsg = '成功';
                break;
            case 400:
                errMsg = '伺服器錯誤';
                break;
            case 401:
                errMsg = '請求參數錯誤，請確認資料是否正確';
                break;
            case 403:
                errMsg = '登入逾期，請重新登入';
                break;
            case 404:
                errMsg = '找不到此頁面';
                break;
            case 405:
                errMsg = 'FORBIDDEN';
                break;
            case 406:
                errMsg = '密碼錯誤';
                break;
            case 407:
                errMsg = 'permission denied';
                break;
            case 504:
                errMsg = '此帳號已存在';
                break;
            case 505:
                errMsg = '請確認帳號密碼是否輸入正確';
                break;
            case 506:
                errMsg = '無效的驗證碼';
                break;
            case 508:
                errMsg = '使用者狀態已被更改，請重新登入';
                break;
            case 5000:
                errMsg = 'KEY IS NOT EXIST';
                break;
            case 9002:
                errMsg = '圖片不得少於一張';
                break;
            case 10000:
                errMsg = '此物件不存在';
                break;
            case 32001:
                errMsg = 'REPLY NOT FOUND';
                break;
            case 33000:
                errMsg = 'SEMINAR NOT FOUND';
                break;
            case 35000:
                errMsg = 'REQUIREMENT NOT FOUND';
                break;
            default:
                errMsg = '發生未知錯誤';
        }
        if (errCode === 403) {
            alert(errMsg);
            Cookies.remove('banana_house');
            urlHelper.redirect('login.html');
        } else if (errCode === 404) {
            alert(errMsg);
            urlHelper.redirect('index.html');
        } else if (errCode === 407) {
            alert(errMsg);
            urlHelper.redirect('index.html');
        } else if (errCode == 508) {
            alert(errMsg);
            var userData = userInfo.checkUserCookie();
            if (userData && userData !== undefined) {
                var token = userData.TOKEN;
                var apiUrl = api.logout;
                var reqData = {
                    'TOKEN': token,
                };
                var resData = dataHelper.post(apiUrl, '', reqData);
            }
            $(window).unbind('beforeunload');
            Cookies.remove('banana_house');
            Cookies.remove('banaba_group_label');
            localStorage.removeItem('banana_chatroom_expand');
            localStorage.removeItem('banana_chatroom_active_id');
            localStorage.removeItem('chat_unread');
            localStorage.removeItem("banana_preview");
            urlHelper.redirect('login.html?logout=1');
        } else if (errCode === 10000) {
            alert(errMsg);
            urlHelper.redirect('rent.html');
        } else {
            var errMsgText = '';
            if (illegalArg) {
                errMsgText += '錯誤參數：';
                errMsgText += illegalArg;
                errMsgText += '\n';
            }
            errMsgText += errMsg;
            alert(errMsgText);
        }
    }
}