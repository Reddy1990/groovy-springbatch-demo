package com.cucharitas.philae.job

import org.springframework.core.io.Resource
import org.springframework.core.io.ClassPathResource
import org.springframework.batch.core.step.tasklet.Tasklet

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.apache.commons.io.FileUtils

import groovy.util.logging.Slf4j
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream

@Slf4j
@Component("unzipFilesTasklet")
public class UnzipFilesTasklet implements Tasklet{

    private final static String INPUT_FILE_PATH = "inputDir/files.output.tar.gz"

    private final static String OUTPUT = "tmpFiles"


    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){
        Resource inputRes = new ClassPathResource(INPUT_FILE_PATH)
        Resource outputRes = new ClassPathResource(OUTPUT)

        log.info "loaded resources"

        final TarArchiveInputStream tarIS = new TarArchiveInputStream(inputRes.getInputStream())
        assert outputRes.exists()
        FileUtils.cleanDirectory(outputRes.getFile())

        tarIS.each{entry ->
            String filename = entry.getName()
            log.info "unzipping $filename"
            byte[] content = new byte[(int) entry.getSize()]
            tarIs.read(content, offset, content.length - offset)
            File outputFile = new FileOutputStream(outputRes.createRelative(filename).getFile())
            outputFile.write(content)
        }

        RepeatStatus.FINISHED
    }

}
