package com.phone.phonefinra.util;


import com.phone.phonefinra.beans.Phone;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FileUtils {
    public static void copyFile(File origin, File copy) {
        try (
                InputStream in = new BufferedInputStream(
                        new FileInputStream(origin));
                OutputStream out = new BufferedOutputStream(
                        new FileOutputStream(copy))) {

            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }

        }catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException("Failed in copyFile()");
        }
    }

    public static void writeStream(InputStream inputStream, File outputFile) {
        try (
                InputStream in = inputStream;
                OutputStream out = new BufferedOutputStream(
                        new FileOutputStream(outputFile))) {

            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        }catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException("Failed in writeStream(): "+e.getMessage());
        }
    }

    public static void writeString(String content, File outputFile) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(content);
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Failed in writeMethod(): "+e.getMessage());
        }
    }

    public static void writeAllPhones(Set<Phone> content, File outputFile) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            content.forEach(line -> {
                try {
                    writer.write(formatPhoneNumber(line.getPhone()));
                    writer.newLine();
                }catch (IOException io) {
                    io.printStackTrace();
                    throw new RuntimeException(io);
                }
            });

        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Failed in writeMethod(): "+e.getMessage());
        }
    }

    private static String formatPhoneNumber(List<Byte> line) {
        String res = "";
        for(int i = 0; i < line.size(); i++) {
            if(i == 0) {
                res +=  "("+ line.get(i);
            }else if(i == 2) {
                res +=  line.get(i) + ")";
            }else if(i == 5) {
                res +=  line.get(i) + "-";
            }else {
                res +=  line.get(i);
            }
        }

        return res;
    }


    public static void deleteAll( File dir) {
        if(dir.delete()) {

        }else {
            File[] files = dir.listFiles();
            for(File file : files) {
                deleteAll(file);
            }
            dir.delete();
        }
    }

    public static void replaceFile(File newFile, File oldFile) {
        if(oldFile.exists()) {
            oldFile.delete();
        }

        try(
                InputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(newFile));
                OutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(oldFile))
        ){

            byte[] bytes = new byte[1024];
            int length;
            while ((length = bufferedInputStream.read(bytes)) > 0){
                bufferedOutputStream.write(bytes, 0, length);
                bufferedOutputStream.flush();
            }

        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<String> getFileContent(String path){
        List<String> content = new ArrayList<>();
        File file = new File(path);
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while((line = reader.readLine()) != null){
                content.add(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return content;
    }

    public static void writeContent(List<String> content, File outputFile) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            content.forEach(line -> {
                try {
                    writer.write(line);
                    writer.newLine();
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Failed in writeMethod(): "+e.getMessage());
        }
    }
}
