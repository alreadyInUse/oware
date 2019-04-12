package com.bogdansucaciu.oware.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGameRequest {

    private Integer player;

    private Integer pitId;

    private Integer pitIndex;
}
