import org.json.JSONException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import play.Application;
import util.LoggerUtil;

@Singleton
public class StartUpTasks {
	@Inject
	public StartUpTasks(Application app) throws JSONException {
		LoggerUtil.logDebug("======ServerStarted=======");
	}

}
