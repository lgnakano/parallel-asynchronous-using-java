package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ParallelStreamsExample {

    public List<String> stringTransform(List<String> namesList) {
        return namesList
                .stream()
                .parallel()
                .map(this::addNameLengthTransform)
                .collect(Collectors.toList());
    }
    public static void main(String[] args) {
        ParallelStreamsExample parallelStreamsExample = new ParallelStreamsExample();
        stopWatch.start();
        List<String> namesList = DataSet.namesList();
        List<String> resultList = parallelStreamsExample.stringTransform(namesList);
        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        log("Final Result : "+ resultList);

    }
    public String addNameLengthTransform(String name) {
        delay(500);
        return name.length()+" - "+name ;
    }
}
