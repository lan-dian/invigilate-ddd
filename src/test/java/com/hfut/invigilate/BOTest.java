package com.hfut.invigilate;

public class BOTest {


    public static void main(String[] args) {
        String s = "2021年07月19日 10:20~12:20";
        int index = s.indexOf("日");
        System.out.println(s.substring(0,index));
    }

}
