package com.example.rxjavaandretrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main (String[] args) {
        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        list.add("test3");
        list.add("test4");
        list.add("test5");
        list.add("test6");
        list.add("test7");
        System.out.println("lsit 大小 >>"+list.size());

        Map<Object,Object> map = new HashMap<>();
        map.put(null,"test1");
        map.put(1,"test2");
        map.put(2,"test3");
        map.put(3,null);
        map.put(1,"test5");

        System.out.println("map 大小 >>"+map.size());
        Iterator<Map.Entry<Object, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            System.out.println("key >>"+entry.getKey());
            System.out.println("value >>"+entry.getValue());
        }
    }
}
