package com.cucharitas.philae.configuration

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory

import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.core.step.tasklet.Tasklet

import org.springframework.core.task.TaskExecutor
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.core.partition.support.MultiResourcePartitioner
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler
import org.springframework.batch.core.partition.PartitionHandler

import org.springframework.core.io.support.ResourcePatternResolver
import org.springframework.core.io.Resource

import org.apache.activemq.spring.ActiveMQConnectionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportResource

import javax.jms.ConnectionFactory

import com.cucharitas.philae.pojo.PhilaeData

@Configuration
@Import(AppConfiguration.class)
@ImportResource(
    "META-INF/spring/philae-spring-integration-master.xml")
    //"META-INF/spring/philae-spring-integration-slave.xml"})
@ComponentScan("com.cucharitas.philae")
public class JobDefinitionConfiguration{

    @Autowired
    private JobBuilderFactory jobs

    @Autowired
    private StepBuilderFactory steps

    @Autowired
    private ResourcePatternResolver resourcePatternResolver

    @Autowired
    private ItemReader<Integer> reader

    @Autowired
    private ItemProcessor<Integer, PhilaeData> processor

    @Autowired
    private ItemWriter<PhilaeData> writer

    @Autowired
    private TaskExecutor taskExecutor

    Job philaeJob(Step unzipStep){
        jobs.get("philaeJob")
        .start(unzipStep)
        .build()
    }

    Step unzipStep(Tasklet unzipFilesTasklet){
        steps.get("unzipFiles")
        .tasklet(unzipFilesTasklet)
        .next(readWriteFilesStep)
        .build()
    }

    Step partitionerStep(Step readWriteFilesStep){
        steps.get("masterStep")
        .partition(readWriteFilesStep)
        .partition("readWriteFiles", partitioner())
        .end()
        .build()
    }

    Step readWriteFilesStep() {
        steps.get("readWriteFiles")
        .<PhilaeData, PhilaeData>chunk(10)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build()
    }

    @Bean
    Partitioner partitioner(){
        MultiResourcePartitioner partitioner = new MultiResourcePartitioner()
        Resource[] resources

        try {
            resources = resourcePatternResolver.getResources("classpath:tmpFiles/*.output")
        } catch (IOException e) {
            throw new RuntimeException("I/O problems when resolving the input file pattern.",e)
        }

       partitioner.setResources(resources)

        partitioner
    }

    @Bean
    PartitionHandler partitionHandler() {
        TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler()

        partitionHandler.setGridSize(4)
        partitionHandler.setTaskExecutor(taskExecutor)
        partitionHandler.setStep(readWriteFilesStep())

        partitionHandler
    }

    @Bean
    ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory()

        factory
    }
}
