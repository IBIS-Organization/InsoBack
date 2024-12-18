package com.hoteleria.hoteleria.Mapper;

import com.hoteleria.hoteleria.DTO.PurchaseCreateDTO;
import com.hoteleria.hoteleria.DTO.PurchaseDTO;
import com.hoteleria.hoteleria.DTO.PurchaseItemCreateDTO;
import com.hoteleria.hoteleria.DTO.PurchaseItemDTO;
import com.hoteleria.hoteleria.Entity.Purchase;
import com.hoteleria.hoteleria.Entity.PurchaseItem;
import com.hoteleria.hoteleria.Entity.Reserva;
import com.hoteleria.hoteleria.Entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {
    private final ModelMapper modelMapper;

    public PurchaseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    private PurchaseItem toPurchaseItemEntity(PurchaseItemCreateDTO purchaseItemDTO) {
        PurchaseItem item = modelMapper.map(purchaseItemDTO, PurchaseItem.class);
        Reserva reserva = new Reserva();
        reserva.setId(purchaseItemDTO.getReservaId());
        item.setReserva(reserva);
        return item;
    }

    private PurchaseItemDTO toPurchaseItemDTO(PurchaseItem purchaseItem) {
        PurchaseItemDTO purchaseItemDTO = modelMapper.map(purchaseItem, PurchaseItemDTO.class);
        purchaseItemDTO.setRoomName(purchaseItem.getReserva().getHabitacion().getCategoria());
        return purchaseItemDTO;
    }


    public Purchase toPurchaseCreateDTO(PurchaseCreateDTO purchaseCreateDTO){
        Purchase purchase = modelMapper.map(purchaseCreateDTO, Purchase.class);
        User user = new User();
        user.setId(purchaseCreateDTO.getUserId());
        purchase.setUser(user);

        purchase.setItems(purchaseCreateDTO.getItems().stream()
                .map(this::toPurchaseItemEntity)
                .toList());

        return purchase;
    }

    public PurchaseDTO toPurchaseDTO(Purchase purchase){
        PurchaseDTO purchaseDTO = modelMapper.map(purchase, PurchaseDTO.class);
        purchaseDTO.setUser(purchase.getUser().getFirstName()+" "+purchase.getUser().getLastName());
        purchaseDTO.setItems(purchase.getItems().stream()
                .map(this::toPurchaseItemDTO)
                .toList());
        return purchaseDTO;
    }
}

