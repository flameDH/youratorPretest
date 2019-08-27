package controllers.company;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import controllers.job.JobModel;
import enums.ErrorCode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.JSONNodeHelper;
import util.ResponseHelper;

public class CompanyController extends Controller {
	
	private CompanyModel company_model = new CompanyModel(); 
	private JobModel job_model = new JobModel();
	private CompanyHandler com_han = new CompanyHandler();
	
	
	//精選公司
	public Result randomList() {
		try {
			JSONArray list = company_model.companyList(true);
			ObjectNode rpn =  Json.newObject();
			rpn.set("list", com_han.processCom(list) );
			return ok(ResponseHelper.genOKResult(rpn));
		}
		catch(Exception e) {
			return ok(ResponseHelper.genErrorResult(ErrorCode.SYS_ERR));
		}
	} 
	
	//公司列表頁
	public Result companyList() {
		try {
			JSONArray list = company_model.companyList(false);
			ObjectNode rpn =  Json.newObject();
			rpn.set("list", com_han.processCom(list));
			return ok(ResponseHelper.genOKResult(rpn));
		}
		catch(Exception e) {
			System.out.println(e);
			return ok(ResponseHelper.genErrorResult(ErrorCode.SYS_ERR));
		}
	}
	
	//公司頁面 for user 
	public Result comapnyDetail(int id) {
		try {
			JSONObject data  =company_model.companyDetail(id);
			ObjectNode rtn = com_han.processDetail(data);
			
			return ok(ResponseHelper.genOKResult(rtn));
		}
		catch(Exception e) {
			return ok(ResponseHelper.genErrorResult(ErrorCode.SYS_ERR));
		}
	}
	
	//更新公司資訊
	public Result adminUpdate() {
		JsonNode json = request().body().asJson();
		try {
			System.out.println(json);
			CompanyObject param = new CompanyObject(json);
			company_model.update(param);
			
			return ok(ResponseHelper.genOKResult());
		}
		catch(Exception e) {
			System.out.println(e);
			return ok(ResponseHelper.genErrorResult(ErrorCode.SYS_ERR));
		}
	}
	
}
