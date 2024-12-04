package com.hoteleria.hoteleria.Service.impl;

import com.hoteleria.hoteleria.DTO.PurchaseCreateDTO;
import com.hoteleria.hoteleria.DTO.PurchaseDTO;
import com.hoteleria.hoteleria.DTO.PurchaseReportDTO;
import com.hoteleria.hoteleria.Entity.Purchase;
import com.hoteleria.hoteleria.Entity.Reserva;
import com.hoteleria.hoteleria.Entity.User;
import com.hoteleria.hoteleria.Entity.enums.PaymentStatus;
import com.hoteleria.hoteleria.Exceptions.ResourceNotFoundException;
import com.hoteleria.hoteleria.Mapper.PurchaseMapper;
import com.hoteleria.hoteleria.Repository.PurchaseRepository;
import com.hoteleria.hoteleria.Repository.ReservaRepository;
import com.hoteleria.hoteleria.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PurchaseServiceImpl implements PurchaseService{

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final PurchaseMapper purchaseMapper;
    private final ReservaRepository reservaRepository;

    @Override
    @Transactional
    public PurchaseDTO createPurchase(PurchaseCreateDTO purchaseCreateDTO) {

        Purchase purchase = purchaseMapper.toPurchaseCreateDTO(purchaseCreateDTO);

        User user = userRepository.findById(purchaseCreateDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + purchaseCreateDTO.getUserId()));
        purchase.getItems().forEach(item -> {
            Reserva reserva = reservaRepository.findById(item.getReserva().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));
            item.setReserva(reserva);
            item.setPurchase(purchase);
        });

        Float total = purchase.getItems()
                .stream()
                .map(item -> item.getPrice() * item.getQuantity())
                .reduce(0f, Float::sum);

        purchase.setCreateAt(LocalDateTime.now());
        purchase.setPaymentStatus(PaymentStatus.PENDING);
        purchase.setUser(user);
        purchase.setTotal(total);
        purchase.getItems().forEach(item -> item.setPurchase(purchase));
        Purchase savePurchase =  purchaseRepository.save(purchase);
        return purchaseMapper.toPurchaseDTO(savePurchase);
    }

    @Override
    public List<PurchaseDTO> getPaidPurchasesByUserId(Integer userId) {
        return purchaseRepository.findByUserIdAndPaymentStatus(userId, PaymentStatus.PAID).stream()
                .map(purchaseMapper::toPurchaseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchaseDTO> getPurchaseHistoryByUserId(Integer userId) {
        return purchaseRepository.findByUserId(userId).stream()
                .map(purchaseMapper::toPurchaseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseReportDTO> getPurchaseReportByDate() {
        List<Object[]> results =  purchaseRepository.getPurchaseReportByDate();
        List<PurchaseReportDTO> purchaseReportDTOS = results.stream()
                .map(result ->
                        new PurchaseReportDTO(
                                ((Integer) result[0]).intValue(),
                                (String) result[1]
                        )
                ).toList();
        return purchaseReportDTOS;
    }

    @Override
    public List<PurchaseDTO> getAllPurchases() {
        return purchaseRepository.findAll()
                .stream()
                .map(purchaseMapper::toPurchaseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseDTO getPurchaseById(Integer id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id: " + id));

        return purchaseMapper.toPurchaseDTO(purchase);
    }

    @Override
    @Transactional
    public PurchaseDTO confirmPurchase(Integer purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id: " + purchaseId));


        /*Float total = purchase.getItems()
                .stream()
                .map(item -> item.getPrice() * item.getQuantity())
                .reduce(0f, Float::sum);

        purchase.setTotal(total);*/
        purchase.setPaymentStatus(PaymentStatus.PAID);

        Purchase updatePurchase = purchaseRepository.save(purchase);
        return purchaseMapper.toPurchaseDTO(updatePurchase);
    }
}

