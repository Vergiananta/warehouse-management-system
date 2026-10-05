package com.example.warehouse.warehouse_service.repository;

import com.example.warehouse.warehouse_service.entity.ItemVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemVariantRepository extends JpaRepository<ItemVariant, Long> {
    List<ItemVariant> findByItemId(Long itemId);
    Optional<ItemVariant> findByIdAndItemId(Long id, Long itemId);
    boolean existsBySku(String sku);
    boolean existsBySkuAndIdNot(String sku, Long id);
}
