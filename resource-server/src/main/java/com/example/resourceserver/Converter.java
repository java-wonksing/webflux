package com.example.resourceserver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Converter {
    /**
     * JSON 문자열을 Object로 변경
     * @param <T>
     * @param json
     * @param type
     * @return
     */
    public static <T> Mono<T> stringToJson(String json, Class<T> type){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return Mono.just(mapper.readValue(json, type));
        } catch (IOException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        }
    }

    /**
     * Object를 JSON 문자열로 변경
     * @param <T>
     * @param obj
     * @return
     */
    public static <T> Mono<String> objToString(T obj){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return Mono.just(mapper.writeValueAsString(obj));
        } catch (IOException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        }
    }

    /**
     * JSON 문자열을 Object로 변경
     * @param <T>
     * @param json
     * @param type
     * @return
     */
    public static <T> T stringToObject(String json, Class<T> type){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <T> T stringToObject(String json, TypeReference<T> type){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T ObjectToObject(Object obj, Class<T> type){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(obj, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T ObjectToObject(Object obj, TypeReference<T> type){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(obj, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * JSON 문자열을 Object로 변경
     * @param <T>
     * @param json
     * @param type
     * @return
     */
    public static <T> String objectToJsonString(T obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * DataBuffer 내용을 UTF8 문자열로 변경
     * @param dataBuffer
     * @return
     */
    public static String dataBufferToUTF8String (DataBuffer dataBuffer){
        InputStream is = dataBuffer.asInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];
        String str = null;

        try {
            while( (nRead = is.read(data)) != -1 ) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            str = new String(buffer.toByteArray(), "UTF-8");
            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Base64 > String decode
     * @param str
     * @return
     */
    public static String decodeBase64(String str) {
        return new String(Base64.getDecoder().decode(str));
    }

    /**
     * String encode > Base64
     * @param str
     * @return
     */
    public static String encodeBase64(String str) {
        return new String(Base64.getEncoder().encode(str.getBytes()));
    }

    /**
     * String > Md5 encode
     * @param str
     * @return
     */
    public static String md5Hash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());

            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return str;
        }
    }


    public static String SHA1withRSAHash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());

            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static String imgurlConverter(String url) {
        if(url == null) {
            return null;
        }
        String thumbnail = new String(url);
        thumbnail = thumbnail.replaceAll(".kr/renewal/", ".kr/thumb/")
                .replaceAll(".kr/src/", ".kr/thumb/");
        if(thumbnail.indexOf("cdn.spotvnow.co.kr") > -1) {
            if(!(thumbnail.endsWith("=w400") || thumbnail.endsWith("=w800"))) {
                thumbnail += "=w400";
            }
        }
        return thumbnail;
    }
}