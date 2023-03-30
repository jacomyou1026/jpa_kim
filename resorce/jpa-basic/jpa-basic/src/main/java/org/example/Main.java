package org.example;

import hellojpa.Address;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Address address1= new Address("city", "street", "10000");
        Address address2= new Address("city", "street", "10000");
        //값타입의 비교는 항상 equals
        System.out.println("address2.equals(address1) = " + address2.equals(address1));



    }




}