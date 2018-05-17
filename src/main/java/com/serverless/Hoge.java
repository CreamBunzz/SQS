package com.serverless;

public class Hoge {
    private String string = "";
    private String check(String test){
        if(test == "a"){
            string = "a";

        }else{
            string = "other";
        }

        return string;
    }
}
