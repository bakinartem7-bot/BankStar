package com.starbank.recommendations.dto;

import java.util.List;

public class RuleListResponseDto {
    private List<RuleResponseDto> data;

    public RuleListResponseDto(List<RuleResponseDto> data) {
        this.data = data;
    }

    public List<RuleResponseDto> getData() { return data; }
    public void setData(List<RuleResponseDto> data) { this.data = data; }
}
