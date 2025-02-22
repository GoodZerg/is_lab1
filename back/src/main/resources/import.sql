CREATE OR REPLACE FUNCTION calculate_average_meters_above_sea_level()
RETURNS DOUBLE PRECISION AS $$
BEGIN
RETURN (SELECT AVG(meters_above_sea_level) FROM city);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION find_cities_by_name_prefix(prefix TEXT)
RETURNS TABLE (
    id BIGINT,
    name TEXT,
    coordinates_id BIGINT,
    creation_date TIMESTAMP,
    area INT,
    population INT,
    establishment_date TIMESTAMP,
    capital BOOLEAN,
    meters_above_sea_level BIGINT,
    climate TEXT,
    government TEXT,
    standard_of_living TEXT,
    governor_id BIGINT,
    user_id BIGINT
) AS $$
BEGIN
RETURN QUERY SELECT * FROM city WHERE name LIKE prefix || '%';
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION find_cities_by_governor_height(max_height DOUBLE PRECISION)
RETURNS TABLE (
    id BIGINT,
    name TEXT,
    coordinates_id BIGINT,
    creation_date TIMESTAMP,
    area INT,
    population INT,
    establishment_date TIMESTAMP,
    capital BOOLEAN,
    meters_above_sea_level BIGINT,
    climate TEXT,
    government TEXT,
    standard_of_living TEXT,
    governor_id BIGINT,
    user_id BIGINT
) AS $$
BEGIN
RETURN QUERY
SELECT * FROM city JOIN human h ON governor_id = h.id
WHERE h.height < max_height;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION relocate_all_population(source_city_id BIGINT)
RETURNS TEXT AS $$
DECLARE
target_city_id BIGINT;
    population_to_relocate INT;
BEGIN
    -- Находим город с наименьшим населением
SELECT id INTO target_city_id
FROM city
ORDER BY population
    LIMIT 1;

-- Получаем население исходного города
SELECT population INTO population_to_relocate
FROM city
WHERE id = source_city_id;

-- Переселяем население
UPDATE city
SET population = population + population_to_relocate
WHERE id = target_city_id;

UPDATE city
SET population = 0
WHERE id = source_city_id;

RETURN 'All population relocated successfully';
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION relocate_half_capital_population()
RETURNS TEXT AS $$
DECLARE
capital_city_id BIGINT;
    population_to_relocate INT;
    target_city_ids BIGINT[];
BEGIN
    -- Находим столицу
SELECT id INTO capital_city_id
FROM city
WHERE capital = TRUE;

-- Получаем 50% населения столицы
SELECT population / 2 INTO population_to_relocate
FROM city
WHERE id = capital_city_id;

-- Находим три города с наименьшим населением
SELECT ARRAY(
           SELECT id
        FROM city
        ORDER BY population
        LIMIT 3
    ) INTO target_city_ids;

-- Переселяем население
FOR i IN 1..array_length(target_city_ids, 1) LOOP
UPDATE city
SET population = population + (population_to_relocate / 3)
WHERE id = target_city_ids[i];
END LOOP;

    -- Уменьшаем население столицы
UPDATE city
SET population = population - population_to_relocate
WHERE id = capital_city_id;

RETURN '50% of capital population relocated successfully';
END;
$$ LANGUAGE plpgsql;