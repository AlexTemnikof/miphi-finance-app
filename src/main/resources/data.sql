CREATE OR REPLACE FUNCTION intervals(start TIMESTAMP, endd TIMESTAMP, interval_name VARCHAR)
    RETURNS TABLE
            (
                start_date TIMESTAMP,
                end_date   TIMESTAMP
            )
AS
'
BEGIN
    RETURN QUERY SELECT
                     CASE
                         WHEN gen_date = start
                             THEN gen_date
                         WHEN interval_start < endd
                             THEN interval_start
                         END start_date,
                     CASE
                         WHEN interval_end < endd
                             THEN interval_end
                         ELSE endd
                         END end_date
                 FROM generate_series(start_date, endd,
                                      concat("1 ", interval_name)::INTERVAL) gen_date,
                      date_trunc(interval_name, gen_date) interval_start,
                      date_trunc(interval_name, gen_date + concat("1 ", interval_name)::INTERVAL) interval_end
                 WHERE interval_start < endd;
END ' language plpgsql;


WITH ins AS (
    INSERT INTO public.client (id, email, login, name, password, role, bank_name, client_type, inn, phone_number)
        VALUES (10000000, 'admin@mail.ru', 'admin', 'admin', '123', 'ADMIN', 'admin', 'PERSON', '12323', '3123123')
        ON CONFLICT (id) DO NOTHING
        RETURNING *
)
SELECT CASE
           WHEN EXISTS (SELECT * FROM ins) THEN 'Admin created'
           ELSE 'Admin already exists'
           END AS message;

