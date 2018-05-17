package com.serverless;
import java.io.IOException;
import java.util.*;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Handler implements RequestHandler<Map<String, Object>, String> {

    private static final String QUEUE_NAME = "lspan-aws";

    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDB dynamoDB = new DynamoDB(client);
    static String tableName = "Lspan-aws-Temp";

    @Override
    public String handleRequest(Map<String, Object> input, Context context) {

        SQSConnector sqsConnector = new SQSConnector(QUEUE_NAME);

        //LinkedHashMap<String,Mail.class> json = new LinkedHashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        String hoge = "";
        try {
            Mail mail =  mapper.readValue(sqsConnector.sendEmail(),Mail.class);
            for (String s: mail.getTo()) {
                hoge +=  s + ",";
            }
            return "test : " + hoge;
        } catch (IOException e) {
            e.printStackTrace();
            return "test NG";
        }

//        try {
//            Mail mail = mapper.readValue(sqsConnector.sendEmail(), Mail.class);
//            return "test: " + "mail.getTo()[0], " + mail.getTo()[0] + "mail.getTo()[1], " + mail.getTo()[1];
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "test NG";
//        }
    }

        //  if(System.getenv("MODE")=="send") {
      //  }
      //  else if(System.getenv("MODE")=="rec"){
      //      sqsConnector.sendQueue();
      //      return "OK! - SendQueue";
      //  }

      //  return "NG!" + System.getenv("MODE");



    private static String createItems() {
        Table table = dynamoDB.getTable(tableName);
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("acount_id = :id and temp_count > :a")
                .withValueMap(new ValueMap()
                        .withString(":id", "okuno")
                        .withInt(":a", 1));
        ItemCollection<QueryOutcome> items = table.query(spec);
        return items.toString();
    }
//        Iterator<Item> iterator = items.iterator();
//        Item item = null;
//        while (iterator.hasNext()) {
//            item = iterator.next();
//            System.out.println(item.toJSONPretty());
//        }

//        Item item = new Item().withPrimaryKey("account_id", "okuno")
//                    .withString("Mail", "Nishi");
//        table.putItem(item);

}