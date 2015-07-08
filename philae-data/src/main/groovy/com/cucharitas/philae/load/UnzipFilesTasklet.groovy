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
import org.apache.commons.io.IOUtils

import java.io.BufferedOutputStream
import java.util.zip.ZipInputStream
import java.util.zip.ZipEntry

import groovy.util.logging.Slf4j

@Slf4j
@Component("unzipFilesTasklet")
public class UnzipFilesTasklet implements Tasklet{

    private final static String INPUT_FILE_PATH = "inputDir/files.output.zip"

    private final static String OUTPUT = "tmpFiles"

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){
        Resource inputRes = new ClassPathResource(INPUT_FILE_PATH)
        Resource outputRes = new ClassPathResource(OUTPUT)

        assert inputRes.exists()
        assert outputRes.exists()

        final ZipInputStream zis = new ZipInputStream(
            new BufferedInputStream(inputRes.getInputStream()));

        // Copies each item from zipped file to output
        BufferedOutputStream dest = null;
        ZipEntry ze = zis.getNextEntry();
        while (ze != null) {
            //final File target = outputRes.createRelative(ze.getName()).getFile()
            final File target = new File(outputRes.getFile(), ze.getName())

            log.error target.getPath();

            final FileOutputStream fos = new FileOutputStream(target);
            dest = new BufferedOutputStream(fos);
            IOUtils.copy(zis, dest);
            dest.flush();
            dest.close();
            ze = zis.getNextEntry();
        }
        zis.close();

        RepeatStatus.FINISHED
    }

}
