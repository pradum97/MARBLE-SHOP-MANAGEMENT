update tbl_product_stock set quantity = (quantity*6) ,
                             quantity_unit='PCS' where quantity_unit = 'PKT';

update tbl_saleitems set product_quantity = concat((NULLIF(SPLIT_PART(product_quantity, ' -', 1), '')::numeric *6) ,' -PCS')
where SPLIT_PART(product_quantity, ' -', 2) = 'PKT';

CREATE TABLE IF NOT EXISTS TBL_LICENSE
(
    LICENSE_ID          SERIAL PRIMARY KEY,
    COMPANY_ID          INT     unique     NOT NULL,
    APPLICATION_ID      VARCHAR(50) unique NOT NULL,
    SERIAL_KEY          VARCHAR(100) unique NOT NULL,
    START_ON            VARCHAR(20)  NOT NULL,
    EXPIRES_ON          VARCHAR(20)  NOT NULL,
    LICENSE_TYPE        VARCHAR(50)  NOT NULL,
    LICENSE_PERIOD_DAYS INTEGER      NOT NULL,
    REGISTERED_EMAIL    VARCHAR(100) NOT NULL
);

delete from tbl_cart;

CREATE TABLE TBL_COLOR
(
    COLOR_ID   SERIAL PRIMARY KEY,
    COLOR_NAME VARCHAR(100) NOT NULL
);

insert into tbl_color(color_name)
values ('White'),('Dark grey'),('Blue'),
       ('Brown'),('Black'),('Gold'),('Orange'),('Red'),
       ('Green'),('Black porcelain'),('Multicolour'),('Other');

ALTER TABLE tbl_product_stock ADD COLUMN PRICE_TYPE VARCHAR(20) ,
                              ADD COLUMN PCS_PER_PACKET INTEGER ;

ALTER TABLE tbl_saleitems ADD COLUMN PRICE_TYPE VARCHAR(20) ,
                              ADD COLUMN PCS_PER_PACKET INTEGER;

alter table tbl_cart add column price_type varchar(50);

alter table proposal_items add column price_type varchar(50)

-- update product


--APPLICATION ID :- JEH1653819073034
--SERIAL KEY :- UXMX-UQRU-SWDC-8165