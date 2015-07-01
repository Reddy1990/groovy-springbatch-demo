package com.cucharitas.philae.job

import org.springframework.core.io.Resource
import org.springframework.batch.core.step.tasklet.Tasklet

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import org.apache.commons.compress.archivers.tar.TarArchiveInputStream


@Component("unzipFilesTasklet")
public class UnzipFilesTasklet implements Tasklet{

    @Value('${com.cucharitas.philae.input.zippedFile}')
    private Resource inputResource

    @Value('${com.cucharitas.philae.outputDir}')
    private Resource outputDir


    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){

        final TarArchiveInputStream tarIS = new TarArchiveInputStream(this.inputResource.getInputStream())

        assert outputDir.exists()
        FileUtils.cleanDirectory(outputDir.getFile())

        tarIS.each{entry ->
            String filename = entry.getName()
            byte[] content = new byte[(int) entry.getSize()]
            tarIs.read(content, offset, content.length - offset)
            File outputFile = new FileOutputStream(outputDir.createRelative(filename).getFile())
            outputFile.write(content)
        }

        RepeatStatus.FINISHED
    }

}
