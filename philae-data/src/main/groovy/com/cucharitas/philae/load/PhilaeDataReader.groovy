package com.cucharitas.philae.load

import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component
import org.springframework.batch.core.configuration.annotation.StepScope

@Component("reader")
@StepScope
public class PhilaeDataReader implements ItemReader<Integer>{
    String filePath

    public Integer read(){
        File f = new File(filePath)

        Integer.parseInt(f.readLines())
    }

}
