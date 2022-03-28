package com.learnjava.parallelstreams;

import java.util.List;
import java.util.stream.Collectors;

public class AssignmentParallelStreamsTransform {

    public static void main(String[] args) {
        List<String> namesList = List.of("Bob", "Jamie", "Jill", "Rick");
        System.out.println("namelist: " + namesList);
        List<String> namesListLowerCase = stringTransform_lowerCase(namesList);
        System.out.println("nameListLowerCase: " +  namesListLowerCase);
    }

    private static List<String> stringTransform_lowerCase(List<String> namesList) {
        return namesList
//                .stream()
//                .parallel()
                .parallelStream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
