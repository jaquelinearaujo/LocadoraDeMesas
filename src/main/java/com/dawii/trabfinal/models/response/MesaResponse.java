package com.dawii.trabfinal.models.response;

import com.dawii.trabfinal.models.Mesa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MesaResponse extends BaseResponse{
    private List<Mesa> mesas;
}
