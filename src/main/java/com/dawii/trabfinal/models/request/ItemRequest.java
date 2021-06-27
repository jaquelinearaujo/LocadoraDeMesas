package com.dawii.trabfinal.models.request;

import com.dawii.trabfinal.models.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    private Item item;
}
