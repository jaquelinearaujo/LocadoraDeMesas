package com.dawii.trabfinal.models.response;

import com.dawii.trabfinal.models.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse extends BaseResponse{
    private List<Item> items;
}
