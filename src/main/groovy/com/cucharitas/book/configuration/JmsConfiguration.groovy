package com.cucharitas.book.configuration

import javax.jms.ConnectionFactory

import org.apache.activemq.broker.BrokerService
import org.apache.activemq.spring.ActiveMQConnectionFactory

import org.springframework.core.task.SimpleAsyncTaskExecutor

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.annotation.JmsListener
import org.springframework.jms.config.JmsListenerContainerFactory

import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler
import org.springframework.integration.dsl.IntegrationFlow

@Configuration
@Import(AppConfiguration.class)
@EnableJms
@ComponentScan("com.cucharitas.book.configuration")
public class JmsConfiguration{
    private static final String BROKER_URL =
        "vm://embedded?broker.persistent=false,useShutdownHook=false"

    @Bean
    JmsListenerContainerFactory <?> jmsContainerFactory ( connectionFactory)
    {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory())

        factory
    }

    @Bean
    ConnectionFactory connectionFactory(){
        new ActiveMQConnectionFactory(BROKER_URL)
    }

    //@Bean
    //IntegrationFlow jmsOutboundFlow() {
    //return IntegrationFlows.from("bookReaderChannel")
    //.handleWithAdapter(h ->
    //h.jms(this.jmsConnectionFactory).destination("slave-queue"))
    //.get();
    //}

    @Bean
    @serviceActivator(inputChannel = "bookReaderChannel")
    public MessageHandler jsmOutboundAdapter() {
        JmsTemplate template = new DynamicJmsTemplate();
        template.setConnectionFactory(connectionFactory());
        JmsSendingMessageHandler handler = new JmsSendingMessageHandler(template);
        handler.setDestinationName("slave-queue");
        return handler;
    }

    @Bean
    TaskExecutorPartitionHandler partitionHander(){
        TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler()
        partitionHandler.setGridSize(5)
        partitionHandler.setTaskExecutor(new SimpleAsynkTaskExecutor())
        //XXX:Set the right step
        partitionHandler.setSteps(partitionStep)

        partitionHandler
    }

    @Bean
    MultiResourcePartitioner partitioner(){
        MultiResourcePartitioner partitioner = new MultiResourcePartitioner()
        partitioner.setResources(

    }
