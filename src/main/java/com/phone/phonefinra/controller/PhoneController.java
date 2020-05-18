package com.phone.phonefinra.controller;

import com.phone.phonefinra.beans.GeneratedStatus;
import com.phone.phonefinra.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@RestController
public class PhoneController {
    @Autowired
    PhoneService phoneServer;

    @CrossOrigin
    @GetMapping("generate")
    public @ResponseBody
    GeneratedStatus generatePhoneNumbers(@RequestParam("phoneNumber")String phoneNumber) {

        phoneNumber = phoneNumber.replace("(", "");
        phoneNumber = phoneNumber.replace(")", "");
        phoneNumber = phoneNumber.replace("-", "");

        System.out.println("PHONE: " + phoneNumber);
        if(phoneNumber.length() != 10 || !isOnlyNumbers(phoneNumber)) {
            throw new RuntimeException("Wrong phone number input");
        }

        PhoneService phoneServer = new PhoneService();
        String id = getCurrentTimestamp();
        GeneratedStatus gStatus = phoneServer.uploadToS3(phoneNumber, phoneServer.generatePhones(phoneNumber), id);
        phoneServer.addGeneratedStatus(gStatus);

        return gStatus;
    }

    private boolean isOnlyNumbers(String str) {
        try{
            Long.parseLong(str);
        }catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    @CrossOrigin
    @GetMapping("getAllGenerated")
    public @ResponseBody List<GeneratedStatus> getAllGenerated() {
        List<GeneratedStatus> gList = phoneServer.getAllGeneratedNumbers();
        Collections.reverse(gList);
        return gList;
    }

    private static String getCurrentTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return String.valueOf(timestamp.getTime());
    }
}
