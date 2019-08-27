import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import javax.inject.Inject;

import akka.stream.Materializer;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;
import util.LoggerUtil;

public class RequestRecordFilter extends Filter {
	Materializer mat;

	@Inject
	public RequestRecordFilter(Materializer mat) {
		super(mat);
		this.mat = mat;
	}

	@Override
	public CompletionStage<Result> apply(Function<Http.RequestHeader, CompletionStage<Result>> next,
			Http.RequestHeader rh) {

		long startTime = System.currentTimeMillis();
		// LoggerUtil.logDebug("[RequestRecordFilter] request url=" + rh.uri() +
		// ", startTime=" + startTime);
		return next.apply(rh).thenApply(result -> {
			long endTime = System.currentTimeMillis();
			long requestTime = endTime - startTime;
			String uri = rh.uri();
			if (uri.startsWith("/1.0/") && !uri.startsWith("/1.0/file/")) {
				LoggerUtil.logDebug("[RequestRecordFilter][status]" + result.status() + "[startTime]" + startTime
						+ "[requestTime]" + requestTime + "[ip]" + rh.remoteAddress() + "[url]" + uri);
			}
			// else {
			// LoggerUtil.logDebug("[RequestRecordFilter][status]" +
			// result.status() + "[startTime]" + startTime
			// + "[requestTime]" + requestTime + "[ip]" + rh.remoteAddress());
			// }

			return result;
		});
	}

}
