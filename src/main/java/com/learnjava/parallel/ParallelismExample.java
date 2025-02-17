package com.learnjava.parallel;

import java.util.List;
import java.util.stream.Collectors;

public class ParallelismExample {

    public static void main(String[] args) {
        List<String> namesList = List.of("Bob", "Jamie", "Jill", "Rick");
        System.out.println("namelist: " + namesList);
        List<String> namesListUpperCase = namesList
//                .stream()
//                .parallel()
                .parallelStream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println("nameListUpperCase: " +  namesListUpperCase);
    }
}
