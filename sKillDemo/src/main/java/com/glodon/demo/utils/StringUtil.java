package com.glodon.demo.utils;

/**
 * @Author huangs-e
 * @Date 2020/8/8 17:19
 * @Version 1.0
 */
public class StringUtil {

        public static boolean isNumber(String str) {

        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");

        }

        }
