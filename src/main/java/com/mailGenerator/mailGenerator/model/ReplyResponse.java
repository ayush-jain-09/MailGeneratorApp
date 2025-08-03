package com.mailGenerator.mailGenerator.model;



/**
 * A data transfer object (DTO) for the outgoing response containing the generated email reply.
 * This class holds the generated reply text.
 */
public class ReplyResponse {
    private String reply;

    // Default constructor for Jackson
    public ReplyResponse() {
    }

    public ReplyResponse(String reply) {
        this.reply = reply;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
