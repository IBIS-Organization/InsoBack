package com.hoteleria.hoteleria.Repository;

import com.hoteleria.hoteleria.Entity.Purchase;
import com.hoteleria.hoteleria.Entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    List<Purchase> findByUserId(Integer userId);
    List<Purchase> findByUserIdAndPaymentStatus(Integer userId, PaymentStatus paymentStatus);
    @Query(value = "SELECT * FROM fn_list_purchase_report()", nativeQuery = true )
    List<Object[]> getPurchaseReportByDate();
}
