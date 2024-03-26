package com.networkKnights.ecms.dto;
;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeRatingDto {
    private long ratingId;
    private long empId;
    private long rating;
    private Date modifiedDate;
    private Date createdDate;
    private boolean isActive;
}
