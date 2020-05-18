package com.phone.phonefinra.beans;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GeneratedStatus {
    private String phoneNumber;
    private String generatedDate;
    private long numberPhonesGenerated;
    private String fileUrlInS3;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(String generatedDate) {
        this.generatedDate = generatedDate;
    }

    public void setGeneratedDate() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        String formattedString = date.format(formatter);

        this.generatedDate = formattedString;
    }

    public long getNumberPhonesGenerated() {
        return numberPhonesGenerated;
    }

    public void setNumberPhonesGenerated(long numberPhonesGenerated) {
        this.numberPhonesGenerated = numberPhonesGenerated;
    }

    public String getFileUrlInS3() {
        return fileUrlInS3;
    }

    public void setFileUrlInS3(String fileUrlInS3) {
        this.fileUrlInS3 = fileUrlInS3;
    }

    public void makeFileUrl(String fileId) {
        String url = "https://finra-phone.s3.amazonaws.com/phones/";
        this.fileUrlInS3 = url + fileId;
    }
}
