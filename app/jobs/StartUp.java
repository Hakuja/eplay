package jobs;

import play.Logger;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class StartUp extends Job {

	@Override
	public void doJob() throws Exception {
		Logger.info("=== StartUp#doJob ===");
	}
}
