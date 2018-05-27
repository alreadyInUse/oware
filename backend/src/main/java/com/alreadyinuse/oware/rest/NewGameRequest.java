package com.alreadyinuse.oware.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewGameRequest {

    private String player1Name;

    private String player2Name;

}
