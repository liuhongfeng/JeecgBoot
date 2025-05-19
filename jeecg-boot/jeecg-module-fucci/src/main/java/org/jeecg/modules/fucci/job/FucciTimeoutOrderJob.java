package org.jeecg.modules.fucci.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.fucci.service.IFucciOrderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lhf
 * @date 2025-05-19
 * @describe 超时订单处理定时任务
 */
@Slf4j
@Service
public class FucciTimeoutOrderJob implements Job {

    @Autowired
    private IFucciOrderService fucciOrderService;

    /**
     * 若参数变量名修改 QuartzJobController 中也需对应修改
     */
    private String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Job Execution key：" + jobExecutionContext.getJobDetail().getKey());
        log.info(String.format("Welcome %s! Fucci 带参数定时任务 FucciOrderJob ! 时间:" + DateUtils.now(), this.parameter));
        // 支付超时订单处理
        fucciOrderService.payClose(null);
    }

}
