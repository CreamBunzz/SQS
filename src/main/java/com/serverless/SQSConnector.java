package com.serverless;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;
import com.amazonaws.services.sqs.buffered.QueueBufferConfig;
import com.amazonaws.services.sqs.model.*;

import java.util.*;

public class SQSConnector {
    public final AmazonSQS sqs;
    public final String queueUrl;
    public int sendLimit = 30;
    public String temp_Mess;

    public SQSConnector(String queueName) {
        sqs = AmazonSQSClientBuilder.defaultClient();
        this.queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
    }

    public void sendQueue() {
        SendMessageBatchRequest send_batch_request = new SendMessageBatchRequest().withQueueUrl(queueUrl);
        for(int i = 1;i <= 10;i++){
            send_batch_request.withEntries(new SendMessageBatchRequestEntry("msg_" + i, "message; " + i));
        }
        sqs.sendMessageBatch(send_batch_request);
    }

    //メール送るMainのところ
    public String sendEmail() {
        double queuesCnt = findApproximateQueues();
        if (this.sendLimit > 0 && queuesCnt > 0) {

            ReceiveMessageResult resultQueues = getQueues(queuesCnt);
            if (resultQueues != null) {
                //メール送信
                List<Message> messages = resultQueues.getMessages();
                //DB書き込み

                this.sendLimit = this.sendLimit - resultQueues.getMessages().size();

                //キュー削除(まだわかってない)
                //messages.forEach(sqs.deleteMessage(queueUrl, m.getReceiptHandle());
                for (Message m : messages) {
                    temp_Mess = m.getBody();
                    return temp_Mess;
                    //sqs.deleteMessage(queueUrl, m.getReceiptHandle());
                }
            }
            //this.sendEmail();
        }
        return  temp_Mess;
    }

    //いくつキューが入っているか
    private double findApproximateQueues() {
        //List<String> attributeList = new ArrayList<String>();
        //attributeList.add("ApproximateNumberOfMessages");
        //Map<String, String> attributesMap = sqs.getQueueAttributes(queueUrl, attributeList).getAttributes();
        //return Double.parseDouble(attributesMap.get("ApproximateNumberOfMessages"));

        List<String> attributeList = new ArrayList(Arrays.asList("ApproximateNumberOfMessages"));
        return Double.parseDouble(sqs.getQueueAttributes(queueUrl, attributeList).getAttributes().get("ApproximateNumberOfMessages"));
    }

    //キューから受信
    private ReceiveMessageResult getQueues(double queuesCnt) {
        ReceiveMessageRequest receive_request = new ReceiveMessageRequest()
                .withQueueUrl(queueUrl)
                .withWaitTimeSeconds(20)
                .withMaxNumberOfMessages(getMaxNumberOfMessages(queuesCnt));

        return sqs.receiveMessage(receive_request);
    }

    //キューから取ってくる数を決定
    private int getMaxNumberOfMessages(double queuesCnt) {
        if (sendLimit >= 10) return 10;
        else{
            if (sendLimit <= queuesCnt) {
                return sendLimit;
            } else if (sendLimit > queuesCnt) {
                return (int)queuesCnt;
            }
            return 0;
        }
    }
}