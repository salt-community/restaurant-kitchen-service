package com.example.restaurant.kitchen_service.mapper;

import com.example.restaurant.kitchen_service.model.Item;
import com.example.restaurant.kitchen_service.model.TicketItemEntity;
import java.util.List;

public final class ItemMapper {
    private ItemMapper() {}
    public static TicketItemEntity toEntity(Item dto) {
        var e = new TicketItemEntity();
        e.setSku(dto.sku());
        e.setQty(dto.qty());
        return e;
    }
    public static Item toDto(TicketItemEntity e) {
        return new Item(e.getSku(), e.getQty());
    }
    public static List<TicketItemEntity> toEntities(List<Item> items) {
        return items.stream().map(ItemMapper::toEntity).toList();
    }
    public static List<Item> toDtos(List<TicketItemEntity> items) {
        return items.stream().map(ItemMapper::toDto).toList();
    }
}
