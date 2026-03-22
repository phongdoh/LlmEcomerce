-- Ensure dashboard-relevant timestamps are initialized consistently.
SET @has_created_at = (
	SELECT COUNT(*)
	FROM INFORMATION_SCHEMA.COLUMNS
	WHERE TABLE_SCHEMA = DATABASE()
	  AND TABLE_NAME = 'user'
	  AND COLUMN_NAME = 'created_at'
);

SET @ddl_user = IF(
	@has_created_at = 0,
	'ALTER TABLE `user` ADD COLUMN `created_at` DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6)',
	'ALTER TABLE `user` MODIFY COLUMN `created_at` DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6)'
);

PREPARE stmt_user FROM @ddl_user;
EXECUTE stmt_user;
DEALLOCATE PREPARE stmt_user;

ALTER TABLE `user`
	MODIFY COLUMN `created_at` DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6);

ALTER TABLE `shop_order`
	MODIFY COLUMN `order_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP;

-- Backfill invalid historical values used by local testing datasets.
UPDATE `user`
SET `created_at` = NOW(6)
WHERE `created_at` IS NULL
   OR `created_at` < '2000-01-01';

UPDATE `shop_order`
SET `order_date` = NOW()
WHERE `order_date` IS NULL
   OR `order_date` < '2000-01-01';

SET @has_product_created_at = (
	SELECT COUNT(*)
	FROM INFORMATION_SCHEMA.COLUMNS
	WHERE TABLE_SCHEMA = DATABASE()
	  AND TABLE_NAME = 'product'
	  AND COLUMN_NAME = 'created_at'
);

SET @ddl_product = IF(
	@has_product_created_at = 0,
	'ALTER TABLE `product` ADD COLUMN `created_at` DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6)',
	'ALTER TABLE `product` MODIFY COLUMN `created_at` DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6)'
);

PREPARE stmt_product FROM @ddl_product;
EXECUTE stmt_product;
DEALLOCATE PREPARE stmt_product;

UPDATE `product`
SET `created_at` = NOW(6)
WHERE `created_at` IS NULL
   OR `created_at` < '2000-01-01';
