SET NAMES utf8mb4;

-- =========================================================
-- OMAMORI SPA - PHASE 2 PUBLIC CATALOG SEED
-- =========================================================

-- =========================================================
-- 1. TREATMENTS
-- =========================================================
USE omamori_treatment;

INSERT INTO treatment
    (id, category, description, duration_minutes, is_active, name, price)
VALUES
(
    '11111111-1111-4111-8111-111111111101',
    'Massage',
    'Liệu trình massage toàn thân giúp thư giãn cơ thể và giảm cảm giác mệt mỏi sau ngày dài.',
    60,
    b'1',
    'Massage thư giãn toàn thân',
    450000.00
),
(
    '11111111-1111-4111-8111-111111111102',
    'Chăm sóc da',
    'Quy trình làm sạch, chăm sóc và dưỡng ẩm da mặt cơ bản phù hợp với nhu cầu chăm sóc định kỳ.',
    50,
    b'1',
    'Chăm sóc da mặt cơ bản',
    390000.00
),
(
    '11111111-1111-4111-8111-111111111103',
    'Trị liệu',
    'Liệu trình tập trung vào vùng cổ, vai và gáy, kết hợp các thao tác thư giãn phù hợp.',
    60,
    b'1',
    'Trị liệu cổ vai gáy',
    520000.00
),
(
    '11111111-1111-4111-8111-111111111104',
    'Chăm sóc da',
    'Liệu trình chăm sóc da chuyên sâu gồm làm sạch, chăm sóc và dưỡng da theo nhiều bước.',
    75,
    b'1',
    'Chăm sóc da chuyên sâu',
    690000.00
),
(
    '11111111-1111-4111-8111-111111111105',
    'Massage',
    'Liệu trình massage kết hợp đá nóng nhằm mang lại cảm giác thư giãn và dễ chịu cho cơ thể.',
    75,
    b'1',
    'Massage đá nóng',
    650000.00
),
(
    '11111111-1111-4111-8111-111111111106',
    'Phục hồi',
    'Liệu trình chăm sóc cơ thể kéo dài với các bước thư giãn và phục hồi phù hợp.',
    90,
    b'1',
    'Liệu trình phục hồi cơ thể',
    890000.00
)
ON DUPLICATE KEY UPDATE
    category = VALUES(category),
    description = VALUES(description),
    duration_minutes = VALUES(duration_minutes),
    is_active = VALUES(is_active),
    name = VALUES(name),
    price = VALUES(price);


-- =========================================================
-- 2. COSMETICS
-- =========================================================
USE omamori_cosmetics;

INSERT INTO cosmetic
    (id, name, brand, manufacturer, unit, price, description)
VALUES
(
    '22222222-2222-4222-8222-222222222201',
    'Sữa rửa mặt dịu nhẹ',
    'Omamori',
    'Omamori Wellness',
    'chai',
    290000.00,
    'Sản phẩm làm sạch da dịu nhẹ, phù hợp sử dụng trong quy trình chăm sóc da hằng ngày.'
),
(
    '22222222-2222-4222-8222-222222222202',
    'Toner cân bằng da',
    'Omamori',
    'Omamori Wellness',
    'chai',
    320000.00,
    'Toner hỗ trợ cân bằng và chuẩn bị bề mặt da cho các bước chăm sóc tiếp theo.'
),
(
    '22222222-2222-4222-8222-222222222203',
    'Serum phục hồi da',
    'Omamori',
    'Omamori Wellness',
    'chai',
    650000.00,
    'Serum dùng trong quy trình chăm sóc da, tập trung vào nhu cầu dưỡng và phục hồi bề mặt da.'
),
(
    '22222222-2222-4222-8222-222222222204',
    'Kem dưỡng ẩm',
    'Omamori',
    'Omamori Wellness',
    'hũ',
    580000.00,
    'Kem dưỡng sử dụng ở bước cuối của quy trình chăm sóc nhằm duy trì độ ẩm cho da.'
),
(
    '22222222-2222-4222-8222-222222222205',
    'Mặt nạ dưỡng da',
    'Omamori',
    'Omamori Wellness',
    'hộp',
    350000.00,
    'Mặt nạ chăm sóc da dùng tại spa hoặc tại nhà theo hướng dẫn sử dụng phù hợp.'
),
(
    '22222222-2222-4222-8222-222222222206',
    'Dầu massage thư giãn',
    'Omamori',
    'Omamori Wellness',
    'chai',
    420000.00,
    'Dầu massage sử dụng trong các liệu trình chăm sóc và thư giãn cơ thể.'
)
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    brand = VALUES(brand),
    manufacturer = VALUES(manufacturer),
    unit = VALUES(unit),
    price = VALUES(price),
    description = VALUES(description);
