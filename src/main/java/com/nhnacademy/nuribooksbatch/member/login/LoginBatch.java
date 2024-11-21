package com.nhnacademy.nuribooksbatch.member.login;

import com.nhnacademy.nuribooksbatch.member.CustomerIdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class LoginBatch {

    private final DataSource dataSource;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    // 최근 로그인 일시를 확인하는 Job을 정의한다.
    @Bean
    public Job inactiveMembersByLastLoginJob() {

        return new JobBuilder("inactiveMembersByLastLogin", jobRepository)
                .start(checkMembersLastLoginStep())
                .build();
    }

    // inactiveMembersByLastLogin Job을 실행하는 Step을 정의한다.
    @Bean
    public Step checkMembersLastLoginStep() {

        return new StepBuilder("checkMembersLastLogin", jobRepository)
                .<CustomerIdDto, CustomerIdDto> chunk(10, platformTransactionManager)
                .reader(membersLastLoginReader())
                .writer(changeMembersStatusWriter())
                .build();
    }

    // checkMembersLastLogin Step을 실행하는 Reader를 정의한다.
    @Bean
    public JdbcPagingItemReader<CustomerIdDto> membersLastLoginReader() {

        return new JdbcPagingItemReaderBuilder<CustomerIdDto>()
                .name("membersLastLoginReader")
                .dataSource(dataSource)
                .selectClause("SELECT customer_id")
                .fromClause("FROM members")
//                .whereClause("WHERE latest_login_at <= DATE_SUB(CURRENT_DATE, INTERVAL 3 MONTH)")
                .whereClause("WHERE username = 'member27'")
                .sortKeys(Map.of("customer_id", Order.ASCENDING))
                .rowMapper(new BeanPropertyRowMapper<>(CustomerIdDto.class))
                .pageSize(10)
                .build();
    }

    // checkMembersLastLogin Step을 실행하는 Writer를 정의한다.
    @Bean
    public JdbcBatchItemWriter<CustomerIdDto> changeMembersStatusWriter() {

        String sql = "UPDATE members SET status = 'INACTIVE' WHERE customer_id = :customer_id";

        return new JdbcBatchItemWriterBuilder<CustomerIdDto>()
                .dataSource(dataSource)
                .sql(sql)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }
}
