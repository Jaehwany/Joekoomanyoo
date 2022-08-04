package com.project.common.dto.Heritage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeritageScrapDto {
    private int heritageScrapSeq;
    private int userSeq;
    private int heritageSeq;
    private String heritageScrapRegistedAt;
}
