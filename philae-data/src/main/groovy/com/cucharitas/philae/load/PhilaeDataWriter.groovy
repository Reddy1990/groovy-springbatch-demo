package com.cucharitas.philae.load

import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import org.springframework.batch.core.configuration.annotation.StepScope

import com.cucharitas.philae.pojo.PhilaeData

@Component("writer")
@StepScope
public class PhilaeDataWriter implements ItemWriter<PhilaeData>{

    public void write(List<PhilaeData> items){
        return null;
    }

}
