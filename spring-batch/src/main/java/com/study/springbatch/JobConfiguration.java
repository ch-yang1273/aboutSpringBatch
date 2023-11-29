package com.study.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class JobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        // step은 기본적으로 tasklet을 무한 반복시킨다.
                        System.out.println("===========================");
                        System.out.println(">> Step1 Spring Batch!!!");

                        // StepContribution에서 JobParameters를 참조
                        JobParameters jobParameters1 = contribution.getStepExecution().getJobExecution().getJobParameters();
                        String name = jobParameters1.getString("name");
                        Long seq = jobParameters1.getLong("seq");
                        Date date = jobParameters1.getDate("date");
                        Double age = jobParameters1.getDouble("age");
                        System.out.println("Name: " + name + ", Seq: " + seq + ", Date: " + date + ", Age: " + age);

                        // ChunkContext에서 JobParameters를 참조
                        Map<String, Object> jobParameters2 = chunkContext.getStepContext().getJobParameters();
                        System.out.println(jobParameters2);

                        System.out.println("===========================");

                        return RepeatStatus.FINISHED; // 종료
                    }
                })
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("===========================");
                        System.out.println(">> Step2 Spring Batch!!!");
                        System.out.println("===========================");
                        return RepeatStatus.FINISHED; // 종료
                    }
                })
                .build();
    }
}
