-- Optional test seed: completed orders in Feb 2026 for dashboard growth verification.
-- This script is idempotent and safe to run multiple times.

INSERT INTO `shop_order_payment` (`created_at`, `order_number`, `status`, `update_at`, `payment_id`)
SELECT '2026-02-10 09:15:00', 'MOCK-FEB-2026-001', 1, '2026-02-10 09:15:00', 1
WHERE NOT EXISTS (
    SELECT 1 FROM `shop_order_payment` WHERE `order_number` = 'MOCK-FEB-2026-001'
);

INSERT INTO `shop_order_payment` (`created_at`, `order_number`, `status`, `update_at`, `payment_id`)
SELECT '2026-02-16 14:20:00', 'MOCK-FEB-2026-002', 1, '2026-02-16 14:20:00', 1
WHERE NOT EXISTS (
    SELECT 1 FROM `shop_order_payment` WHERE `order_number` = 'MOCK-FEB-2026-002'
);

INSERT INTO `shop_order_payment` (`created_at`, `order_number`, `status`, `update_at`, `payment_id`)
SELECT '2026-02-24 18:05:00', 'MOCK-FEB-2026-003', 1, '2026-02-24 18:05:00', 1
WHERE NOT EXISTS (
    SELECT 1 FROM `shop_order_payment` WHERE `order_number` = 'MOCK-FEB-2026-003'
);

SET @pay1 = (SELECT `id` FROM `shop_order_payment` WHERE `order_number` = 'MOCK-FEB-2026-001' LIMIT 1);
SET @pay2 = (SELECT `id` FROM `shop_order_payment` WHERE `order_number` = 'MOCK-FEB-2026-002' LIMIT 1);
SET @pay3 = (SELECT `id` FROM `shop_order_payment` WHERE `order_number` = 'MOCK-FEB-2026-003' LIMIT 1);

INSERT INTO `shop_order` (`note`, `order_date`, `total`, `user_id`, `address_id`, `payment_id`, `shipping_method`)
SELECT 'Mock order for Feb growth #1', '2026-02-10 09:15:00', 12500000, 1, NULL, @pay1, 1
WHERE @pay1 IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM `shop_order` WHERE `payment_id` = @pay1);

INSERT INTO `shop_order` (`note`, `order_date`, `total`, `user_id`, `address_id`, `payment_id`, `shipping_method`)
SELECT 'Mock order for Feb growth #2', '2026-02-16 14:20:00', 17800000, 1, NULL, @pay2, 1
WHERE @pay2 IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM `shop_order` WHERE `payment_id` = @pay2);

INSERT INTO `shop_order` (`note`, `order_date`, `total`, `user_id`, `address_id`, `payment_id`, `shipping_method`)
SELECT 'Mock order for Feb growth #3', '2026-02-24 18:05:00', 9900000, 2, NULL, @pay3, 1
WHERE @pay3 IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM `shop_order` WHERE `payment_id` = @pay3);

SET @order1 = (SELECT `id` FROM `shop_order` WHERE `payment_id` = @pay1 LIMIT 1);
SET @order2 = (SELECT `id` FROM `shop_order` WHERE `payment_id` = @pay2 LIMIT 1);
SET @order3 = (SELECT `id` FROM `shop_order` WHERE `payment_id` = @pay3 LIMIT 1);

-- Mark all mock orders as COMPLETED (status id = 7).
INSERT INTO `shop_order_status` (`detail`, `note`, `shop_order_id`, `status`, `update_at`)
SELECT 'Completed', 'Mock completed order #1', @order1, 7, '2026-02-10 10:00:00'
WHERE @order1 IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM `shop_order_status`
      WHERE `shop_order_id` = @order1 AND `status` = 7
  );

INSERT INTO `shop_order_status` (`detail`, `note`, `shop_order_id`, `status`, `update_at`)
SELECT 'Completed', 'Mock completed order #2', @order2, 7, '2026-02-16 15:00:00'
WHERE @order2 IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM `shop_order_status`
      WHERE `shop_order_id` = @order2 AND `status` = 7
  );

INSERT INTO `shop_order_status` (`detail`, `note`, `shop_order_id`, `status`, `update_at`)
SELECT 'Completed', 'Mock completed order #3', @order3, 7, '2026-02-24 18:30:00'
WHERE @order3 IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM `shop_order_status`
      WHERE `shop_order_id` = @order3 AND `status` = 7
  );
