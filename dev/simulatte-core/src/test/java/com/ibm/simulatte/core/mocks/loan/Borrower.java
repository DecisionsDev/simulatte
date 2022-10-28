package com.ibm.simulatte.core.mocks.loan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Borrower {

    @JsonProperty("name")
    private String name;

    @JsonProperty("creditScore")
    private int creditScore;

    @JsonProperty("yearlyIncome")
    private int yearlyIncome;

}
