package com.scand.bookshop.service;

import com.scand.bookshop.entity.Order;
import com.scand.bookshop.entity.SaleStat;
import com.scand.bookshop.jobs.UpdateStatsJob;
import com.scand.bookshop.repository.SaleStatRepository;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsService {

  private static final String GROUP_NAME = "UPDATE_STATS_SCHEDULER_GROUP";
  private static final String JOB_NAME = "UPDATE_STATS_SCHEDULER";

  private final OrderService orderService;
  private final SaleStatRepository saleStatRepository;
  private final Scheduler scheduler;

  @Value("${stats.update.cron}")
  private String cron;

  @PostConstruct
  public void init() {
    initJobs();
  }

  private void initJobs() {
    try {
      Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(JOB_NAME, GROUP_NAME));
      if (trigger != null) {
        String currentCron = ((CronTrigger) trigger).getCronExpression();

        if (!currentCron.equals(cron)) {
          JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobKey());
          Trigger newTrigger = buildTrigger(jobDetail, cron);
          scheduler.rescheduleJob(trigger.getKey(), newTrigger);
          log.debug("Update stats scheduler job updated successfully.");
        }
      } else {
        JobDetail jobDetail = JobBuilder.newJob(UpdateStatsJob.class)
            .withIdentity(JOB_NAME, GROUP_NAME)
            .withDescription("Scheduled Update Stats Job")
            .storeDurably()
            .build();
        trigger = buildTrigger(jobDetail, cron);
        scheduler.scheduleJob(jobDetail, trigger);
        log.debug("Update stats scheduler job created successfully.");
      }
    } catch (SchedulerException ex) {
      log.error("Error initializing update stats scheduler job", ex);
      throw new RuntimeException("Unable to initialize update stats scheduler job.");
    }
  }

  private Trigger buildTrigger(JobDetail jobDetail, String cronExpression) {
    return TriggerBuilder.newTrigger()
        .forJob(jobDetail)
        .withIdentity(JOB_NAME, GROUP_NAME)
        .withDescription("Scheduled Update Stats Trigger")
        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)
            .inTimeZone(TimeZone.getTimeZone("UTC"))
            .withMisfireHandlingInstructionDoNothing())
        .build();
  }

  @Transactional
  public void createStats() {
    LocalDate today = LocalDate.now();
    List<Order> orders = orderService.getPaidOrdersForDate(today);
    BigDecimal sumOfTotalPrices = orders.stream()
        .map(Order::getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    SaleStat stat = new SaleStat(null, orders.size(), sumOfTotalPrices.doubleValue(), today);
    saleStatRepository.save(stat);
  }

  public List<SaleStat> getOrdersForDays(int daysAmount) {
    LocalDate startDate = LocalDate.now().minusDays(daysAmount);
    return saleStatRepository.findStatsForLastDays(startDate);
  }
}
