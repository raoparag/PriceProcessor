DROP TABLE price_data
IF EXISTS;
CREATE TABLE price_data (
  ticker     INTEGER,
  instrument VARCHAR(10),
  price      DOUBLE
);
