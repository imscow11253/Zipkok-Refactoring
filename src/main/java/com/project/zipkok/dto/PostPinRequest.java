package com.project.zipkok.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class PostPinRequest {

    @NotBlank @Size(max = 12)
    private String name;

    @Valid
    private PinAddressInfo address;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PinAddressInfo {

        @NotNull @Size(max = 200)
        private String addressName;

        @NotNull
        private Double x;

        @NotNull
        private Double y;
    }
}
