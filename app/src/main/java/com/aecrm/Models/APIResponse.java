package com.aecrm.Models;

public class APIResponse {

    String Message;
    String MessageType;

    public APIResponse(){}
    public APIResponse(String Message, String MessageType){

        this.Message = Message;
        this.MessageType = MessageType;
    }

    public String getMessage() {
        return Message;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }
}



