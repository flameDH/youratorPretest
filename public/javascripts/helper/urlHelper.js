var urlHelper=
{
    queryString : function(name){
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
        return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    },
    queryString2 : function(name){
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.href);
        return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    },
    redirect : function(pageName)
    {
        window.location.href=pageName;
    },
    redirectToLogin:function(LoginPageURL,LOGIN_URL,userCookies)
    {
        if (userCookies && userCookies[LOGIN_URL]) {
            var url = LoginPageURL + userCookies[LOGIN_URL];
            urlHelper.redirect(url);
        }

    }
}
