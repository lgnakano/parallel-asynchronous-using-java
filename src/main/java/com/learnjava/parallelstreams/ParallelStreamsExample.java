package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ParallelStreamsExample {

    public static List<String> stringTransform(List<String> namesList) {
        return namesList
                .stream()
                .parallel()
                .map(ParallelStreamsExample::addNameLengthTransform)
                .collect(Collectors.toList());
    }
    public static void main(String[] args) {
        stopWatch.start();
        List<String> namesList = DataSet.namesList();
        List<String> resultList = ParallelStreamsExample.stringTransform(namesList);
        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        log("Final Result : "+ resultList);

    }
    private static String addNameLengthTransform(String name) {
        delay(500);
        return name.length()+" - "+name ;
    }
}
