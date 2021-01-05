package com.netty;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/22 13:53
 */
public class CommonTest {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        set.add("1");
        set.add("2");
        set.add("3");
        set.add("4");
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            if ("1".equals(iterator.next())) {
                iterator.remove();
            }
        }
        set.stream().forEach(a -> System.out.println(a));
    }
}
