package com.serverless;

import java.util.List;

public class Mail {
    //jsonのkey名をあわせる
    private String[] to ;
    private String subject;
    private String body;

    public String[] getTo(){ return this.to; }
    public String getSubject(){ return this.subject; }
    public String getBody(){ return this.body; }
    //setterはmapperがしてくれるからいらないかな？
}

