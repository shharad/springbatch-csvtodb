package com.end.csvtodb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "T_RSS_SERVICE_TST")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RssService {

    @Id
    @Column(name = "SERVICE_ID")
    private int serviceId;

    @Column(name = "SERVICE_NAME")
    private String serviceName;

    @Column(name = "SERVICE_TYPE")
    private String serviceType;

    @Column(name = "SOURCE_SYSTEM")
    private String sourceSystem;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_ON")
    private Date createdOn;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Column(name = "MODIFIED_ON")
    private Date modifiedOn;

}
