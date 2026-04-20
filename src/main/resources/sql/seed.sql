-- Users
insert into public.users (id, username, email, is_deleted, created_at, updated_at, deleted_at) values ('52d3b753-b7d5-41a5-adf8-c967308aa9ba', 'liuliu', 'liuliugit@gmail.com', false, '2026-04-18 03:36:40.371540 +00:00', '2026-04-19 15:46:45.346050 +00:00', null);

-- Products
insert into public.products (id, product_category_id, product_name, unit_price, is_deleted, created_at, updated_at) values ('321bca7a-7b1e-4064-be7a-557318a3e2eb', '97f9c074-687b-4ecf-a5b9-31d98c899d40', '電容', 0.0001, false, '2026-04-18 03:57:35.033755 +00:00', '2026-04-18 03:57:35.033755 +00:00');

-- Product Categories
insert into public.product_categories (id, category_name, tax_rate, calculation_type, created_at, updated_at) values ('97f9c074-687b-4ecf-a5b9-31d98c899d40', '被動元件', 0.0003, 'ELECTRONICS', '2026-04-18 03:56:54.459069 +00:00', '2026-04-18 03:56:54.459069 +00:00');
