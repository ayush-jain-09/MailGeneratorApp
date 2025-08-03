package com.mailGenerator.mailGenerator.model;



/**
 * A data transfer object (DTO) for the incoming request to generate an email reply.
 * This class encapsulates the body of an email and the desired tone for the reply.
 */
public class ReplyRequest {
    private String body;
    private String tone;

    // Default constructor for Jackson
    public ReplyRequest() {
    }

    public ReplyRequest(String body, String tone) {
        this.body = body;
        this.tone = tone;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }
}

