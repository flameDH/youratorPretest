var jobId;

$(function () {
    jobId=urlHelper.queryString("id");
    jobPage.init();
});

var jobPage = {
    init: function(){
        var self = this;
        jobPage.detail();
    },
    detail: function(){
        var self = this;
        var apiUrl= api.jobDetail+jobId;

        dataHelper.async_get(apiUrl,'',function(data){
            if (data.ERR == 0) {
                $("#jobTitle").text(data.JOB_NAME);
                $("#jobName").text(data.JOB_NAME);
                $("#CONTENT").text(data.CONTENT);
                $("#REQUIREMENT").text(data.CONCEPT);
                $("#BENEFIT").text(data.SALARY);
                $("#SALARY").text(data.REPORT);
            }else{
                dataHelper.dealError(data);
            }
        });
    }
}