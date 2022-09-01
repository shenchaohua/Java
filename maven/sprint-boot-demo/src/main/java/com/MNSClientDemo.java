package com;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.Message;

public class MNSClientDemo {
    public static String ACCESSID = "LTAI5tSGBATws52aW8Bn81ap";
    public static String ACCESSKEY = "OvMdmvymjRRPlgu6mgI155jl6HT9Mp";
    public static String MNSENDPOIMT = "https://1146694229543810.mns.cn-shenzhen.aliyuncs.com/";

    public static void consumerClient() {
        CloudAccount account = new CloudAccount(
                ACCESSID,ACCESSKEY,MNSENDPOIMT);
        MNSClient client = account.getMNSClient();

        try{
            CloudQueue queue = client.getQueueRef("charging-queue-uat");
            for (int i = 0; i < 10; i++)
            {
                Message popMsg = queue.popMessage();
                if (popMsg != null){
                    System.out.println("message handle: " + popMsg.getReceiptHandle());
                    System.out.println("message body: " + popMsg.getMessageBodyAsString());
                    System.out.println("message id: " + popMsg.getMessageId());
                    System.out.println("message dequeue count:" + popMsg.getDequeueCount());

                    queue.deleteMessage(popMsg.getReceiptHandle());
                    System.out.println("delete message successfully.\n");
                }
            }
        } catch (ClientException ce)
        {
            System.out.println("Something wrong with the network connection between client and MNS service."
                    + "Please check your network and DNS availability.");
            ce.printStackTrace();
        } catch (ServiceException se)
        {
            if (se.getErrorCode().equals("QueueNotExist"))
            {
                System.out.println("Queue is not exist.Please create queue before use");
            } else if (se.getErrorCode().equals("TimeExpired"))
            {
                System.out.println("The request is time expired. Please check your local machine timeclock");
            }

            se.printStackTrace();
        } catch (Exception e)
        {
            System.out.println("Unknown exception happened!");
            e.printStackTrace();
        }

        client.close();
    }

    public static void producerClient() {
        CloudAccount account = new CloudAccount(
                ACCESSID, ACCESSKEY, MNSENDPOIMT);
        MNSClient client = account.getMNSClient();
        try{
            CloudQueue queue = client.getQueueRef("charging-queue-preprod");
            Message message = new Message();
            String msg = "{\"msg\":" +
                    "{\"msgContent\":{\"appToken\":\"066306492fb441efa641ea867e3a9f2d\"," +
                    "\"chargeConfigUuid\":\"42a286ea-b6ab-463d-91f0-c515137f0db4\"," +
                    "\"endTime\":\"2022-08-31 00:00:00\",\"houseUuid\":\"4a03d447-f4ad-48d7-925c-87918e6d8d0e\"," +
                    "\"startTime\":\"2022-08-02 00:00:00\",\"updateOrder\":true,\"userToken\":\"\"}," +
                    "\"msgSource\":\"chargingWithDay\",\"msgType\":\"POST\"}}";

            String ms2 = "{\"msg\":{\"msgContent\":{\"orderItemList\":" +
                    "[{\"taxPoint\":10,\"itemId\":\"60b8d16e-10ed-4a84-95e5-c9d4438efb1a\"," +
                    "\"penaltyDerate\":0,\"penaltyConfig\":{\"penaltyPoint\":10,\"penaltyFormName\":\"0\"," +
                    "\"chargeRuleDec\":2,\"chargeRuleRate\":\"1\",\"penaltyCycle\":\"0\"," +
                    "\"penaltyMonth\":[{\"month\":\"1\",\"multiple\":0,\"checked\":false}," +
                    "{\"month\":\"2\",\"multiple\":0,\"checked\":false},{\"month\":\"3\"," +
                    "\"multiple\":0,\"checked\":false},{\"month\":\"4\",\"multiple\":0," +
                    "\"checked\":false},{\"month\":\"5\",\"multiple\":0,\"checked\":false}," +
                    "{\"month\":\"6\",\"multiple\":0,\"checked\":false},{\"month\":\"7\"," +
                    "\"multiple\":0,\"checked\":false},{\"month\":\"8\",\"multiple\":0,\"checked\":false}," +
                    "{\"month\":\"9\",\"multiple\":0,\"checked\":false},{\"month\":\"10\",\"multiple\":0," +
                    "\"checked\":false},{\"month\":\"11\",\"multiple\":0,\"checked\":false}," +
                    "{\"month\":\"12\",\"multiple\":0,\"checked\":false}]},\"taxDec\":2," +
                    "\"receivableAmount\":1260,\"penaltyActualAmount\":0,\"belongYears\":\"2022-08\"," +
                    "\"discountMoney\":0,\"receivableTime\":\"2022-08-01 23:59:59\",\"reductionsMoney\":0," +
                    "\"carryForwardMoney\":0}],\"appToken\":\"434c1002fc044fc88660366d3d739360\"," +
                    "\"houseUuid\":\"6e17fab3-92a2-466a-bffe-c5308da920c9\",\"mqMessageId\":138513329}," +
                    "\"msgType\":\"POST\",\"msgSource\":\"penalty\"},\"mqMessageId\":138513329}";
//            JsonObject jsonObject = new JsonParser().parse(msg).getAsJsonObject();
            message.setMessageBody(msg);


            Message putMsg = queue.putMessage(message);
            System.out.println("Send message id is: " + putMsg.getMessageId());

        } catch (ClientException ce)
        {
            System.out.println("Something wrong with the network connection between client and MNS service."
                    + "Please check your network and DNS availability.");
            ce.printStackTrace();
        } catch (ServiceException se)
        {
            if (se.getErrorCode().equals("QueueNotExist"))
            {
                System.out.println("Queue is not exist.Please create before use");
            } else if (se.getErrorCode().equals("TimeExpired"))
            {
                System.out.println("The request is time expired. Please check your local machine timeclock");
            }
            se.printStackTrace();
        } catch (Exception e)
        {
            System.out.println("Unknown exception happened!");
            e.printStackTrace();
        }

        client.close();
    }

    public static void main(String[] args) {
        producerClient();
//        consumerClient();
    }
}