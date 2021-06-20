package com.dawii.trabfinal.models.response;

import com.dawii.trabfinal.models.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseResponse implements Serializable {

    private Status status = Status.SUCCESS;
    private List<String> messages = new ArrayList<>();
}