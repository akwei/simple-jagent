//package com.github.akwei.simple.jagent;
//
//import net.bytebuddy.implementation.bind.annotation.SuperCall;
//import net.bytebuddy.implementation.bind.annotation.This;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.Callable;
//
//public class LoggerInterceptor {
//
//    public static List<String> log(@SuperCall Callable<List<String>> zuper, @This Object obj)
//            throws Exception {
//        System.out.println("Calling database ");
//        System.out.println("super: " + zuper.getClass().getName());
//        System.out.println("this: " + obj.getClass().getName());
//        try {
//            return zuper.call();
////            return new ArrayList<>();
//        } finally {
//            System.out.println("Returned from database");
//        }
//    }
//}
