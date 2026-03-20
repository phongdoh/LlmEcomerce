package com.nhs.individual.repository;

import com.nhs.individual.domain.EmbeddedId.ProductItemInWarehouseId;
import com.nhs.individual.domain.WarehouseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseItemRepository extends JpaRepository<WarehouseItem, ProductItemInWarehouseId> {
    @Modifying
    @Query(value = "delete from product_item_in_warehouse where product_item_id in (select id from product_item where product_id = :productId)", nativeQuery = true)
    void deleteByProductId(@Param("productId") Integer productId);
    
    @Modifying
    @Query(value = "delete from product_item_in_warehouse where product_item_id = :productItemId", nativeQuery = true)
    void deleteByProductItemId(@Param("productItemId") Integer productItemId);

    @Query(value = """
            SELECT p.name AS productName, COALESCE(SUM(piw.quantity), 0) AS currentStock
            FROM product_item_in_warehouse piw
            JOIN product_item pi ON pi.id = piw.product_item_id
            JOIN product p ON p.id = pi.product_id
            GROUP BY p.id, p.name
            HAVING COALESCE(SUM(piw.quantity), 0) < :threshold
            ORDER BY currentStock ASC, p.name ASC
            """, nativeQuery = true)
    List<LowStockProjection> findLowStockProducts(@Param("threshold") Integer threshold);

    interface LowStockProjection {
        String getProductName();

        Integer getCurrentStock();
    }
}
