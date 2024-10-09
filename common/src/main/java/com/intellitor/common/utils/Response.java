package com.intellitor.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private int status;
    private Object content;
    private final Instant timestamp = Instant.now();
}
