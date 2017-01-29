set default_transaction_isolation="read committed";
show default_transaction_isolation;
create user qumla with LOGIN password 'Qu312';
alter database qumla owner to qumla;
ALTER DEFAULT PRIVILEGES IN SCHEMA qumla GRANT SELECT, INSERT, UPDATE, DELETE ON tables TO qumla;
ALTER DEFAULT PRIVILEGES IN SCHEMA qumla GRANT SELECT, USAGE ON sequences TO qumla;
GRANT SELECT, INSERT, UPDATE, delete ON ALL TABLES IN SCHEMA qumla to qumla;
GRANT SELECT, USAGE ON ALL sequences IN SCHEMA qumla to qumla;
grant all on schema qumla to qumla;

# login psql -h localhost -U qumla qumla

set search_path=qumla,public

CREATE FUNCTION crypt(text, text) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/pgcrypto', 'pg_crypt';


ALTER FUNCTION crypt(text, text) OWNER TO qumla;

--
-- Name: encrypt(bytea, bytea, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION encrypt(bytea, bytea, text) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/pgcrypto', 'pg_encrypt';


ALTER FUNCTION encrypt(bytea, bytea, text) OWNER TO qumla;

--
-- Name: encrypt_iv(bytea, bytea, bytea, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION encrypt_iv(bytea, bytea, bytea, text) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/pgcrypto', 'pg_encrypt_iv';


ALTER FUNCTION encrypt_iv(bytea, bytea, bytea, text) OWNER TO qumla;

--
-- Name: gen_salt(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION gen_salt(text) RETURNS text
    LANGUAGE c STRICT
    AS '$libdir/pgcrypto', 'pg_gen_salt';


ALTER FUNCTION gen_salt(text) OWNER TO qumla;

--
-- Name: gen_salt(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION gen_salt(text, integer) RETURNS text
    LANGUAGE c STRICT
    AS '$libdir/pgcrypto', 'pg_gen_salt_rounds';


ALTER FUNCTION gen_salt(text, integer) OWNER TO qumla;
