package com.end.csvtodb.util;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AppProperties {

    @Value("${files.input.path}")
    String fileInputPath;

}
