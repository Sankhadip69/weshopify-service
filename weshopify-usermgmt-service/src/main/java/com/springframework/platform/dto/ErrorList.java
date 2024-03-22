package com.springframework.platform.dto;

import com.springframework.platform.exception.UserException;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorList {

    private List<UserException> messages;
}
