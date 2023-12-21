package com.scand.bookshop.jobs;

import com.scand.bookshop.service.StatsService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Job;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

@Slf4j
public class UpdateStatsJob implements Job {

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    try {
      ApplicationContext applicationContext = (ApplicationContext) context.getScheduler()
          .getContext()
          .get("applicationContext");
      StatsService statsService = applicationContext.getBean(StatsService.class);
      statsService.createStats();
    } catch (SchedulerException e) {
      log.error("Error accessing Spring ApplicationContext", e);
      throw new JobExecutionException(e);
    }
  }
}
