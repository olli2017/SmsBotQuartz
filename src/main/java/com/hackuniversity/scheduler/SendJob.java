package com.hackuniversity.scheduler;

import com.hackuniversity.parser.Parser;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import ru.dezhik.sms.sender.SenderService;
import ru.dezhik.sms.sender.SenderServiceConfiguration;
import ru.dezhik.sms.sender.SenderServiceConfigurationBuilder;
import ru.dezhik.sms.sender.api.InvocationStatus;
import ru.dezhik.sms.sender.api.smsru.SMSRuResponseStatus;
import ru.dezhik.sms.sender.api.smsru.send.SMSRuSendRequest;
import ru.dezhik.sms.sender.api.smsru.send.SMSRuSendResponse;

import java.io.IOException;
import java.util.Arrays;

public class SendJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String jobSays = dataMap.getString("jobSays");
        String numbers = dataMap.getString("numbersString");
        Parser parser = new Parser();

        SenderServiceConfiguration config = SenderServiceConfigurationBuilder.create()
                .setApiId("E153FE51-B4E2-7B9E-2EC7-E3DD557D9EA1")
//        .setReturnPlainResponse(true)
                .setTestSendingEnabled(true)
                .build();

        SenderService sender = new SenderService(config);
        SMSRuSendRequest sendRequest = new SMSRuSendRequest();
        sendRequest.setReceivers(parser.parseNumbers(numbers));
        sendRequest.setText(jobSays);
        SMSRuSendResponse sendResponse = sender.execute(sendRequest);
        if (sendRequest.getStatus() == InvocationStatus.SUCCESS) {
            if (sendResponse.getResponseStatus() == SMSRuResponseStatus.IN_QUEUE) {
                System.out.println(String.format("Success. Balance %4.2f, smsIds %s",
                        sendResponse.getBalance(), Arrays.toString(sendResponse.getMsgIds().toArray())));
            } else {
                System.out.println(String.format("Failed with status %s", sendResponse.getResponseStatus()));
            }
        } else {
            if (sendRequest.getStatus().isAbnormal()) {
                sendRequest.getException().printStackTrace();
            }
        }

        try {
            sender.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
