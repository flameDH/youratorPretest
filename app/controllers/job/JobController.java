package controllers.job;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import enums.ErrorCode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.JSONNodeHelper;
import util.ResponseHelper;

public class JobController extends Controller {
	private JobModel jobModel = new JobModel();
	private JobHandler jobHandler = new JobHandler();
	//精選職缺
	public Result indexList() {
		try{
			JSONArray data = jobModel.list(-1, false, true);
			ObjectNode rpn =  Json.newObject();
			rpn.set("list", jobHandler.processJob(data));
			return ok(ResponseHelper.genOKResult(rpn));
		}
		catch(Exception e) {
			return ok(ResponseHelper.genErrorResult(ErrorCode.SYS_ERR));
		}
	}
	
	//不分公司職缺列表
	public Result allList() {
		try{
			JSONArray data = jobModel.list(-1, false, false);
			ObjectNode rpn =  Json.newObject();
			rpn.set("list", jobHandler.processJob(data));
			return ok(ResponseHelper.genOKResult(rpn));
		}
		catch(Exception e) {
			return ok(ResponseHelper.genErrorResult(ErrorCode.SYS_ERR));
		}
	}
	
	//職缺列表
	public Result list(int companyId) {
		try {
			JSONArray data = jobModel.list(companyId, false,false);
			ObjectNode rpn =  Json.newObject();
			rpn.set("list", jobHandler.processJob(data));
			return ok(ResponseHelper.genOKResult(rpn));
		}
		catch(Exception e) {
			return ok(ResponseHelper.genErrorResult(ErrorCode.SYS_ERR));
		}
	}
	
	//公司後台職缺列表
	public Result adminList(int companyId) {
		try {
			JSONArray data = jobModel.list(companyId, true,false);
			ObjectNode rpn =  Json.newObject();
			rpn.set("list", jobHandler.processJob(data));
			return ok(ResponseHelper.genOKResult(rpn));
		}
		catch(Exception e) {
			return ok(ResponseHelper.genErrorResult(ErrorCode.SYS_ERR));
		}
	}

	//職缺詳細
	public Result Detail(int jobId) {
		try {
			JSONObject data = jobModel.detail(jobId);
			ObjectNode rpn = jobHandler.processDetail(data);
			return ok(ResponseHelper.genOKResult(rpn));
		}
		catch(Exception e) {
			return ok(ResponseHelper.genErrorResult(ErrorCode.SYS_ERR));
		}
	}
	
	//新增資料
	public Result create() {
		JsonNode json = request().body().asJson();
		try {
			JobObject param = new JobObject(json);
			System.out.println(param.benefit);
			jobModel.create(param);
			return ok(ResponseHelper.genOKResult());
		}
		catch(Exception e) {
			System.out.println(e);
			return ok(ResponseHelper.genErrorResult(ErrorCode.SYS_ERR));
		}
	}

	//修改資料
	public Result update() {
		JsonNode json = request().body().asJson();
		try {
			JobObject param = new JobObject(json);
			jobModel.update(param);
			return ok(ResponseHelper.genOKResult());
		}
		catch(Exception e) {
			return ok(ResponseHelper.genErrorResult(ErrorCode.SYS_ERR));
		}
	}
	
	//刪除資料
	public Result delete() {
		JsonNode json = request().body().asJson();
		try {
			int id = json.get("JOB_ID").asInt();
			
			jobModel.delete(id);
			
			return ok(ResponseHelper.genOKResult());
		}
		catch(Exception e) {
			return ok(ResponseHelper.genErrorResult(ErrorCode.SYS_ERR));
		}
	}
	
	/**
	 * 關閉職缺
	 * @return
	 */
	public Result close() {
		JsonNode json = request().body().asJson();
		try {
			int id = json.get("JOB_ID").asInt();
			//int state = json.get("STATE").asInt();
			
			jobModel.is_open(id, 1);
			
			return ok(ResponseHelper.genOKResult());
		}
		catch(Exception e) {
			return ok(ResponseHelper.genErrorResult(ErrorCode.SYS_ERR));
		}
	}

}
