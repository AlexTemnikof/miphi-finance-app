package ru.rfma.auth.utils;

import java.util.Random;
public class VerificationCode {
     public static int createCode(){
        Random rn = new Random();
        int max = 9999;
        int min = 1000;
        int code = min + rn.nextInt(max - min + 1);
        return code;
    }
}
