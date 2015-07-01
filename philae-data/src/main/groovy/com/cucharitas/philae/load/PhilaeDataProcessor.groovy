package com.cucharitas.philae.load

import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.core.configuration.annotation.StepScope

import org.springframework.stereotype.Component
import com.cucharitas.philae.pojo.PhilaeData

@Component("processor")
@StepScope
public class PhilaeDataProcessor implements ItemProcessor<Integer, PhilaeData>{

    public PhilaeData process(Integer item){
        new PhilaeData(data:item)
    }

}
