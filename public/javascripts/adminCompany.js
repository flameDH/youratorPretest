var companyId;
var stateString=['開啟','關閉'];
$(function () {
    companyId=urlHelper.queryString("id");
    adminCompany.init();
});

var adminCompany={
    init: function(){
        var self = this;
        $('#updateCompany').attr("onclick",'location.href="adminCompanyDetail.html?id='+companyId+'"')
        $('#create').attr("onclick",'location.href="adminJob.html?company='+companyId+'"')
        self.jobList();
    },
    jobList:function(){
        var self = this;
        var apiUrl=api.adminJob+companyId;

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
                            var state = e.IS_OPEN;
                            var renderData = {
                                'NAME': name,
                                'URL':"adminJob.html?id="+jobId+"&company="+companyId,
                                'JOB_ID':e.jobId,
                                'STATE':state
                            };
                            var dom = getRenderDom(renderData, listLength);
                            renderHtml += dom;
                        }
                    }
                }
                $('#job-list').html(renderHtml);
                self.setButton();
            }else{
                dataHelper.dealError(data);
            }
        });

    },

    setButton:function(){
        var self = this;
        $('.job-change').unbind().click(function (e) {
          e.preventDefault();
          var id = $(this).attr('data-Id');
          self.jobState(id);
        });

        $('.job-delete').unbind().click(function (e) {
            e.preventDefault();
            var id = $(this).attr('data-Id');
            self.jobDelete(id);
          });
    },

    jobState:function(id){
        var self = this;
        var apiUrl=api.changeState;

        var param ={
            "JOB_ID":id
        }

        dataHelper.async_post(apiUrl,'',param,function(data){
            if (data.ERR == 0) {
                self.jobList();
            }
            else{
                dataHelper.dealError(data);
            }
        });
    },
    jobDelete:function(id){
        var self = this;
        var apiUrl=api.deleteJob;

        var param ={
            "JOB_ID":id
        }

        dataHelper.async_post(apiUrl,'',param,function(data){
            if (data.ERR == 0) {
                self.jobList();
            }
            else{
                dataHelper.dealError(data);
            }
        });
    }
}

function getRenderDom(data, length) {
    var url = data.URL;
    var name = data.NAME;
    var id = data.JOB_ID;
    var state = data.STATE;

    var dom = '<div class="gal-item col col'+length+'">' +
        '<a href="'+url+'" class="img-shadow">' +
        '</div>' +
        '<div class="title">'+name+'</div>' +
        '</div>' +
        '</a>' +
        '<button class="job-change"  data-Id="'+id+'" >'+stateString[state]+'</button>'+
        '<button class="job-delete"  data-Id="'+id+'" >'+"刪除"+'</button>'+
        '</div>';

    return dom;
}