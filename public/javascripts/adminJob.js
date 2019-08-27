var jobId;
var companyId;
$(function () {
    jobId=urlHelper.queryString("id");
    companyId=urlHelper.queryString("company");
    page.init();
});

var page = {
    init: function(){
        var self= this;

        if(jobId){
            self.read();
        }

        $('#save').unbind().click(function (e) {
            e.preventDefault();
            
             if(jobId){
                self.update();
             }else{
                self.create();
             }
          });
        
    },
    read: function(){
        var apiUrl=api.jobDetail+jobId;

        dataHelper.async_get(apiUrl,'',function(data){
            if (data.ERR == 0) {
                $("#JOBNAME").val(data.JOB_NAME);
                $("#CONTENT").val(data.CONTENT);
                $("#REQUIREMENT").val(data.REQUIREMENT);
                $("#BENEFIT").val(data.BENEFIT);
                $("#SALARY").val(data.SALARY);
            }else{
                dataHelper.dealError(data);
            }
        });

    },
    update:function(){
        var apiUrl=api.updateJob;

        param={
            JOB_ID:jobId,
            COM_ID:companyId,
            JOB_NAME: $("#JOBNAME").val(),
            CONTENT:$("#CONTENT").val(),
            REQUIREMENT:$("#REQUIREMENT").val(),
            BENEFIT:$("#BENEFIT").val(),
            SALARY:$("#SALARY").val()
        }

        dataHelper.async_post(apiUrl,'',param,function(data){
            if (data.ERR == 0) {
                location.href="adminCompany.html?id="+companyId;
            }
            else{
                dataHelper.dealError(data);
            }
        });
    },
    create:function(){
        var apiUrl=api.createJob;

        param={
            JOB_ID:-1,
            COM_ID:companyId,
            JOB_NAME: $("#JOBNAME").val(),
            CONTENT:$("#CONTENT").val(),
            REQUIREMENT:$("#REQUIREMENT").val(),
            BENEFIT:$("#BENEFIT").val(),
            SALARY:$("#SALARY").val()
        }


        dataHelper.async_post(apiUrl,'',param,function(data){
            if (data.ERR == 0) {
                location.href="adminCompany.html?id="+companyId;
            }
            else{
                dataHelper.dealError(data);
            }
        });

    }
}