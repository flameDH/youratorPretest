var baseUrl = 'http://127.0.0.1:9000/';

var api = {
    indexCompany:       baseUrl + 'company/indexList',
    indexJob:           baseUrl + 'job/indexList',

    companyDetail:      baseUrl + 'company/detail/',
    companyList:        baseUrl + 'company/list',

    jobDetail:          baseUrl + 'job/detail/',
    jobList:            baseUrl + 'job/allList',
    jobCompanyList:     baseUrl + 'job/companyList/',

    adminJob:          baseUrl + 'job/adminList/',
    changeState:       baseUrl + 'job/close',
    deleteJob:         baseUrl + 'job/delete',
    createJob:         baseUrl + 'job/create',
    updateJob:         baseUrl + 'job/update',

    updateCompany:     baseUrl + 'company/update',     
}