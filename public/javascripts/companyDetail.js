var companyId;

$(function () {
    companyId=urlHelper.queryString("id");
    companyDetail.init();
});

var companyDetail = {
    init:function(){
        var self = this;
        self.detail();
        self.jobList();
    },
    detail: function(){
        
        var apiUrl = api.companyDetail+companyId;

        dataHelper.async_get(apiUrl,'',function(data){
            if (data.ERR == 0) {
                $("#companyTitle").text(data.NAME);
                $("#companyName").text(data.NAME);
                $("#INTRO").text(data.INTRO);
                $("#CONCEPT").text(data.CONCEPT);
                $("#SALARY").text(data.SALARY);
                $("#REPORT").text(data.REPORT);
            }else{
                dataHelper.dealError(data);
            }
        });
    },
    jobList:function(){
        var self = this;
        var apiUrl = api.jobCompanyList+companyId;

        dataHelper.async_get(apiUrl,'',function(data){
            if (data.ERR == 0) {
                var list = data.list;
                if (list && list !== undefined) {
                    var renderHtml = '';
                    if (list.length > 0) {
                        var listLength = list.length;
                        for (var i = 0; i < listLength; i++) {
                            var e = list[i];
                            var jobId = e.jobId;
                            var name = e.jobName;
                            var renderData = {
                                'NAME': name,
                                'URL':"jobDetail.html?id="+jobId,
                            };
                            var dom = getRenderDom(renderData, listLength);
                            renderHtml += dom;
                        }
                    }
                }
                $('#job-rand').html(renderHtml);
            }else{
                dataHelper.dealError(data);
            }
        });
    }
}

function getRenderDom(data, length) {
    var url = data.URL;
    var name = data.NAME;

    var dom = '<div class="gal-item col col'+length+'">' +
        '<a href="'+url+'" class="img-shadow">' +
        '</div>' +
        '<div class="title">'+name+'</div>' +
        '</div>' +
        '</a>' +
        '</div>';

    return dom;
}