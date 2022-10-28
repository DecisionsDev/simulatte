package com.ibm.simulatte.core.mocks.loan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Loan {
    private String amount;
    private int duration;
    private String yearlyInterestRate;
    private String yearlyRepayment;
    private boolean approved;
    private String [] messages;
    private String approvalStatus;

}
