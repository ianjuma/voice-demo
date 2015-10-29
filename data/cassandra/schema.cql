use morpheus;

DROP TABLE IF EXISTS billing;
CREATE TABLE billing (
  agent_id int,
  insertion_time timestamp,
  insertion_day int,
  source int,
  ref_id varchar,
  currency_code varchar,
  amount decimal,
  PRIMARY KEY (agent_id, insertion_day, insertion_time, ref_id)
) WITH CLUSTERING ORDER BY (insertion_day DESC, insertion_time DESC);

CREATE INDEX billing_source ON billing(source);

DROP TABLE IF EXISTS payment_log;
CREATE TABLE payment_log (
  category int,
  transaction_id varchar,
  PRIMARY KEY ((category, transaction_id))
);

DROP TABLE IF EXISTS incoming_sms_log;
CREATE TABLE incoming_sms_log (
  message_id varchar,
  PRIMARY KEY (message_id)
);

DROP TABLE IF EXISTS airtime_voucher;
CREATE TABLE airtime_voucher (
  product_id int,
  insertion_day int,
  insertion_time timestamp,
  voucher_id varchar,
  voucher_code varchar,
  currency_code varchar,
  amount decimal,
  PRIMARY KEY (product_id, insertion_day, insertion_time, voucher_id)
) WITH CLUSTERING ORDER BY (insertion_day DESC, insertion_time DESC);

DROP TABLE IF EXISTS airtime_voucher_status;
CREATE TABLE airtime_voucher_status (
  voucher_id varchar,
  status int,
  PRIMARY KEY (voucher_id)
);

DROP TABLE IF EXISTS airtime_voucher_request;
CREATE TABLE airtime_voucher_request (
  agent_id int,
  insertion_day int,
  insertion_time timestamp,
  voucher_id varchar,
  product_id int,
  recipient varchar,
  voucher_code varchar,
  currency_code varchar,
  amount decimal,
  commission decimal,
  PRIMARY KEY (agent_id, insertion_day, insertion_time, voucher_id)
) WITH CLUSTERING ORDER BY (insertion_day DESC, insertion_time DESC);

CREATE INDEX airtime_voucher_request_product ON airtime_voucher_request(product_id);
CREATE INDEX airtime_voucher_request_recipient ON airtime_voucher_request(recipient);

DROP TABLE IF EXISTS pinless_airtime_request;
CREATE TABLE pinless_airtime_request (
  agent_id int,
  insertion_day int,
  insertion_time timestamp,
  request_id varchar,
  product_id int,
  recipient varchar,
  currency_code varchar,
  amount decimal,
  commission decimal,
  PRIMARY KEY (agent_id, insertion_day, insertion_time, request_id)
) WITH CLUSTERING ORDER BY (insertion_day DESC, insertion_time DESC);

CREATE INDEX pinless_airtime_request_product ON pinless_airtime_request(product_id);
CREATE INDEX pinless_airtime_request_recipient ON pinless_airtime_request(recipient);

DROP TABLE IF EXISTS pinless_airtime_status;
CREATE TABLE pinless_airtime_status (
  request_id varchar,
  status int,
  PRIMARY KEY (request_id)
);

DROP TABLE IF EXISTS crunch_day_view;
CREATE TABLE crunch_day_view (
  span varchar,
  category varchar,
  filter varchar,
  metric varchar,
  bucket bigint,
  key varchar,
  value bigint,
  PRIMARY KEY((span, category, filter, metric), bucket, key)
) WITH CLUSTERING ORDER BY (bucket ASC, key ASC);

DROP TABLE IF EXISTS crunch_hour_view;
CREATE TABLE crunch_hour_view (
  span varchar,
  category varchar,
  filter varchar,
  metric varchar,
  bucket bigint,
  key varchar,
  value bigint,
  PRIMARY KEY((span, category, filter, metric), bucket, key)
) WITH CLUSTERING ORDER BY (bucket ASC, key ASC);
