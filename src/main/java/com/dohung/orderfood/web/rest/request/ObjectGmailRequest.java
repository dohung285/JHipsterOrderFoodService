package com.dohung.orderfood.web.rest.request;

public class ObjectGmailRequest {

    private String mailTo;
    private String fileName;
    private String subject;
    private String text;

    public ObjectGmailRequest() {}

    public ObjectGmailRequest(String mailTo, String fileName, String subject, String text) {
        this.mailTo = mailTo;
        this.fileName = fileName;
        this.subject = subject;
        this.text = text;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return (
            "ObjectGmailRequest{" +
            "mailTo='" +
            mailTo +
            '\'' +
            ", fileName='" +
            fileName +
            '\'' +
            ", subject='" +
            subject +
            '\'' +
            ", text='" +
            text +
            '\'' +
            '}'
        );
    }
}
