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

/*

APPLICATION ID :- JEH1653819073034
SERIAL KEY :- WXOX-FQEQ-RGCJ-9978

*/