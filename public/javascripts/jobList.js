$(function () {
    jobList.init();
});

var jobList ={

    init:function(){
        var self = this;
        self.list();
    },

    list:function(){
        
        var apiUrl=api.jobList;

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
                $('#job-List').html(renderHtml);
            }else{
                dataHelper.dealError(data);
            }
        });

    }
}

function getRenderDom(data, length) {
    var url = data.URL;
    var imgUrl = data.IMG;
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
