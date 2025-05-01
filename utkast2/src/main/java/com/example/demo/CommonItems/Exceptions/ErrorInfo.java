package com.example.demo.CommonItems.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorInfo {
    private String errorType; // mer spesifikk feiltype. f.eks "uventet feil" eller ugyldig argument
    private String message; // detaljert feilmelding
    private String exceptionClass;
}
