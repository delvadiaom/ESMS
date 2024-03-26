package com.networkKnights.ecms.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
public class ExpCtcDto {
    private int experience;
    private int avgCtc;
    public ExpCtcDto(int experience, int avgCtc) {
        this.experience = experience;
        this.avgCtc = avgCtc;
    }
}
