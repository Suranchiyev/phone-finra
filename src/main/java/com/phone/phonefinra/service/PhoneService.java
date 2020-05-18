package com.phone.phonefinra.service;

import com.phone.phonefinra.beans.GeneratedStatus;
import com.phone.phonefinra.beans.Phone;
import com.phone.phonefinra.util.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PhoneService {
    private Set<Phone> allPhones = new HashSet<>();

    public static final String phoneFilesInS3 = "phones";

    private void permute(int[] a, int k) {
        if (k == a.length) {
            List<Byte> number = new ArrayList<>();

            for (int i = 0; i < a.length; i++) {
                number.add((byte)a[i]);
            }

            Phone phone = new Phone(number);
            allPhones.add(phone);
        } else {
            for (int i = k; i < a.length; i++) {
                int temp = a[k];
                a[k] = a[i];
                a[i] = temp;

                permute(a, k + 1);

                temp = a[k];
                a[k] = a[i];
                a[i] = temp;
            }
        }
    }

    public Set<Phone> generatePhones(String phone){
        int[] phoneArr = new int[10];
        for(int i = 0; i < phone.length(); i++) {
            phoneArr[i] = Integer.parseInt( String.valueOf(phone.charAt(i)));
        }

        permute(phoneArr, 0);
        return allPhones;
    }

    public GeneratedStatus uploadToS3(String phone, Set<Phone> allPhones, String fileId) {
        File tmpFile = new File(System.getProperty("user.home")+"/" + fileId + ".txt");
        FileUtils.writeAllPhones(allPhones, tmpFile);
        S3Service.putFileToS3(tmpFile, phoneFilesInS3+"/"+tmpFile.getName());

        GeneratedStatus gStatus = new GeneratedStatus();
        gStatus.setGeneratedDate();
        gStatus.setNumberPhonesGenerated(allPhones.size());
        gStatus.setPhoneNumber(phone);
        gStatus.makeFileUrl(tmpFile.getName());

        tmpFile.delete();
        return gStatus;
    }

    public List<GeneratedStatus> getAllGeneratedNumbers() {
        try {
            List<GeneratedStatus> sList = new ArrayList<>();
            File tmpStatus = new File(System.getProperty("user.home")+"/status.txt");
            S3Service.getFileFromS3("status/statusFile.txt", tmpStatus);
            List<String> statusList = FileUtils.getFileContent(tmpStatus.getAbsolutePath());
            statusList.forEach(line -> sList.add(getGStatus(line)));

            tmpStatus.delete();
            return sList;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addGeneratedStatus(GeneratedStatus gStatus) {
        try {
            File tmpStatus = new File(System.getProperty("user.home")+"/status-in.txt");

            S3Service.getFileFromS3("status/statusFile.txt", tmpStatus);

            List<String> statusList = FileUtils.getFileContent(tmpStatus.getAbsolutePath());
            statusList.add(getLine(gStatus));

            File tmpStatusOut = new File(System.getProperty("user.home")+"/status-out.txt");
            FileUtils.writeContent(statusList, tmpStatusOut);
            S3Service.putFileToS3(tmpStatusOut, "status/statusFile.txt");

            tmpStatus.delete();
            tmpStatusOut.delete();
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private GeneratedStatus getGStatus(String line) {
        String[] lArr = line.split("\\|");
        GeneratedStatus gStaus = new GeneratedStatus();
        gStaus.setPhoneNumber(lArr[0].trim());
        gStaus.setGeneratedDate(lArr[1].trim());
        gStaus.setNumberPhonesGenerated(Long.parseLong(lArr[2].trim()));
        gStaus.setFileUrlInS3(lArr[3].trim());
        return  gStaus;
    }

    private String getLine(GeneratedStatus gStatus) {
        return gStatus.getPhoneNumber() + "|" + gStatus.getGeneratedDate() + "|" + gStatus.getNumberPhonesGenerated() + "|" +gStatus.getFileUrlInS3();
    }

}
