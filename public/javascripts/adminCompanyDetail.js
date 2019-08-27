var companyId;

$(function () {
    companyId=urlHelper.queryString("id");
    page.init();
});

var page={
    init:function(){
        var self=this;
        self.read();

        $('#save').unbind().click(function (e) {
            e.preventDefault();
            self.update();
          });
    },
    read:function(){
        var apiUrl = api.companyDetail+companyId;

        dataHelper.async_get(apiUrl,'',function(data){
            if (data.ERR == 0) {
                //$("#companyTitle").text(data.NAME);
                $("#companyName").text(data.NAME);
                $("#INTRO").val(data.INTRO);
                $("#CONCEPT").val(data.CONCEPT);
                $("#SALARY").val(data.SALARY);
                $("#REPORT").val(data.REPORT);
            }else{
                dataHelper.dealError(data);
            }
        });
    },
    update:function(){
        var apiUrl = api.updateCompany;

        //TODO get info
        var param={
            "ID":companyId,
            "INTRO":$("#INTRO").val(),
            "CONCEPT":$("#CONCEPT").val(),
            "SALARY":$("#SALARY").val(),
            "REPORT":$("#REPORT").val()
        }

        dataHelper.async_post(apiUrl,'',param,function(data){
            if (data.ERR == 0) {
                //TODO forward admin company;
                location.href="adminCompany.html?id="+companyId;
            }
            else{
                dataHelper.dealError(data);
            }
        });

    }

}